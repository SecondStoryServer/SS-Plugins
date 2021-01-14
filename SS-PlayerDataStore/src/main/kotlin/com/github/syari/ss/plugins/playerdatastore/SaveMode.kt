package com.github.syari.ss.plugins.playerdatastore

import com.github.syari.ss.plugins.core.config.CustomConfig
import com.github.syari.ss.plugins.core.config.CustomFileConfig
import org.bukkit.entity.Player
import com.github.syari.ss.plugins.core.config.dataType.ConfigDataType as IConfigDataType

enum class SaveMode(val condition: (Player) -> Boolean) {
    Everyone({ true }),
    OnlyAdmin(Player::isOp),
    Disable({ false });

    companion object {
        fun get(name: String): SaveMode? {
            return values().firstOrNull { it.name.equals(name, true) }
        }
    }

    object ConfigDataType : IConfigDataType.WithSet<SaveMode> {
        override val typeName = "SaveMode"

        override fun get(
            config: CustomConfig,
            path: String,
            notFoundError: Boolean
        ): SaveMode? {
            return config.get(path, IConfigDataType.STRING, notFoundError)?.let(Companion::get)
        }

        override fun set(config: CustomFileConfig, path: String, value: SaveMode?) {
            config.set(path, IConfigDataType.STRING, value?.name)
        }
    }
}
