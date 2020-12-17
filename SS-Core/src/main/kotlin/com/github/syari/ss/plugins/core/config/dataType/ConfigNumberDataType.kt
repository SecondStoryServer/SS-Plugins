package com.github.syari.ss.plugins.core.config.dataType

import com.github.syari.ss.plugins.core.config.CustomConfig

object ConfigNumberDataType: ConfigDataType<Number> {
    override val typeName = "Number"

    override fun get(
        config: CustomConfig, path: String, notFoundError: Boolean
    ): Number? {
        return config.getUnsafe(path, typeName, notFoundError)
    }
}