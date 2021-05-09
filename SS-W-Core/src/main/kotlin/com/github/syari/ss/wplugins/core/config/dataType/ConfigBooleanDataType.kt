package com.github.syari.ss.wplugins.core.config.dataType

import com.github.syari.ss.wplugins.core.config.CustomConfig

object ConfigBooleanDataType : ConfigDataType<Boolean> {
    override val typeName = "Boolean"

    override fun get(
        config: CustomConfig,
        path: String,
        notFoundError: Boolean
    ): Boolean? {
        return config.getUnsafe(path, typeName, notFoundError)
    }
}
