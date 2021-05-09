package com.github.syari.ss.wplugins.votifier.util

class QuietException(message: String) : Exception(message) {
    @Synchronized
    override fun fillInStackTrace(): Throwable? {
        return null
    }
}
