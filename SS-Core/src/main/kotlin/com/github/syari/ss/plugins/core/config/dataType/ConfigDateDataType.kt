package com.github.syari.ss.plugins.core.config.dataType

import com.github.syari.ss.plugins.core.config.CustomConfig
import com.github.syari.ss.plugins.core.config.CustomFileConfig
import java.util.Date

object ConfigDateDataType: ConfigDataType.WithSet<Date> {
    override val typeName = "Date"

    override fun get(
        config: CustomConfig, path: String, notFoundError: Boolean
    ): Date? {
        return config.getUnsafe(path, typeName, notFoundError)
    }

    override fun set(config: CustomFileConfig, path: String, value: Date?) {
        config.setUnsafe(path, value)
    }
}