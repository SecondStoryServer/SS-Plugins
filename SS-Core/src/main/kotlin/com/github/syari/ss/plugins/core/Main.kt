@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.github.syari.ss.plugins.core

import com.github.syari.spigot.api.inventory.CustomInventory
import com.github.syari.ss.plugins.core.bossBar.CustomBossBar
import com.github.syari.ss.plugins.core.code.SSPlugin
import com.github.syari.ss.plugins.core.pluginMessage.PluginMessage
import com.github.syari.ss.plugins.core.time.TimeScheduler
import org.bukkit.plugin.java.JavaPlugin

class Main : SSPlugin() {
    companion object {
        internal lateinit var plugin: JavaPlugin
    }

    init {
        plugin = this
    }

    override val onEnables = listOf(TimeScheduler, PluginMessage, CustomBossBar)
    override val onDisables = listOf(CustomBossBar)

    override fun onEnable() {
        super.onEnable()
        CustomInventory.onEnable(this)
    }
}
