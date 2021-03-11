package com.github.syari.ss.plugins.mobarena

import com.github.syari.ss.plugins.core.code.SSPlugin
import com.github.syari.ss.plugins.mobarena.arena.ArenaEventListener
import com.github.syari.ss.plugins.mobarena.hook.MythicMobsRegister
import org.bukkit.plugin.java.JavaPlugin

class Main : SSPlugin() {
    companion object {
        internal lateinit var plugin: JavaPlugin
    }

    init {
        plugin = this
    }

    override val events = listOf(EventListener, ArenaEventListener, MythicMobsRegister)
    override val onEnables = listOf(ConfigLoader, CommandCreator)
    override val onDisables = listOf(MobArenaManager)
}
