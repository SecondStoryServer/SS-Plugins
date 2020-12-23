package com.github.syari.ss.plugins.discord.api.websocket

import com.github.syari.ss.plugins.discord.api.ConnectStatus
import com.github.syari.ss.plugins.discord.api.KtDiscord
import com.github.syari.ss.plugins.discord.api.KtDiscord.API_VERSION
import com.github.syari.ss.plugins.discord.api.KtDiscord.LOGGER
import com.github.syari.ss.plugins.discord.api.KtDiscord.gatewayIntents
import com.github.syari.ss.plugins.discord.api.KtDiscord.maxShards
import com.github.syari.ss.plugins.discord.api.KtDiscord.shard
import com.github.syari.ss.plugins.discord.api.KtDiscord.token
import com.github.syari.ss.plugins.discord.api.handle.EventHandler.handleEvent
import com.github.syari.ss.plugins.discord.api.rest.RestClient.client
import com.github.syari.ss.plugins.discord.api.util.ByteArrayUtil.concat
import com.github.syari.ss.plugins.discord.api.util.ByteArrayUtil.takeLastAsByteArray
import com.github.syari.ss.plugins.discord.api.util.json.JsonUtil.getObjectOrNull
import com.github.syari.ss.plugins.discord.api.util.json.JsonUtil.getOrNull
import com.github.syari.ss.plugins.discord.api.util.json.JsonUtil.json
import com.github.syari.ss.plugins.discord.api.util.json.JsonUtil.jsonArray
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.URI
import java.net.http.WebSocket
import java.nio.ByteBuffer
import java.util.Timer
import java.util.TimerTask
import java.util.concurrent.CompletionStage
import java.util.zip.Inflater
import java.util.zip.InflaterOutputStream
import kotlin.concurrent.timerTask

internal object GatewayClient {
    private val GSON = Gson()

    @Volatile
    private var sessionId: String? = null

    @Volatile
    private var ready: Boolean = false

    @Volatile
    private var heartbeatTask: TimerTask? = null

    @Volatile
    private var heartbeatAckReceived: Boolean = false

    @Volatile
    private var lastSequence: Int? = null

    private lateinit var url: String

    fun connect(url: String) {
        GatewayClient.url = url
        connect()
    }

    private fun connect() {
        val url = URI.create("$url/?v=$API_VERSION&encoding=json&compress=zlib-stream")
        client.newWebSocketBuilder().buildAsync(url, Listener)
    }

    private fun authenticate(webSocket: WebSocket) {
        send(webSocket, Opcode.IDENTIFY, json {
            "token" to token

            if (gatewayIntents.isNotEmpty()) {
                var value = 0
                gatewayIntents.forEach {
                    value = value or it.flag
                }
                LOGGER.trace("Intent flag: $value")
                "intents" to value
            }

            "properties" to json {
                "\$os" to "who knows"
                "\$browser" to "who knows"
                "\$device" to "who knows"
            }

            "shard" to jsonArray {
                +JsonPrimitive(shard)
                +JsonPrimitive(maxShards)
            }
        })
    }

    private fun resume(webSocket: WebSocket) {
        val sessionId = sessionId ?: throw IllegalArgumentException("sessionId is null")
        send(webSocket, Opcode.RESUME, json {
            "session_id" to sessionId
            "token" to token
            "seq" to lastSequence
        })
    }

