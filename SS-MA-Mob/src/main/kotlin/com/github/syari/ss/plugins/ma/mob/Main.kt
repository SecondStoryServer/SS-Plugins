package com.github.syari.ss.plugins.ma.mob

import com.github.syari.ss.plugins.core.code.SSPlugin

class Main: SSPlugin() {
    override val listeners = listOf(MythicMobsListener)

    override fun onEnable() {
        registerListeners()
    }
}