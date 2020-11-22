package com.github.syari.ss.plugins.core.config.dataType

import com.github.syari.ss.plugins.core.config.CustomConfig
import com.github.syari.ss.plugins.core.sql.Database

object ConfigDatabaseDataType: ConfigDataType<Database> {
    override val typeName = "Database(Section)"

    override fun get(
        config: CustomConfig,
        path: String,
        notFoundError: Boolean
    ): Database? {
        if (!config.contains(path)) return null
        return when (config.get("$path.type", ConfigDataType.STRING, "mysql", false)) {
            "mysql" -> {
                config.get(path, ConfigDataType.MYSQL, notFoundError)
            }
            "sqlite" -> {
                config.get(path, ConfigDataType.SQLITE, notFoundError)
            }
            else -> {
                config.nullError("$path.type", "DatabaseType(mysql, sqlite)")
                null
            }
        }
    }
}