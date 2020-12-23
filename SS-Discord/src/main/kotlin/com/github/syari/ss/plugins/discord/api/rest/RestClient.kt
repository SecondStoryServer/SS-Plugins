package com.github.syari.ss.plugins.discord.api.rest

import com.github.syari.ss.plugins.discord.api.DiscordAPI.API_VERSION
import com.github.syari.ss.plugins.discord.api.DiscordAPI.GITHUB_URL
import com.github.syari.ss.plugins.discord.api.DiscordAPI.GSON
import com.github.syari.ss.plugins.discord.api.DiscordAPI.LOGGER
import com.github.syari.ss.plugins.discord.api.DiscordAPI.token
import com.github.syari.ss.plugins.discord.api.exception.DiscordException
import com.github.syari.ss.plugins.discord.api.exception.MissingPermissionsException
import com.github.syari.ss.plugins.discord.api.exception.NotFoundException
import com.github.syari.ss.plugins.discord.api.exception.RateLimitedException
import com.github.syari.ss.plugins.discord.api.util.buildHttpClient
import com.github.syari.ss.plugins.discord.api.util.buildHttpRequest
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration

object RestClient {
    private const val DISCORD_API_URL = "https://discord.com/api/v$API_VERSION"

    private val client = buildHttpClient {
        followRedirects(HttpClient.Redirect.NORMAL)
        connectTimeout(Duration.ofSeconds(10))
    }

    fun newWebSocketBuilder() = client.newWebSocketBuilder()

    private val mutex = Any()

    fun request(endPoint: EndPoint, data: JsonObject? = null, rateLimitRetries: Int = 50): JsonElement {
        synchronized(mutex) {
            repeat(rateLimitRetries) {
                RateLimiter.wait(endPoint)

                try {
                    var url = DISCORD_API_URL + endPoint.path
                    val response = client.send(buildHttpRequest {
                        header("Accept", "application/json")
                        header("Authorization", "Bot $token")
                        header("User-Agent", "DiscordBot ($GITHUB_URL)")
                        if (endPoint.method == HttpMethod.Get) {
                            if (data != null) {
                                val parameter = data.entrySet().joinToString("&") { "${it.key}=${it.value}" }
                                url += "?$parameter"
                            }
                            method(endPoint.method.value, HttpRequest.BodyPublishers.noBody())
                        } else {
                            method(endPoint.method.value, HttpRequest.BodyPublishers.ofString(data?.toString() ?: "{}"))
                            header("Content-Type", "application/json; charset=UTF-8")
                        }
                        uri(URI.create(url))
                    }, HttpResponse.BodyHandlers.ofString())
                    val headers = response.headers()
                    val contentType = headers.firstValue("Content-Type").or { null }.orElse(null)
                    val body = response.body()
                    val statusCode = response.statusCode()
                    val json = if (contentType?.equals("application/json", true) == true) {
                        GSON.fromJson(body, JsonElement::class.java)
                    } else {
                        null
                    }
                    LOGGER.debug("Response: $statusCode, Body: $json")

                    // Update rate limits
                    val rateLimit = headers.firstValue("X-RateLimit-Limit").orElse(null)?.toInt()
                    val rateLimitRemaining = headers.firstValue("X-RateLimit-Remaining").orElse(null)?.toInt()
                    val rateLimitEnds = headers.firstValue("X-RateLimit-Reset").orElse(null)?.toLong()

                    if (statusCode !in 200..299) { // When failed to request
                        RateLimiter.incrementRateLimitRemaining(endPoint)
                    }

                    if (rateLimit != null && rateLimitRemaining != null && rateLimitEnds != null) {
                        RateLimiter.setRateLimit(endPoint, rateLimit)
                        RateLimiter.setRateLimitRemaining(endPoint, rateLimitRemaining)
                        RateLimiter.setRateLimitEnds(endPoint, rateLimitEnds * 1000)
                        LOGGER.debug("RateLimit: $rateLimit, Remaining: $rateLimitRemaining, Ends: $rateLimitEnds")
                    }

                    when (statusCode) { // Handle rate limits (429 Too Many Requests)
                        429 -> {
                            if (json == null) { // When get rate limited without body
                                throw RateLimitedException()
                            }

                            val delay = json.asJsonObject["retry_after"].asLong

                            if (json.asJsonObject["global"].asBoolean) {
                                RateLimiter.setGlobalRateLimitEnds(delay)
                            } else {
                                RateLimiter.setRateLimitEnds(endPoint, System.currentTimeMillis() + delay)
                                RateLimiter.setRateLimitRemaining(endPoint, 0)
                            }

                            return@repeat
                        }
                        in 500..599 -> {
                            val message = "Discord API returned internal server error (code: $statusCode)"
                            throw Exception(message) // Retry
                        }
                        403 -> {
                            throw MissingPermissionsException("Request: $url, Response: $body")
                        }
                        404 -> {
                            throw NotFoundException(true)
                        }
                        !in 200..299 -> {
                            val message = "Discord API returned status code $statusCode with body ${json?.toString()}"
                            throw DiscordException(message, statusCode)
                        }
                        else -> {
                            return json ?: JsonObject()
                        }
                    }
                } catch (ex: DiscordException) {
                    throw ex
                } catch (ex: RateLimitedException) {
                    throw ex
                } catch (ex: NotFoundException) {
                    throw ex
                } catch (ex: MissingPermissionsException) {
                    throw ex
                } catch (ex: Exception) {
                    LOGGER.warn("An unexpected error has occurred! we will retry in a second", ex)
                    Thread.sleep(1000)
                }
            }

            throw RateLimitedException()
        }
    }
}