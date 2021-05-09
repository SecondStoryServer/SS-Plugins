package com.github.syari.ss.wplugins.discord.api.exception

class DiscordException(override val message: String?, val code: Int? = null) : Exception()
