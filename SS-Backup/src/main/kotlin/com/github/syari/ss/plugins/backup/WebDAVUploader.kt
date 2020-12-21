package com.github.syari.ss.plugins.backup

import com.github.syari.ss.plugins.backup.Main.Companion.plugin
import io.ktor.client.HttpClient
import io.ktor.client.content.LocalFileContent
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.features.auth.Auth
import io.ktor.client.features.auth.providers.basic
import io.ktor.client.request.request
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpMethod
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.net.SocketException

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

    private val client = HttpClient(OkHttp) {
        install(Auth) {
            basic {
                username = user
                password = pass
            }
        }
        engine {
            config {
                retryOnConnectionFailure(true)
            }
        }
    }

    fun upload(file: File) {
        GlobalScope.launch {
            repeat(10) {
                delay(100)
                try {
                    client.request<HttpResponse>("$url/${file.name}") {
                        method = HttpMethod.Put
                        body = LocalFileContent(file)
                    }
                    plugin.logger.info("${file.path} をアップロードしました")
                    file.delete()
                    return@launch
                } catch (ex: SocketException) {

                }
            }
            plugin.logger.severe("${file.path} のアップロードに失敗しました")
        }
    }
}