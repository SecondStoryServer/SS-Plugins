package com.github.syari.ss.wplugins.globalplayers

import com.github.syari.ss.wplugins.core.code.SSPlugin
import net.md_5.bungee.api.plugin.Plugin

class Main : SSPlugin() {
    companion object {
        internal lateinit var plugin: Plugin
    }

    override val listeners = listOf(TabUpdater, GlobalJoinMessage)

    override fun onEnable() {
        plugin = this
        registerListeners()
    }
}
