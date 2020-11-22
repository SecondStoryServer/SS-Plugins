package com.github.syari.ss.plugins.core.sql

import java.sql.Connection
import java.sql.DriverManager

/**
 * データベース接続クラス MySQL
 */
data class MySQL(
    val host: String,
    val port: Int,
    val database: String,
    val user: String,
    val password: String
):
        Database {
    companion object {
        fun create(
            host: String?,
            port: Int?,
            database: String?,
            user: String?,
            password: String?
        ): MySQL? {
            return if (host != null && port != null && database != null && user != null && password != null) {
                MySQL(host, port, database, user, password)
            } else {
                null
            }
        }
    }

    /**
     * 接続します
     * @return [Connection]
     */
    override fun getConnection(): Connection {
        return DriverManager.getConnection("jdbc:mysql://$host:$port/$database", user, password)
    }
}