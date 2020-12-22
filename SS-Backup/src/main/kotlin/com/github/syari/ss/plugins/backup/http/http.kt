package com.github.syari.ss.plugins.backup.http

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest

inline fun buildHttpClient(action: HttpClient.Builder.() -> Unit): HttpClient = HttpClient.newBuilder().apply(action).build()
inline fun buildHttpRequest(url: String, action: HttpRequest.Builder.() -> Unit): HttpRequest = HttpRequest.newBuilder(URI(url)).apply(action).build()