    private fun handleMessage(webSocket: WebSocket, text: String) {
        val payloads = GSON.fromJson(text, JsonObject::class.java)
        val opcode = Opcode.getByCode(payloads["op"].asInt)
        val data = payloads.getObjectOrNull("d")
        LOGGER.trace("Receive: $payloads")

        when (opcode) {
            Opcode.HELLO -> {
                LOGGER.debug("Starting heartbeat task")

                val period = data!!["heartbeat_interval"].asLong
                heartbeatAckReceived = true

                heartbeatTask?.cancel()
                Timer().schedule(timerTask {
                    if (heartbeatAckReceived) {
                        heartbeatAckReceived = false
                        send(webSocket, Opcode.HEARTBEAT, json { lastSequence?.let { "d" to it } })
                    } else {
                        webSocket.sendClose(4000, "Heartbeat ACK wasn't received")
                    }
                }.apply {
                    heartbeatTask = this
                }, 0, period)
            }
            Opcode.RECONNECT -> {
                LOGGER.info("Received RECONNECT opcode. Attempting to reconnect.")
                webSocket.sendClose(4001, "Received Reconnect Request")
            }
            Opcode.INVALID_SESSION -> {
                LOGGER.info("Received INVALID_SESSION opcode. Attempting to reconnect.")
                webSocket.sendClose(4990, "Invalid Session")
            }
            Opcode.HEARTBEAT_ACK -> {
                LOGGER.debug("Received heartbeat ACK")
                heartbeatAckReceived = true
            }
            Opcode.DISPATCH -> {
                val eventType = payloads["t"].asString
                lastSequence = payloads["s"].asString.toInt()

                if (eventType == "READY" || eventType == "RESUMED") {
                    ready = true

                    data?.getOrNull("session_id")?.let {
                        sessionId = it.asString
                    }

                    LOGGER.info("Ready")
                }

                handleEvent(eventType, data!!)
            }
            else -> {
                LOGGER.warn("Received a packet with unknown opcode: $data")
            }
        }
    }

    private fun send(webSocket: WebSocket, opCode: Opcode, data: JsonObject) {
        val json = json {
            "op" to opCode.code
            "d" to data
        }.toString()

        LOGGER.trace("Sent: $json")
        webSocket.sendText(json, true)
    }

    object Listener: WebSocket.Listener {
        override fun onOpen(webSocket: WebSocket) {
            LOGGER.info("Connected to the gateway")
            KtDiscord.status = ConnectStatus.CONNECTED

            if (sessionId == null) {
                LOGGER.info("Authenticating...")
                authenticate(webSocket)
            } else {
                LOGGER.info("Resuming the session...")
                resume(webSocket)
            }

            webSocket.request(1)
        }

        private val buffer = mutableListOf<ByteArray>()
        private val inflater = Inflater()

        private fun hex(s: String): ByteArray {
            val result = ByteArray(s.length / 2)
            for (idx in result.indices) {
                val srcIdx = idx * 2
                val high = s[srcIdx].toString().toInt(16) shl 4
                val low = s[srcIdx + 1].toString().toInt(16)
                result[idx] = (high or low).toByte()
            }

            return result
        }

        private val ZLIB_SUFFIX = hex("0000ffff")

        override fun onBinary(webSocket: WebSocket, data: ByteBuffer, last: Boolean): CompletionStage<*>? {
            val byteArray = ByteArray(data.capacity())
            data.get(byteArray)

            // Add the received data to buffer
            buffer.add(byteArray)

            // Check for zlib suffix
            if (byteArray.size < 4 || !byteArray.takeLastAsByteArray(4).contentEquals(ZLIB_SUFFIX)) {
                return null
            }

            // Decompress the buffered data
            val text = ByteArrayOutputStream().use { output ->
                try {
                    InflaterOutputStream(output, inflater).use {
                        it.write(buffer.concat())
                    }
                    output.toString("UTF-8")
                } catch (e: IOException) {
                    LOGGER.error("Error while decompressing payload", e)
                    return null
                } finally {
                    buffer.clear()
                }
            }

            // Handle the message
            handleMessage(webSocket, text)
            webSocket.request(1)
            return null
        }

        override fun onClose(webSocket: WebSocket, statusCode: Int, reason: String): CompletionStage<*>? {
            LOGGER.info("WebSocket closed with code: $statusCode, reason: '$reason'")

            ready = false
            heartbeatTask?.cancel()

            if (statusCode == 4014) {
                LOGGER.error("Invalid privilege intent(s) are specified. you must first go to your application in the Developer Portal and enable the toggle for the Privileged Intents you wish to use.")
                return null
            }

            // Invalidate cache
            if (statusCode == 4007 || statusCode == 4990 || statusCode == 4003) {
                sessionId = null
                lastSequence = null
            }

            connect()
            return null
        }

        override fun onError(webSocket: WebSocket, error: Throwable) {
            LOGGER.error("WebSocket Error", error)
        }
    }
}