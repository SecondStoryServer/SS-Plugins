package com.github.syari.ss.plugins.mobarena

import com.github.syari.ss.plugins.core.code.OnEnable
import com.github.syari.ss.plugins.core.command.create.CommandCreator.Companion.command
import com.github.syari.ss.plugins.core.command.create.CommandTabElement.Companion.element
import com.github.syari.ss.plugins.core.command.create.ErrorMessage
import com.github.syari.ss.plugins.core.message.Message.send
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
        plugin.command("ma", "MobArena") {
            tab {
                arg {
                    val player = sender as? Player
                    if (player != null) {
                        val arenaPlayer = player.arenaPlayer
                        when {
                            arenaPlayer == null -> element("join", "spec", "shop", "kit")
                            arenaPlayer.play -> element("leave", "ready", "notready", "kit")
                            else -> element("join", "leave")
                        }
                    } else {
                        element()
                    }.join("start", "end", "reload")
                }
                arg("join", "j", "spec", "s", "start", "end") {
                    element(arenas.map(MobArena::id))
                }
                arg("kit") {
                    val player = sender as? Player ?: return@arg null
                    element(player.arenaPlayer?.arena?.kits)
                }
            }
            execute {
                when (args.whenIndex(0)) {
                    "join", "j" -> {
                        val player = sender as? Player ?: return@execute sendError(ErrorMessage.OnlyPlayer)
                        val id = args.getOrNull(1) ?: return@execute sendError("モブアリーナを入力してください")
                        val arena = getArena(id) ?: return@execute sendError("モブアリーナが見つかりませんでした")
                        arena.join(player)
                    }
                    "leave", "l" -> {
                        val player = sender as? Player ?: return@execute sendError(ErrorMessage.OnlyPlayer)
                        val arena = player.arena ?: return@execute sendError("モブアリーナに入っていません")
                        arena.leave(player)
                    }
                    "spec", "s" -> {
                        val player = sender as? Player ?: return@execute sendError(ErrorMessage.OnlyPlayer)
                        val id = args.getOrNull(1) ?: return@execute sendError("モブアリーナを入力してください")
                        val arena = getArena(id) ?: return@execute sendError("モブアリーナが見つかりませんでした")
                        arena.spec(player)
                    }
                    "shop" -> {
                        val player = sender as? Player ?: return@execute sendError(ErrorMessage.OnlyPlayer)
                        val id = args.getOrNull(1) ?: return@execute sendError(ErrorMessage.NotEnterId)
                        val shopData = Shop.get(id) ?: return@execute sendError(ErrorMessage.NotExistId)
                        shopData.open(player)
                    }
                    "kit" -> {
                        val player = sender as? Player ?: return@execute sendError(ErrorMessage.OnlyPlayer)
                        val arenaPlayer = player.arenaPlayer
                        if (arenaPlayer != null) {
                            if (arenaPlayer.play) {
                                val id = args.getOrNull(1) ?: return@execute sendError("キット名を入力してください")
                                val kit = MobArenaKit.getKit(id) ?: return@execute sendError("存在しないキットです")
                                if (arenaPlayer.arena.availableKit(kit).not()) return@execute sendError("使用不可能なキットです")
                                arenaPlayer.loadKit(kit)
                            } else {
                                sendError("モブアリーナに参加していません")
                            }
                        } else {
                            MobArenaKit.openPreviewList(player)
                        }
                    }
                    "ready", "r" -> {
                        val player = sender as? Player ?: return@execute sendError(ErrorMessage.OnlyPlayer)
                        val arenaPlayer = player.arenaPlayer ?: return@execute sendError("モブアリーナに入っていません")
                        if (arenaPlayer.play) {
                            if (arenaPlayer.kit == null) return@execute sendError("キットを選択していません")
                            if (arenaPlayer.ready) return@execute sendError("既に準備完了しています")
                            arenaPlayer.ready = true
                        } else {
                            sendError("モブアリーナに参加していません")
                        }
                    }
                    "notready", "nr" -> {
                        val player = sender as? Player ?: return@execute sendError(ErrorMessage.OnlyPlayer)
                        val arenaPlayer = player.arenaPlayer ?: return@execute sendError("モブアリーナに入っていません")
                        if (arenaPlayer.play) {
                            if (arenaPlayer.ready.not()) return@execute sendError("まだ準備完了していません")
                            arenaPlayer.ready = false
                        } else {
                            sendError("モブアリーナに参加していません")
                        }
                    }
                    "start" -> {
                        val id = args.getOrNull(1) ?: return@execute sendError("アリーナIDを入力してください")
                        val arena = getArena(id) ?: return@execute sendError("モブアリーナが見つかりませんでした")
                        if (arena.status != MobArenaStatus.WaitReady) return@execute sendError("準備待機中のアリーナではありません")
                        arena.start()
                        sender.send("&b[MobArena] &a$id&fのゲームを強制的に始めました")
                    }
                    "end" -> {
                        val id = args.getOrNull(1) ?: return@execute sendError("アリーナIDを入力してください")
                        if (id.toLowerCase() == "all") {
                            endAllArena()
                            sendWithPrefix("全てのアリーナを強制終了しました")
                        } else {
                            val arena = getArena(id) ?: return@execute sendError("モブアリーナが見つかりませんでした")
                            arena.end(true)
                            sendError("&a$id &fのゲームを強制的終了しました")
                        }
                    }
                    "reload" -> {
                        sendWithPrefix("コンフィグを読み込みます")
                        ConfigLoader.load(sender)
                    }
                    else -> sendHelp(
                        "ma join" to "モブアリーナに参加します", //
                        "ma leave" to "モブアリーナから脱退します", //
                        "ma spec" to "モブアリーナを観戦します", //
                        "ma kit" to "キットを選択します", //
                        "ma ready" to "準備完了します", //
                        "ma notready" to "準備完了を取り消します", //
                        "ma start" to "モブアリーナを強制で開始します", //
                        "ma end" to "モブアリーナを強制で終了します", //
                        "ma reload" to "コンフィグを再読み込みします"
                    )
                }
            }
        }
    }
}
