package com.github.syari.ss.wplugins.core.config.dataType

import com.github.syari.ss.wplugins.core.config.CustomConfig

object ConfigStringDataType : ConfigDataType<String> {
    override val typeName = "String"

    override fun get(
        config: CustomConfig,
        path: String,
        notFoundError: Boolean
    ): String? {
        return config.getUnsafe<String>(path, typeName, notFoundError)
    }
}
