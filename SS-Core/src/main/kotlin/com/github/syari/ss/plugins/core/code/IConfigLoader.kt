package com.github.syari.ss.plugins.core.code

import com.github.syari.ss.plugins.core.console
import org.bukkit.command.CommandSender

interface IConfigLoader : OnEnable {
    override fun onEnable() {
        load(console)
    }

    fun load(sender: CommandSender)
}
