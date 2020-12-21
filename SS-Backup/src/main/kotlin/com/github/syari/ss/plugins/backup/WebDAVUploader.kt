package com.github.syari.ss.plugins.backup

import io.ktor.client.HttpClient
import io.ktor.client.content.LocalFileContent
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.features.auth.Auth
import io.ktor.client.features.auth.providers.basic
import io.ktor.client.request.request
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpMethod
import java.io.File

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

    suspend fun upload(file: File) {
        client.request<HttpResponse>("$url/${file.name}") {
            method = HttpMethod.Put
            body = LocalFileContent(file)
        }
    }
}