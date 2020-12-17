package com.github.syari.ss.plugins.core.config.dataType

import com.github.syari.ss.plugins.core.config.CustomConfig
import com.github.syari.ss.plugins.core.sql.SQLite

object ConfigSQLiteDataType: ConfigDataType<SQLite> {
    override val typeName = "SQLite(Section)"

    override fun get(
        config: CustomConfig, path: String, notFoundError: Boolean
    ): SQLite? {
        return SQLite.create(
            config.plugin.dataFolder, config.get("$path.name", ConfigDataType.STRING)
        )
    }
}