package com.github.syari.ss.wplugins.core.config.dataType

import com.github.syari.ss.wplugins.core.config.CustomConfig
import java.util.UUID

object ConfigUUIDListDataType : ConfigDataType<List<UUID>> {
    override val typeName = "List<UUID>"

    override fun get(config: CustomConfig, path: String, notFoundError: Boolean): List<UUID>? {
        return config.get(path, ConfigDataType.STRINGLIST, notFoundError)?.mapNotNull {
            try {
                UUID.fromString(it)
            } catch (ex: IllegalArgumentException) {
                config.nullError(path, "UUID")
                null
            }
        }
    }
}
