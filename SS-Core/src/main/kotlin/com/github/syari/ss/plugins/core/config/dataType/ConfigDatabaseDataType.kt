package com.github.syari.ss.plugins.core.config.dataType

import com.github.syari.ss.plugins.core.config.CustomConfig
import com.github.syari.ss.plugins.core.config.CustomFileConfig
import com.github.syari.ss.plugins.core.sql.Database
import com.github.syari.ss.plugins.core.sql.MySQL
import com.github.syari.ss.plugins.core.sql.SQLite

object ConfigDatabaseDataType: ConfigDataType.WithSet<Database> {
    override val typeName = "Database(Section)"

    override fun get(
        config: CustomConfig, path: String, notFoundError: Boolean
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

    override fun set(config: CustomFileConfig, path: String, value: Database?) {
        when (value) {
            is MySQL -> {
                config.set(path, ConfigDataType.MYSQL, value)
            }
            is SQLite -> { // config.set(path, ConfigDataType.SQLITE, value)
            }
            else -> {
                config.setUnsafe(path, null)
            }
        }
    }
}