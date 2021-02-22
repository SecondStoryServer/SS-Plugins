package com.github.syari.ss.plugins.lobby

import com.github.syari.spigot.api.command.command
import com.github.syari.spigot.api.command.tab.CommandTabArgument.Companion.argument
import com.github.syari.ss.plugins.core.code.OnEnable
import com.github.syari.ss.plugins.core.message.template.ConstantMessage.ReloadConfig
import com.github.syari.ss.plugins.core.message.template.templateMessage
import com.github.syari.ss.plugins.lobby.Main.Companion.plugin

object CommandCreator : OnEnable {
    override fun onEnable() {
        plugin.command("lobby-admin") {
            tab {
                argument { add("reload") }
            }
            execute {
                val template = templateMessage("Lobby")
                when (args.lowerOrNull(0)) {
                    "reload" -> {
                        template.send(ReloadConfig)
                        ConfigLoader.load(sender)
                    }
                    else -> {
                        template.sendCommandHelp(
                            "/$label reload" to ReloadConfig
                        )
                    }
                }
            }
        }
    }
}
