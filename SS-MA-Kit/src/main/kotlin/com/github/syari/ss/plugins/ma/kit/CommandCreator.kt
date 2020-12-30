package com.github.syari.ss.plugins.ma.kit

import com.github.syari.ss.plugins.core.code.OnEnable
import com.github.syari.ss.plugins.core.command.create.CommandCreator.Companion.command
import com.github.syari.ss.plugins.core.command.create.ErrorMessage
import com.github.syari.ss.plugins.core.inventory.CreateInventory.inventory
import com.github.syari.ss.plugins.ma.kit.Main.Companion.plugin
import org.bukkit.entity.Player
import su.nightexpress.ama.AMA
import su.nightexpress.ama.api.ArenaAPI
import su.nightexpress.ama.manager.kits.ArenaKit

object CommandCreator: OnEnable {
    override fun onEnable() {
        plugin.command("ma-kit", "MA-Kit") {
            execute {
                val player = sender as? Player ?: return@execute sendError(ErrorMessage.OnlyPlayer)
                when (args.whenIndex(0)) {
                    "preview" -> {
                        val id = args.getOrNull(1) ?: return@execute sendError("キットを入力してください")
                        val kit = ArenaAPI.getKitManager().getKitById(id) ?: return@execute sendError("キットが見つかりませんでした")
                        kit.openPreview(player)
                    }
                    "select" -> {
                        val id = args.getOrNull(1) ?: return@execute sendError("キットを入力してください")
                        val kit = ArenaAPI.getKitManager().getKitById(id) ?: return@execute sendError("キットが見つかりませんでした")
                        val arenaPlayer = ArenaAPI.getArenaManager().getArenaPlayer(player) ?: return@execute sendError("アリーナに入っていません")
                        if (kit.isAvailable(arenaPlayer, false)) {
                            arenaPlayer.kit = kit
                        } else {
                            val availableBuy = kit.checkRequirements(player)
                            inventory("&9&l購入 - ${kit.id}", 1) {
                                item(4, kit.icon.type, if (availableBuy) "&a&l購入する" else "&c&l購入できません", "&6Coin ${kit.requirementMoney}").apply {
                                    if (availableBuy) {
                                        kit.buy(arenaPlayer)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun ArenaKit.checkRequirements(player: Player): Boolean {
        val user = AMA.getInstance().userManager.getOrLoadUser(player)
        val cost = if (player.hasPermission("advancedmobarena.bypass.kit.requirement.money")) 0 else requirementMoney
        return if (hasPermission(player) && cost < ArenaAPI.getEconomy().getBalance(player)) {
            if (ArenaAPI.getKitManager().isAccountKits && requirementKits.isNotEmpty()) {
                return requirementKits.all { user.hasKit(it) }
            }
            true
        } else {
            false
        }
    }
}