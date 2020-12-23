package com.github.syari.ss.plugins.discord.api.util

import java.net.http.HttpClient
import java.net.http.HttpRequest

internal inline fun buildHttpClient(action: HttpClient.Builder.() -> Unit): HttpClient = HttpClient.newBuilder().apply(action).build()
internal inline fun buildHttpRequest(action: HttpRequest.Builder.() -> Unit): HttpRequest = HttpRequest.newBuilder().apply(action).build()