package com.github.syari.ss.plugins.ma.kit

import com.github.syari.ss.plugins.core.code.SSPlugin
import org.bukkit.plugin.java.JavaPlugin

class Main: SSPlugin() {
    companion object {
        internal lateinit var plugin: JavaPlugin
    }

    override val onEnables = listOf(CommandCreator)

    override fun onEnable() {
        plugin = this
        runOnEnable()
    }
}