package com.github.syari.ss.plugins.discord.api.util

import java.net.http.HttpClient
import java.net.http.HttpRequest

inline fun buildHttpClient(action: HttpClient.Builder.() -> Unit): HttpClient = HttpClient.newBuilder().apply(action).build()
inline fun buildHttpRequest(action: HttpRequest.Builder.() -> Unit): HttpRequest = HttpRequest.newBuilder().apply(action).build()