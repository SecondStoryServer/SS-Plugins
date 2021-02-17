package com.github.syari.ss.plugins.event.acrobatsniper

import com.github.syari.spigot.api.command.command
import com.github.syari.spigot.api.command.tab.CommandTabArgument.Companion.argument
import com.github.syari.ss.plugins.core.code.OnEnable
import com.github.syari.ss.plugins.core.message.template.ConstantMessage.ReloadConfig
import com.github.syari.ss.plugins.core.message.template.templateMessage
import com.github.syari.ss.plugins.event.acrobatsniper.Main.Companion.plugin

object CommandCreator : OnEnable {
    override fun onEnable() {
        plugin.command("acrobatsniper") {
            aliases = listOf("as")
            tab {
                argument { add("reload") }
            }
            execute {
                val template = templateMessage("AcrobatSniper")
                when (args.lowerOrNull(0)) {
                    "reload" -> {
                        template.send(ReloadConfig)
                        ConfigLoader.load(sender)
                    }
                    else -> {
                        template.sendCommandHelp(
                            "$label reload" to ReloadConfig
                        )
                    }
                }
            }
        }
    }
}
