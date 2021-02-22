package com.github.syari.ss.plugins.event.acrobatsniper

import com.github.syari.spigot.api.command.command
import com.github.syari.spigot.api.command.tab.CommandTabArgument.Companion.argument
import com.github.syari.spigot.api.util.uuid.UUIDPlayer
import com.github.syari.ss.plugins.core.code.OnEnable
import com.github.syari.ss.plugins.core.command.getPlayer
import com.github.syari.ss.plugins.core.message.JsonBuilder
import com.github.syari.ss.plugins.core.message.JsonBuilder.Companion.buildJson
import com.github.syari.ss.plugins.core.message.template.ConstantMessage.OnlyPlayer
import com.github.syari.ss.plugins.core.message.template.ConstantMessage.ReloadConfig
import com.github.syari.ss.plugins.core.message.template.templateMessage
import com.github.syari.ss.plugins.event.acrobatsniper.Main.Companion.plugin
import org.bukkit.entity.Player

object CommandCreator : OnEnable {
    override fun onEnable() {
        plugin.command("acrobat-sniper") {
            aliases = listOf("as")
            tab {
                argument { add("entry") }
            }
            execute {
                val template = templateMessage("AcrobatSniper")
                val player = sender as? Player ?: return@execute template.sendError(OnlyPlayer)
                when (args.lowerOrNull(0)) {
                    "entry" -> {
                        if (EntryList.isEnable.not()) {
                            return@execute template.sendError("エントリー受付時間外です")
                        }
                        val uuidPlayer = UUIDPlayer.from(player)
                        if (EntryList.contains(uuidPlayer)) {
                            EntryList.remove(uuidPlayer)
                            template.send("エントリーを取り消しました")
                        } else {
                            EntryList.add(uuidPlayer)
                            template.send("エントリーしました")
                        }
                    }
                    else -> {
                        template.sendCommandHelp(
                            "$label entry" to "参加登録を行います"
                        )
                    }
                }
            }
        }
        plugin.command("acrobat-sniper-admin") {
            aliases = listOf("asa")
            tab {
                argument { add("match", "entry", "reload") }
                argument("match") { add("test") }
                argument("match test **") { addAll(plugin.server.onlinePlayers.map(Player::getName)) }
                argument("entry") { add("list", "clear", "enable", "disable") }
                argument("entry list") { add("all") }
            }
            execute {
                val template = templateMessage("AcrobatSniper")
                when (args.lowerOrNull(0)) {
                    "match" -> {
                        when (args.lowerOrNull(1)) {
                            "test" -> {
                                val player1 = args.getPlayer(2, template) ?: return@execute
                                val player2 = args.getPlayer(3, template) ?: return@execute
                                Match.start(player1, player2)
                            }
                            else -> {
                                template.sendCommandHelp(
                                    "$label match test [Player] [Player]" to "対戦のテストを行います"
                                )
                            }
                        }
                    }
                    "entry" -> {
                        when (args.lowerOrNull(1)) {
                            "list" -> {
                                val nameList = EntryList.nameList
                                template.send(
                                    buildJson {
                                        append(
                                            "エントリー一覧",
                                            JsonBuilder.Hover.Text("&6コピー"),
                                            JsonBuilder.Click.Clipboard(nameList.joinToString("\n"))
                                        )
                                        val nameListLimit = if (args.lowerOrNull(2) == "all") {
                                            -1
                                        } else {
                                            args.getOrNull(2)?.toIntOrNull() ?: 5
                                        }
                                        append(nameList.joinToString(limit = nameListLimit) { "&7$it" })
                                        append(" &a(${nameList.size})")
                                    }
                                )
                            }
                            "clear" -> {
                                EntryList.clear()
                                template.send("エントリーを削除しました")
                            }
                            "enable" -> {
                                EntryList.isEnable = true
                                template.send("エントリー受付を開始します")
                            }
                            "disable" -> {
                                EntryList.isEnable = false
                                template.send("エントリー受付を終了します")
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
