package com.github.syari.ss.plugins.event.acrobatsniper

import com.github.syari.ss.plugins.core.code.SSPlugin

class Main : SSPlugin() {
    override val events = listOf(DoubleJump)

    override fun onEnable() {
        registerEvents()
    }
}
