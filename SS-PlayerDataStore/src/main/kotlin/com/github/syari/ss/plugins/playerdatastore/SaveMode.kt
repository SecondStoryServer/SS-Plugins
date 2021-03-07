package com.github.syari.ss.plugins.playerdatastore

import com.github.syari.spigot.api.config.CustomConfig
import com.github.syari.spigot.api.uuid.UUIDPlayer
import com.github.syari.spigot.api.config.type.ConfigDataType as IConfigDataType

enum class SaveMode(val condition: (UUIDPlayer) -> Boolean) {
    Everyone({ true }),
    OnlyAdmin({ it.offlinePlayer.isOp }),
    Disable({ false });

    companion object {
        fun get(name: String): SaveMode? {
            return values().firstOrNull { it.name.equals(name, true) }
        }
    }

    object ConfigDataType : IConfigDataType<SaveMode> {
        override val typeName = "SaveMode"

        override fun get(
            config: CustomConfig,
            path: String,
            notFoundError: Boolean
        ): SaveMode? {
            return config.get(path, IConfigDataType.String, notFoundError)?.let(Companion::get)
        }

        override fun set(config: CustomConfig, path: String, value: SaveMode?) {
            config.set(path, IConfigDataType.String, value?.name)
        }
    }
}
