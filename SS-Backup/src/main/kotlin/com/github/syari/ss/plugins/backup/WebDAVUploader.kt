package com.github.syari.ss.plugins.backup

import com.github.syari.ss.plugins.backup.Main.Companion.plugin
import com.github.syari.ss.plugins.backup.http.buildHttpClient
import com.github.syari.ss.plugins.backup.http.buildHttpRequest
import com.github.syari.ss.plugins.core.scheduler.CreateScheduler.runSchedule
import java.io.File
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration
import java.util.Base64

class WebDAVUploader(private val url: String, user: String, pass: String) {
    companion object {
        var uploader: WebDAVUploader? = null

        fun from(url: String?, user: String?, pass: String?): WebDAVUploader? {
            return if (url != null && user != null && pass != null) {
                WebDAVUploader(url, user, pass)
            } else {
                null
            }
        }
    }

    private val client = buildHttpClient {
        followRedirects(HttpClient.Redirect.NORMAL)
        connectTimeout(Duration.ofSeconds(10))
    }

    private val authBase64 = Base64.getEncoder().encodeToString("$user:$pass".toByteArray())

    fun upload(file: File) {
        plugin.runSchedule(async = true) {
            val response: HttpResponse<String> = client.send(
                buildHttpRequest("$url/${file.name}") {
                    header("Authorization", "Basic $authBase64")
                    PUT(HttpRequest.BodyPublishers.ofFile(file.toPath()))
                },
                HttpResponse.BodyHandlers.ofString()
            )
            val statusCode = response.statusCode()
            if (statusCode in 200 until 300) {
                plugin.logger.info("${file.path} をアップロードしました")
                file.delete()
            } else {
                plugin.logger.warning("${file.path} のアップロードに失敗しました ($statusCode)")
            }
        }
    }
}
