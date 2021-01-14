package com.github.syari.ss.plugins.core.sql

import java.sql.Connection
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement

/**
 * データベース接続クラス
 */
interface Database {
    /**
     * 接続します
     * @return [Connection]
     */
    fun getConnection(): Connection

    /**
     * 接続テストを行います
     * @param action データベースに対して実行する処理
     * @return [Boolean]
     */
    fun canConnect(action: Statement.() -> Unit = {}): Boolean {
        return use(action) != null
    }

    /**
     * データベースを使用します
     * @param R 任意の戻り値型
     * @param action データベースに対して実行する処理
     * @return [R]?
     */
    fun <R> use(action: Statement.() -> R): R? {
        return try {
            getConnection().use { connection ->
                connection.createStatement().use(action)
            }
        } catch (ex: SQLException) {
            null
        }
    }

    companion object {
        /**
         * 値が存在していれば 処理を実行し、していなければ null を返します
         * @param R 任意のデータ型
         * @param action 値が存在していた時に実行する処理
         * @return [R]?
         */
        inline fun <R> ResultSet.nextOrNull(action: ResultSet.() -> R): R? {
            return if (next()) {
                action()
            } else {
                null
            }
        }

        /**
         * 全ての値を [Set] に変換します
         * @param R 任意のデータ型
         * @param action それぞれの値に対する処理
         * @return [List]<[R]>
         */
        @OptIn(ExperimentalStdlibApi::class)
        inline fun <R> ResultSet.asSet(action: ResultSet.() -> R?): Set<R?> {
            return buildSet {
                while (next()) {
                    add(action())
                }
            }
        }

        /**
         * 全ての値を [Set] に変換します
         * @param R 任意のデータ型
         * @param action それぞれの値に対する処理
         * @return [List]<[R]>
         */
        @OptIn(ExperimentalStdlibApi::class)
        inline fun <R> ResultSet.asSetNotNull(action: ResultSet.() -> R?): Set<R> {
            return buildSet {
                while (next()) {
                    action()?.let(::add)
                }
            }
        }

        /**
         * 全ての値を [List] に変換します
         * @param R 任意のデータ型
         * @param action それぞれの値に対する処理
         * @return [List]<[R]>
         */
        @OptIn(ExperimentalStdlibApi::class)
        inline fun <R> ResultSet.asList(action: ResultSet.() -> R?): List<R?> {
            return buildList {
                while (next()) {
                    add(action())
                }
            }
        }

        /**
         * 全ての値を [List] に変換します
         * @param R 任意のデータ型
         * @param action それぞれの値に対する処理
         * @return [List]<[R]>
         */
        @OptIn(ExperimentalStdlibApi::class)
        inline fun <R> ResultSet.asListNotNull(action: ResultSet.() -> R?): List<R> {
            return buildList {
                while (next()) {
                    action()?.let(::add)
                }
            }
        }

        /**
         * 全ての値を [Map] に変換します
         * @param K マップのキーの型
         * @param V マップの値の型
         * @param action それぞれの値に対する処理
         * @return [Map]<[K], [V]>
         */
        @OptIn(ExperimentalStdlibApi::class)
        inline fun <K, V> ResultSet.asMap(action: ResultSet.() -> Pair<K, V?>?): Map<K, V?> {
            return buildMap {
                while (next()) {
                    action()?.let { (key, value) ->
                        this[key] = value
                    }
                }
            }
        }

        /**
         * 全ての値を [Map] に変換します
         * @param K マップのキーの型
         * @param V マップの値の型
         * @param action それぞれの値に対する処理
         * @return [Map]<[K], [V]>
         */
        @OptIn(ExperimentalStdlibApi::class)
        inline fun <K, V> ResultSet.asMapNotNull(action: ResultSet.() -> Pair<K, V?>?): Map<K, V> {
            return buildMap {
                while (next()) {
                    action()?.let { (key, value) ->
                        if (value != null) {
                            this[key] = value
                        }
                    }
                }
            }
        }
    }
}