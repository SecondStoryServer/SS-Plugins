package com.github.syari.ss.plugins.discord.api.exception

class DiscordException(override val message: String?, val code: Int? = null): Exception()