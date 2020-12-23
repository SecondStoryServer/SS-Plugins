package com.github.syari.ss.plugins.discord.api.exception

class NotFoundException(remote: Boolean = false): Exception("the entity no longer exists (remote: $remote)")