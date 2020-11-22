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
     * @param run データベースに対して実行する処理
     * @return [Boolean]
     */
    fun canConnect(run: Statement.() -> Unit = {}): Boolean {
        return use(run) != null
    }

    /**
     * データベースを使用します
     * @param R 任意の戻り値型
     * @param run データベースに対して実行する処理
     * @return [R]?
     */
    fun <R> use(run: Statement.() -> R): R? {
        return try {
            getConnection().use { connection ->
                connection.createStatement().use(run)
            }
        } catch (ex: SQLException) {
            null
        }
    }

    companion object {
        /**
         * 値が存在していれば 処理を実行し、していなければ null を返します
         * @param R 任意のデータ型
         * @param run 値が存在していた時に実行する処理
         * @return [R]?
         */
        inline fun <R> ResultSet.nextOrNull(run: ResultSet.() -> R): R? {
            return if (next()) {
                run.invoke(this)
            } else {
                null
            }
        }

        /**
         * 全ての値を [Set] に変換します
         * @param R 任意のデータ型
         * @param run それぞれの値に対する処理
         * @return [List]<[R]>
         */
        inline fun <R> ResultSet.asSet(run: ResultSet.() -> R?): Set<R?> {
            return mutableSetOf<R?>().also { list ->
                while (next()) {
                    list.add(run.invoke(this))
                }
            }
        }

        /**
         * 全ての値を [Set] に変換します
         * @param R 任意のデータ型
         * @param run それぞれの値に対する処理
         * @return [List]<[R]>
         */
        inline fun <R> ResultSet.asSetNotNull(run: ResultSet.() -> R?): Set<R> {
            return mutableSetOf<R>().also { list ->
                while (next()) {
                    run.invoke(this)?.let { list.add(it) }
                }
            }
        }

        /**
         * 全ての値を [List] に変換します
         * @param R 任意のデータ型
         * @param run それぞれの値に対する処理
         * @return [List]<[R]>
         */
        inline fun <R> ResultSet.asList(run: ResultSet.() -> R?): List<R?> {
            return mutableListOf<R?>().also { list ->
                while (next()) {
                    list.add(run.invoke(this))
                }
            }
        }

        /**
         * 全ての値を [List] に変換します
         * @param R 任意のデータ型
         * @param run それぞれの値に対する処理
         * @return [List]<[R]>
         */
        inline fun <R> ResultSet.asListNotNull(run: ResultSet.() -> R?): List<R> {
            return mutableListOf<R>().also { list ->
                while (next()) {
                    run.invoke(this)?.let { list.add(it) }
                }
            }
        }

        /**
         * 全ての値を [Map] に変換します
         * @param K マップのキーの型
         * @param V マップの値の型
         * @param run それぞれの値に対する処理
         * @return [Map]<[K], [V]>
         */
        inline fun <K, V> ResultSet.asMap(run: ResultSet.() -> Pair<K, V?>?): Map<K, V?> {
            return mutableMapOf<K, V?>().also { map ->
                while (next()) {
                    run.invoke(this)?.let { (key, value) ->
                        map[key] = value
                    }
                }
            }
        }

        /**
         * 全ての値を [Map] に変換します
         * @param K マップのキーの型
         * @param V マップの値の型
         * @param run それぞれの値に対する処理
         * @return [Map]<[K], [V]>
         */
        inline fun <K, V> ResultSet.asMapNotNull(run: ResultSet.() -> Pair<K, V?>?): Map<K, V> {
            return mutableMapOf<K, V>().also { map ->
                while (next()) {
                    run.invoke(this)?.let { (key, value) ->
                        if (value != null) {
                            map[key] = value
                        }
                    }
                }
            }
        }
    }
}