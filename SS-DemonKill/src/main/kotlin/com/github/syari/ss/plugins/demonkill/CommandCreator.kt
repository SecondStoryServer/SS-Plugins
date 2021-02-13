package com.github.syari.ss.plugins.demonkill

import com.github.syari.spigot.api.command.command
import com.github.syari.spigot.api.command.tab.CommandTabArgument.Companion.argument
import com.github.syari.ss.plugins.core.code.OnEnable
import com.github.syari.ss.plugins.core.message.template.ConstantMessage.ReloadConfig
import com.github.syari.ss.plugins.core.message.template.templateMessage
import com.github.syari.ss.plugins.demonkill.Main.Companion.plugin
import com.github.syari.ss.plugins.demonkill.craft.ConfigLoader

object CommandCreator : OnEnable {
    override fun onEnable() {
        plugin.command("demonkill-admin") {
            aliases = listOf("dka")

            tab {
                argument { add("reload") }
            }

            execute {
                val template = templateMessage("DemonKill")
                when (args.lowerOrNull(0)) {
                    "reload" -> {
                        template.send(ReloadConfig)
                        ConfigLoader.load(sender)
                    }
                    else -> template.sendCommandHelp(
                        "$label reload" to ReloadConfig
                    )
                }
            }
        }
    }
}
