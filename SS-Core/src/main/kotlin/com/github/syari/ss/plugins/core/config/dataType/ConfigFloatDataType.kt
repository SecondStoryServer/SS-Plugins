package com.github.syari.ss.plugins.core.config.dataType

import com.github.syari.ss.plugins.core.config.CustomConfig

object ConfigFloatDataType: ConfigDataType<Float> {
    override val typeName = "Float"

    override fun get(
        config: CustomConfig, path: String, notFoundError: Boolean
    ): Float? {
        return config.get(path, ConfigDataType.NUMBER, notFoundError)?.toFloat()
    }
}