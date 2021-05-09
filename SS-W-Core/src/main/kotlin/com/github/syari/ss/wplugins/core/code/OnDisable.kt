package com.github.syari.ss.wplugins.core.code

/**
 * プラグインが無効になった時に処理を行う
 * @see onDisable
 */
interface OnDisable {
    /**
     * プラグインが無効になった時に実行される関数
     */
    fun onDisable()
}
