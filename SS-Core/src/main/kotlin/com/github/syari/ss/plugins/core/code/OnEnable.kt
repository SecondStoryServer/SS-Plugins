package com.github.syari.ss.plugins.core.code

/**
 * プラグインが有効になった時に処理を行う
 */
interface OnEnable {
    /**
     * プラグインが有効になった時に実行される関数
     */
    fun onEnable()
}