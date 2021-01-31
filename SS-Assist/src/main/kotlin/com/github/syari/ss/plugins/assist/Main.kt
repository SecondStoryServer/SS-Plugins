package com.github.syari.ss.plugins.assist

import com.github.syari.ss.plugins.core.code.SSPlugin
import org.bukkit.plugin.java.JavaPlugin

class Main : SSPlugin() {
    companion object {
        internal lateinit var plugin: JavaPlugin
    }

    override val events = listOf(SpawnOverride)
    override val onEnables = listOf(AutoCommand, CommandCreator, ConfigLoader)

    override fun onEnable() {
        plugin = this
        runOnEnable()
        registerEvents()
    }
}
