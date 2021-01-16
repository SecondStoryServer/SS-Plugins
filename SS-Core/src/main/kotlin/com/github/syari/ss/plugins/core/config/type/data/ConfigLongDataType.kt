@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.github.syari.ss.plugins.core.config.type.data

import com.github.syari.ss.plugins.core.config.CustomConfig
import com.github.syari.ss.plugins.core.config.CustomFileConfig
import com.github.syari.ss.plugins.core.config.type.ConfigDataType

object ConfigLongDataType : ConfigDataType.WithSet<Long> {
    override val typeName = "Long"

    override fun get(
        config: CustomConfig,
        path: String,
        notFoundError: Boolean
    ): Long? {
        return config.get(path, ConfigDataType.NUMBER, notFoundError)?.toLong()
    }

    override fun set(config: CustomFileConfig, path: String, value: Long?) {
        config.setUnsafe(path, value)
    }
}
