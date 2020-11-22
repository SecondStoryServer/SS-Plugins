package com.github.syari.ss.plugins.core.config.dataType

import com.github.syari.ss.plugins.core.config.CustomConfig
import java.util.Date

object ConfigDateDataType: ConfigDataType<Date> {
    override val typeName = "Date"

    override fun get(
        config: CustomConfig,
        path: String,
        notFoundError: Boolean
    ): Date? {
        return config.getUnsafe(path, typeName, notFoundError)
    }
}