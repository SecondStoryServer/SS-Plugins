package com.github.syari.ss.plugins.globalplayers

import com.github.syari.ss.plugins.core.code.SSPlugin
import org.bukkit.plugin.java.JavaPlugin

class Main: SSPlugin() {
    companion object {
        internal lateinit var plugin: JavaPlugin
    }

    override val listeners = listOf(TabUpdater)

    override fun onEnable() {
        plugin = this
        registerListeners()
    }
}