package com.github.syari.ss.plugins.core.config.dataType

import com.github.syari.ss.plugins.core.config.CustomConfig

object ConfigIntDataType: ConfigDataType<Int> {
    override val typeName = "Int"

    override fun get(
        config: CustomConfig, path: String, notFoundError: Boolean
    ): Int? {
        return config.get(path, ConfigDataType.NUMBER, notFoundError)?.toInt()
    }
}