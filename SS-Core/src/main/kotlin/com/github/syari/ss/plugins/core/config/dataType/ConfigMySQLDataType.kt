package com.github.syari.ss.plugins.core.config.dataType

import com.github.syari.ss.plugins.core.config.CustomConfig
import com.github.syari.ss.plugins.core.config.CustomFileConfig
import com.github.syari.ss.plugins.core.sql.MySQL

object ConfigMySQLDataType: ConfigDataType.WithSet<MySQL> {
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

    override fun set(config: CustomFileConfig, path: String, value: MySQL?) {
        if (value != null) {
            config.set("$path.host", ConfigDataType.STRING, value.host)
            config.set("$path.port", ConfigDataType.INT, value.port)
            config.set("$path.database", ConfigDataType.STRING, value.database)
            config.set("$path.user", ConfigDataType.STRING, value.user)
            config.set("$path.password", ConfigDataType.STRING, value.password)
        } else {
            config.setUnsafe(path, null)
        }
    }
}