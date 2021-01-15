package com.github.syari.ss.plugins.mobarena

import com.github.syari.ss.plugins.core.code.SSPlugin
import org.bukkit.plugin.java.JavaPlugin

class Main : SSPlugin() {
    companion object {
        internal lateinit var plugin: JavaPlugin
    }

    override val listeners = listOf(EventListener)
    override val onEnables = listOf(ConfigLoader, CommandCreator)
    override val onDisables = listOf(MobArenaManager)

    override fun onEnable() {
        plugin = this
        runOnEnable()
        registerListeners()
    }

    override fun onDisable() {
        runOnDisable()
    }
}
