package com.github.syari.ss.plugins.assist

import com.github.syari.ss.plugins.assist.Main.Companion.plugin
import com.github.syari.ss.plugins.core.code.OnEnable
import com.github.syari.ss.plugins.core.command.create.CommandCreator.Companion.command
import com.github.syari.ss.plugins.core.command.create.CommandTabElement.Companion.element
import com.github.syari.ss.plugins.core.message.Message.send

object CommandCreator : OnEnable {
    override fun onEnable() {
        plugin.command("assist", "Assist") {
            tab {
                arg { element("reload") }
            }
            execute {
                when (args.whenIndex(0)) {
                    "reload" -> {
                        sendWithPrefix("コンフィグを再読み込みします")
                        ConfigLoader.load(sender)
                        AutoCommand.load(sender)
                    }
                }
            }
        }
        plugin.command("self", "") {
            execute {
                sender.send(args.joinToString(" "))
            }
        }
        plugin.command("versions", "PluginManager") {
            execute {
                val plugins = plugin.server.pluginManager.plugins
                sendList("&fプラグイン一覧", plugins.map { "${it.name} &7${it.description.version}" }.sorted())
            }
        }
    }
}
