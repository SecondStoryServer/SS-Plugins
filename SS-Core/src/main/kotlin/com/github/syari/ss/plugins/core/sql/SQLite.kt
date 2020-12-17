package com.github.syari.ss.plugins.core.sql

import java.io.File
import java.sql.Connection
import java.sql.DriverManager

/**
 * データベース接続クラス SQLite
 */
data class SQLite(val file: File): Database {
    companion object {
        fun create(
            parentDirectory: File?, fileName: String?
        ): SQLite? {
            return if (fileName != null) {
                val dbFileName = if (fileName.endsWith(".db")) fileName else "$fileName.db"
                val file = if (parentDirectory != null) {
                    File(parentDirectory, dbFileName)
                } else {
                    File(dbFileName)
                }
                if (!file.exists()) {
                    file.createNewFile()
                }
                SQLite(file)
            } else {
                null
            }
        }

        fun create(path: String?): SQLite? {
            return create(null, path)
        }
    }

    /**
     * 接続します
     * @return [Connection]
     */
    override fun getConnection(): Connection {
        return DriverManager.getConnection("jdbc:sqlite:${file.path}")
    }
}