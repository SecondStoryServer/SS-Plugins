package com.github.syari.ss.plugins.mobarena

import com.github.syari.ss.plugins.core.code.SSPlugin
import com.github.syari.ss.plugins.mobarena.hook.MythicMobsRegister
import com.github.syari.ss.plugins.mobarena.shop.ShopEventListener
import org.bukkit.plugin.java.JavaPlugin

class Main : SSPlugin() {
    companion object {
        internal lateinit var plugin: JavaPlugin
    }

    init {
        plugin = this
    }

    override val events = listOf(EventListener, ShopEventListener, MythicMobsRegister)
    override val onEnables = listOf(ConfigLoader, CommandCreator)
    override val onDisables = listOf(MobArenaManager)
}
