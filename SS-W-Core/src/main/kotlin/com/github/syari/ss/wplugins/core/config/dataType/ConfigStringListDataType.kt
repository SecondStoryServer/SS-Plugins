package com.github.syari.ss.wplugins.core.config.dataType

import com.github.syari.ss.wplugins.core.config.CustomConfig

object ConfigStringListDataType : ConfigDataType<List<String>> {
    override val typeName = "List<String>"

    override fun get(
        config: CustomConfig,
        path: String,
        notFoundError: Boolean
    ): List<String>? {
        return config.getListUnsafe(path, typeName, notFoundError)
    }
}
