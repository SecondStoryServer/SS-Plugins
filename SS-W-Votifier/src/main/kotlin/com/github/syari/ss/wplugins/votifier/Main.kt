package com.github.syari.ss.wplugins.votifier

import com.github.syari.ss.wplugins.core.code.SSPlugin
import net.md_5.bungee.api.plugin.Plugin

class Main : SSPlugin() {
    companion object {
        internal lateinit var plugin: Plugin
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
