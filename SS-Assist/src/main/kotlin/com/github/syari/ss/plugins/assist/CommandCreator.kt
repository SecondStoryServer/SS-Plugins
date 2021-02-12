package com.github.syari.ss.plugins.assist

import com.github.syari.spigot.api.command.command
import com.github.syari.spigot.api.command.tab.CommandTabArgument.Companion.argument
import com.github.syari.ss.plugins.assist.Main.Companion.plugin
import com.github.syari.ss.plugins.core.code.OnEnable
import com.github.syari.ss.plugins.core.message.Message.send
import com.github.syari.ss.plugins.core.message.template.sendTemplate
import com.github.syari.ss.plugins.core.message.template.sendTemplateList

object CommandCreator : OnEnable {
    override fun onEnable() {
        plugin.command("assist") {
            tab {
                argument { add("reload") }
            }
            execute {
                when (args.lowerOrNull(0)) {
                    "reload" -> {
                        sender.sendTemplate("Assist", "コンフィグを再読み込みします")
                        ConfigLoader.load(sender)
                        AutoCommand.load(sender)
                    }
                }
            }
        }
        plugin.command("self") {
            execute {
                sender.send(args.joinToString(" "))
            }
        }
        plugin.command("versions") {
            execute {
                val plugins = plugin.server.pluginManager.plugins
                val pluginNames = plugins.map { "${it.name} &7${it.description.version}" }.sorted()
                sender.sendTemplateList("PluginManager", "プラグイン一覧", pluginNames)
            }
        }
    }
}
