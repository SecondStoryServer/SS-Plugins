package com.github.syari.ss.plugins.core.config.dataType

import com.github.syari.ss.plugins.core.config.CustomConfig
import com.github.syari.ss.plugins.core.config.CustomFileConfig

object ConfigBooleanDataType : ConfigDataType.WithSet<Boolean> {
    override val typeName = "Boolean"

    override fun get(
        config: CustomConfig,
        path: String,
        notFoundError: Boolean
    ): Boolean? {
        return config.getUnsafe(path, typeName, notFoundError)
    }

    override fun set(config: CustomFileConfig, path: String, value: Boolean?) {
        config.setUnsafe(path, value)
    }
}
