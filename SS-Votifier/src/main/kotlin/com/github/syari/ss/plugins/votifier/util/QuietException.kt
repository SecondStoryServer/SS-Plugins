package com.github.syari.ss.plugins.votifier.util

class QuietException(message: String): Exception(message) {
    @Synchronized
    override fun fillInStackTrace(): Throwable? {
        return null
    }
}