package com.github.syari.ss.plugins.ma.chest

import com.github.syari.ss.plugins.core.code.SSPlugin

class Main: SSPlugin() {
    override val listeners = listOf(EventListener)

    override fun onEnable() {
        registerListeners()
    }
}