@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.github.syari.ss.plugins.core.code

import java.util.UUID

object UUIDConverter {
    fun String.toUUID(): UUID = UUID.fromString(this)

    fun String.toUUIDOrNull() = getUUIDOrNull(this)

    fun getUUIDOrNull(uuid: String): UUID? {
        return try {
            UUID.fromString(uuid)
        } catch (ex: IllegalArgumentException) {
            null
        }
    }
}
