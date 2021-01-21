package com.github.syari.ss.plugins.core.code

import com.github.syari.ss.plugins.core.Main.Companion.console
import org.bukkit.command.CommandSender

interface IConfigLoader : OnEnable {
    override fun onEnable() {
        load(console)
    }

    fun load(sender: CommandSender)
}
