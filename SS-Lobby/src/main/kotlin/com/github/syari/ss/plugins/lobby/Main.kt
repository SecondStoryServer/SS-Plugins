package com.github.syari.ss.plugins.lobby

import com.github.syari.ss.plugins.core.code.SSPlugin

class Main : SSPlugin() {
    override val events = listOf(EventListener)

    override fun onEnable() {
        registerEvents()
    }
}
