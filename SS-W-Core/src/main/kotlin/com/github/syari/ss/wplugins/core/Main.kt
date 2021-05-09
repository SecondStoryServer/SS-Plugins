package com.github.syari.ss.wplugins.core

import com.github.syari.ss.wplugins.core.code.SSPlugin
import com.github.syari.ss.wplugins.core.pluginMessage.PluginMessage
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.plugin.Plugin

class Main : SSPlugin() {
    companion object {
        internal lateinit var plugin: Plugin

        /**
         * コンソール
         */
        lateinit var console: CommandSender
    }

    override val listeners = listOf(
        PluginMessage
    )

    override val onEnables = listOf(
        PluginMessage
    )

    override fun onEnable() {
        plugin = this
        console = proxy.console
        runOnEnable()
        registerListeners()
    }
}
