package com.github.syari.ss.plugins.core.config.dataType

import com.github.syari.ss.plugins.core.config.CustomConfig

object ConfigLongDataType: ConfigDataType<Long> {
    override val typeName = "Long"

    override fun get(
        config: CustomConfig,
        path: String,
        notFoundError: Boolean
    ): Long? {
        return config.get(path, ConfigDataType.NUMBER, notFoundError)?.toLong()
    }
}