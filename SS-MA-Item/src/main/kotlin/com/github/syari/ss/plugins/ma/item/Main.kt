package com.github.syari.ss.plugins.ma.item

import com.github.syari.ss.plugins.core.code.SSPlugin

class Main: SSPlugin() {
    override val listeners = listOf(StoreItemCanceler)

    override fun onEnable() {
        registerListeners()
    }
}