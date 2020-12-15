package com.github.syari.ss.plugins.commandblocker

import com.github.syari.ss.plugins.core.code.SSPlugin
import org.bukkit.plugin.java.JavaPlugin

class Main: SSPlugin() {
    companion object {
        internal lateinit var plugin: JavaPlugin
    }

    override val listeners = listOf(EventListener)
    override val onEnables = listOf(EventListener)

    override fun onEnable() {
        plugin = this
        registerListeners()
        runOnEnable()
    }
}