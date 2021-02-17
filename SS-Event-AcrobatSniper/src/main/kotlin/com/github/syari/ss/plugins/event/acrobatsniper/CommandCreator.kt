package com.github.syari.ss.plugins.event.acrobatsniper

import com.github.syari.spigot.api.command.command
import com.github.syari.spigot.api.command.tab.CommandTabArgument.Companion.argument
import com.github.syari.ss.plugins.core.code.OnEnable
import com.github.syari.ss.plugins.core.command.getPlayer
import com.github.syari.ss.plugins.core.message.template.ConstantMessage.ReloadConfig
import com.github.syari.ss.plugins.core.message.template.templateMessage
import com.github.syari.ss.plugins.event.acrobatsniper.Main.Companion.plugin
import org.bukkit.entity.Player

object CommandCreator : OnEnable {
    override fun onEnable() {
        plugin.command("acrobatsniper") {
            aliases = listOf("as")
            tab {
                argument { add("match", "reload") }
                argument("match") { add("start") }
                argument("match start", "match start *") { addAll(plugin.server.onlinePlayers.map(Player::getName)) }
            }
            execute {
                val template = templateMessage("AcrobatSniper")
                when (args.lowerOrNull(0)) {
                    "match" -> {
                        when (args.lowerOrNull(1)) {
                            "start" -> { // TODO テスト用処理
                                val player1 = args.getPlayer(2, template) ?: return@execute
                                val player2 = args.getPlayer(3, template) ?: return@execute
                                Match(player1, player2).start()
                            }
                            else -> {
                            }
                        }
                    }
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
