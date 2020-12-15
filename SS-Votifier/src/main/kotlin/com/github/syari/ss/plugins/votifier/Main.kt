package com.github.syari.ss.plugins.votifier

import com.github.syari.ss.plugins.core.code.SSPlugin
import org.bukkit.plugin.java.JavaPlugin

class Main: SSPlugin() {
    companion object {
        internal lateinit var plugin: JavaPlugin
    }

    override val onEnables = listOf(
        CommandCreator, BootstrapBuilder
    )

    override val onDisables = listOf(
        BootstrapBuilder
    )

    override fun onEnable() {
        plugin = this
        runOnEnable()
    }

    override fun onDisable() {
        runOnDisable()
    }
}