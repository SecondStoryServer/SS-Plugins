package com.github.syari.ss.plugins.core.config.type.data

import com.github.syari.ss.plugins.core.config.CustomConfig
import com.github.syari.ss.plugins.core.config.CustomFileConfig
import com.github.syari.ss.plugins.core.config.type.ConfigDataType

object ConfigStringDataType : ConfigDataType.WithSet<String> {
    override val typeName = "String"

    override fun get(
        config: CustomConfig,
        path: String,
        notFoundError: Boolean
    ): String? {
        return config.getUnsafe<String>(path, typeName, notFoundError)
    }

    override fun set(config: CustomFileConfig, path: String, value: String?) {
        config.setUnsafe(path, value)
    }
}
