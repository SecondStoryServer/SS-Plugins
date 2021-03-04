package com.github.syari.ss.plugins.mobarena

import com.github.syari.spigot.api.command.command
import com.github.syari.spigot.api.command.tab.CommandTabArgument.Companion.argument
import com.github.syari.ss.plugins.core.code.OnEnable
import com.github.syari.ss.plugins.core.message.template.ConstantMessage.NotEnterId
import com.github.syari.ss.plugins.core.message.template.ConstantMessage.NotExistId
import com.github.syari.ss.plugins.core.message.template.ConstantMessage.OnlyPlayer
import com.github.syari.ss.plugins.core.message.template.ConstantMessage.ReloadConfig
import com.github.syari.ss.plugins.core.message.template.templateMessage
import com.github.syari.ss.plugins.mobarena.Main.Companion.plugin
import com.github.syari.ss.plugins.mobarena.MobArenaManager.arena
import com.github.syari.ss.plugins.mobarena.MobArenaManager.arenaPlayer
import com.github.syari.ss.plugins.mobarena.MobArenaManager.arenas
import com.github.syari.ss.plugins.mobarena.MobArenaManager.endAllArena
import com.github.syari.ss.plugins.mobarena.MobArenaManager.getArena
import com.github.syari.ss.plugins.mobarena.arena.MobArena
import com.github.syari.ss.plugins.mobarena.arena.MobArenaStatus
import com.github.syari.ss.plugins.mobarena.kit.MobArenaKit
import com.github.syari.ss.plugins.mobarena.shop.Shop
import org.bukkit.entity.Player

object CommandCreator : OnEnable {
    override fun onEnable() {
        plugin.command("ma") {
            tab {
                argument {
                    val player = sender as? Player
                    if (player != null) {
                        val arenaPlayer = player.arenaPlayer
                        when {
                            arenaPlayer == null -> addAll("join", "spec", "shop", "kit")
                            arenaPlayer.play -> addAll("leave", "ready", "notready", "kit")
                            else -> addAll("join", "leave")
                        }
                    }
                    addAll("start", "end", "reload")
                }
                argument("join", "j", "spec", "s", "start", "end") {
                    addAll(arenas.map(MobArena::id))
                }
                argument("shop") {
                    addAll(Shop.names)
                }
                argument("kit") {
                    val player = sender as? Player
                    val arenaPlayer = player?.arenaPlayer ?: return@argument
                    addAll(arenaPlayer.arena.kits)
                }
            }
            execute {
                val template = templateMessage("MobArena")
                when (args.lowerOrNull(0)) {
                    "join", "j" -> {
                        val player = sender as? Player ?: return@execute template.sendError(OnlyPlayer)
                        val id = args.getOrNull(1) ?: return@execute template.sendError(NotEnterId)
                        val arena = getArena(id) ?: return@execute template.sendError(NotFoundArena)
                        arena.join(player)
                    }
                    "leave", "l" -> {
                        val player = sender as? Player ?: return@execute template.sendError(OnlyPlayer)
                        val arena = player.arena ?: return@execute template.sendError(NotJoinArena)
                        arena.leave(player)
                    }
                    "spec", "s" -> {
                        val player = sender as? Player ?: return@execute template.sendError(OnlyPlayer)
                        val id = args.getOrNull(1) ?: return@execute template.sendError(NotEnterId)
                        val arena = getArena(id) ?: return@execute template.sendError(NotFoundArena)
                        arena.spec(player)
                    }
                    "shop" -> {
                        val player = sender as? Player ?: return@execute template.sendError(OnlyPlayer)
                        val id = args.getOrNull(1) ?: return@execute template.sendError(NotEnterId)
                        val shopData = Shop.get(id) ?: return@execute template.sendError(NotExistId)
                        shopData.openShop(player)
                    }
                    "kit" -> {
                        val player = sender as? Player ?: return@execute template.sendError(OnlyPlayer)
                        val arenaPlayer = player.arenaPlayer
                        if (arenaPlayer != null) {
                            if (arenaPlayer.play) {
                                val id = args.getOrNull(1) ?: return@execute template.sendError("キット名を入力してください")
                                val kit = MobArenaKit.getKit(id) ?: return@execute template.sendError("存在しないキットです")
                                if (arenaPlayer.arena.availableKit(kit).not()) return@execute template.sendError("使用不可能なキットです")
                                arenaPlayer.loadKit(kit)
                            } else {
                                template.sendError(NotJoinArena)
                            }
                        } else {
                            MobArenaKit.openPreviewList(player)
                        }
                    }
                    "ready", "r" -> {
                        val player = sender as? Player ?: return@execute template.sendError(OnlyPlayer)
                        val arenaPlayer = player.arenaPlayer ?: return@execute template.sendError(NotJoinArena)
                        if (arenaPlayer.play) {
                            if (arenaPlayer.kit == null) return@execute template.sendError("キットを選択していません")
                            if (arenaPlayer.ready) return@execute template.sendError("既に準備完了しています")
                            arenaPlayer.ready = true
                        } else {
                            template.sendError(NotJoinArena)
                        }
                    }
                    "notready", "nr" -> {
                        val player = sender as? Player ?: return@execute template.sendError(OnlyPlayer)
                        val arenaPlayer = player.arenaPlayer ?: return@execute template.sendError(NotJoinArena)
                        if (arenaPlayer.play) {
                            if (arenaPlayer.ready.not()) return@execute template.sendError("まだ準備完了していません")
                            arenaPlayer.ready = false
                        } else {
                            template.sendError(NotJoinArena)
                        }
                    }
                    "start" -> {
                        val id = args.getOrNull(1) ?: return@execute template.sendError(NotEnterId)
                        val arena = getArena(id) ?: return@execute template.sendError(NotFoundArena)
                        if (arena.status != MobArenaStatus.WaitReady) return@execute template.sendError("準備待機中のアリーナではありません")
                        arena.start()
                        template.send("&a$id&fのゲームを強制的に始めました")
                    }
                    "end" -> {
                        val id = args.getOrNull(1) ?: return@execute template.sendError(NotEnterId)
                        if (id.toLowerCase() == "all") {
                            endAllArena()
                            template.send("全てのアリーナを強制終了しました")
                        } else {
                            val arena = getArena(id) ?: return@execute template.sendError(NotFoundArena)
                            arena.end(true)
                            template.sendError("&a$id &fのゲームを強制的終了しました")
                        }
                    }
                    "reload" -> {
                        template.send(ReloadConfig)
                        ConfigLoader.load(sender)
                    }
                    else -> template.sendCommandHelp(
                        "$label join" to "モブアリーナに参加します",
                        "$label leave" to "モブアリーナから脱退します",
                        "$label spec" to "モブアリーナを観戦します",
                        "$label kit" to "キットを選択します",
                        "$label ready" to "準備完了します",
                        "$label notready" to "準備完了を取り消します",
                        "$label start" to "モブアリーナを強制で開始します",
                        "$label end" to "モブアリーナを強制で終了します",
                        "$label reload" to "コンフィグを再読み込みします"
                    )
                }
            }
        }
    }

    private const val NotFoundArena = "モブアリーナが見つかりませんでした"
    private const val NotJoinArena = "モブアリーナに参加していません"
}
