package com.github.syari.ss.wplugins.accessblocker

import com.github.syari.ss.wplugins.core.code.SSPlugin
import net.md_5.bungee.api.plugin.Plugin

class Main : SSPlugin() {
    companion object {
        internal lateinit var plugin: Plugin
    }

    override val listeners = listOf(ModBlocker, WhiteList, BrandBlocker)
    override val onEnables = listOf(CommandCreator, ConfigLoader)

    override fun onEnable() {
        plugin = this
        runOnEnable()
        registerListeners()
    }
}
