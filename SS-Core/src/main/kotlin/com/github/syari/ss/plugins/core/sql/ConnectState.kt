package com.github.syari.ss.plugins.core.sql

import java.sql.Statement

/**
 * データベース接続結果
 * @param message 日本語メッセージ
 */
enum class ConnectState(val message: String) {
    /**
     * 成功
     */
    Success("成功しました"),

    /**
     * 失敗
     */
    CatchException("失敗しました"),

    /**
     * 設定不足
     */
    NullError("必要な設定が足りていません");

    val isSuccess get() = this == Success

    companion object {
        /**
         * Boolean を ConnectState に変換します
         * @param bool canConnect(), use() の結果
         * @return [ConnectState]
         */
        fun get(bool: Boolean?): ConnectState {
            return when (bool) {
                true -> Success
                false -> CatchException
                null -> NullError
            }
        }

        /**
         * データベースに接続できるか確認します
         * @param action データベースに対して実行する処理
         * @return [ConnectState]
         */
        fun Database?.checkConnect(action: Statement.() -> Unit = {}): ConnectState {
            return get(this?.canConnect(action))
        }
    }
}
