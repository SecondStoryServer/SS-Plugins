package com.github.syari.ss.plugins.core

import com.github.syari.ss.plugins.core.bossBar.CustomBossBar
import com.github.syari.ss.plugins.core.code.SSPlugin
import com.github.syari.ss.plugins.core.inventory.CreateInventory
import com.github.syari.ss.plugins.core.time.TimeScheduler
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.plugin.java.JavaPlugin

class Main: SSPlugin() {
    companion object {
        /**
         * コアプラグインのインスタンス
         */
        internal lateinit var plugin: JavaPlugin

        /**
         * コンソール
         */
        lateinit var console: ConsoleCommandSender
    }

    override val listeners = listOf(CustomBossBar, CreateInventory, TimeScheduler)
    override val onEnables = listOf(TimeScheduler)
    override val onDisables = listOf(CustomBossBar)

    override fun onEnable() {
        plugin = this
        console = server.consoleSender
        runOnEnable()
        registerListeners()
    }

    override fun onDisable() {
        runOnDisable()
    }
}