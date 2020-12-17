package com.github.syari.ss.plugins.core.config.dataType

import com.github.syari.ss.plugins.core.config.CustomConfig
import com.github.syari.ss.plugins.core.sql.MySQL

object ConfigMySQLDataType: ConfigDataType<MySQL> {
    override val typeName = "MySQL(Section)"

    override fun get(
        config: CustomConfig, path: String, notFoundError: Boolean
    ): MySQL? {
        return MySQL.create(
            config.get("$path.host", ConfigDataType.STRING, notFoundError),
            config.get("$path.port", ConfigDataType.INT, notFoundError),
            config.get("$path.database", ConfigDataType.STRING, notFoundError),
            config.get("$path.user", ConfigDataType.STRING, notFoundError),
            config.get("$path.password", ConfigDataType.STRING, notFoundError)
        )
    }
}