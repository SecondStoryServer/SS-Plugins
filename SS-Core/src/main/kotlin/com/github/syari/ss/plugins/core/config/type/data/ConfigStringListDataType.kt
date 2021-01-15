package com.github.syari.ss.plugins.core.config.type.data

import com.github.syari.ss.plugins.core.config.CustomConfig
import com.github.syari.ss.plugins.core.config.CustomFileConfig
import com.github.syari.ss.plugins.core.config.type.ConfigDataType

object ConfigStringListDataType : ConfigDataType.WithSet<List<String>> {
    override val typeName = "List<String>"

    override fun get(
        config: CustomConfig,
        path: String,
        notFoundError: Boolean
    ): List<String>? {
        return config.getListUnsafe(path, typeName, notFoundError)
    }

    override fun set(config: CustomFileConfig, path: String, value: List<String>?) {
        config.setUnsafe(path, value)
    }
}
