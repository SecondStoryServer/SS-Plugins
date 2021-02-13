package com.github.syari.ss.plugins.demonkill

import com.github.syari.ss.plugins.core.code.SSPlugin
import com.github.syari.ss.plugins.demonkill.craft.ConfigLoader
import com.github.syari.ss.plugins.demonkill.craft.EventListener
import org.bukkit.plugin.java.JavaPlugin

class Main : SSPlugin() {
    companion object {
        internal lateinit var plugin: JavaPlugin
    }

    init {
        plugin = this
    }

    override val events = listOf(EventListener)
    override val onEnables = listOf(CommandCreator, ConfigLoader)

    override fun onEnable() {
        registerEvents()
        runOnEnable()
    }
}
