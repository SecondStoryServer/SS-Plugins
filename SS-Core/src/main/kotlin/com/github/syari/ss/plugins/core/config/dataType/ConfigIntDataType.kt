package com.github.syari.ss.plugins.core.config.dataType

import com.github.syari.ss.plugins.core.config.CustomConfig
import com.github.syari.ss.plugins.core.config.CustomFileConfig

object ConfigIntDataType: ConfigDataType.WithSet<Int> {
    override val typeName = "Int"

    override fun get(
        config: CustomConfig, path: String, notFoundError: Boolean
    ): Int? {
        return config.get(path, ConfigDataType.NUMBER, notFoundError)?.toInt()
    }

    override fun set(config: CustomFileConfig, path: String, value: Int?) {
        config.setUnsafe(path, value)
    }
}