package com.github.syari.ss.plugins.core.config.type.data

import com.github.syari.ss.plugins.core.config.CustomConfig
import com.github.syari.ss.plugins.core.config.CustomFileConfig
import com.github.syari.ss.plugins.core.config.type.ConfigDataType

object ConfigNumberDataType : ConfigDataType.WithSet<Number> {
    override val typeName = "Number"

    override fun get(
        config: CustomConfig,
        path: String,
        notFoundError: Boolean
    ): Number? {
        return config.getUnsafe(path, typeName, notFoundError)
    }

    override fun set(config: CustomFileConfig, path: String, value: Number?) {
        config.setUnsafe(path, value)
    }
}
