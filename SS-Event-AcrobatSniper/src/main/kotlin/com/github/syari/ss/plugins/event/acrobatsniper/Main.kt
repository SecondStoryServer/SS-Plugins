package com.github.syari.ss.plugins.event.acrobatsniper

import com.github.syari.ss.plugins.core.code.SSPlugin
import org.bukkit.plugin.java.JavaPlugin

class Main : SSPlugin() {
    companion object {
        internal lateinit var plugin: JavaPlugin
    }

    init {
        plugin = this
    }

    override val events = listOf(EventListener, DoubleJump, Match.EventListener)
    override val onEnables = listOf(CommandCreator, ConfigLoader)

    override fun onEnable() {
        registerEvents()
        runOnEnable()
    }
}
