package com.github.syari.ss.plugins.ma.kit

import com.github.syari.ss.plugins.core.code.OnEnable
import com.github.syari.ss.plugins.core.command.create.CreateCommand.createCommand
import com.github.syari.ss.plugins.core.command.create.ErrorMessage
import com.github.syari.ss.plugins.core.inventory.CreateInventory.inventory
import com.github.syari.ss.plugins.ma.kit.Main.Companion.plugin
import org.bukkit.entity.Player
import su.nightexpress.ama.AMA
import su.nightexpress.ama.api.ArenaAPI
import su.nightexpress.ama.manager.kits.ArenaKit

object CommandCreator: OnEnable {
    override fun onEnable() {
        createCommand(plugin, "ma-kit", "MA-Kit") { sender, args ->
            when (args.whenIndex(0)) {
                "preview" -> {
                    if (sender !is Player) return@createCommand sendError(ErrorMessage.OnlyPlayer)
                    val id = args.getOrNull(1) ?: return@createCommand sendError("キットを入力してください")
                    val kit = ArenaAPI.getKitManager().getKitById(id) ?: return@createCommand sendError("キットが見つかりませんでした")
                    kit.openPreview(sender)
                }
                "select" -> {
                    if (sender !is Player) return@createCommand sendError(ErrorMessage.OnlyPlayer)
                    val id = args.getOrNull(1) ?: return@createCommand sendError("キットを入力してください")
                    val kit = ArenaAPI.getKitManager().getKitById(id) ?: return@createCommand sendError("キットが見つかりませんでした")
                    val arenaPlayer = ArenaAPI.getArenaManager().getArenaPlayer(sender) ?: return@createCommand sendError("アリーナに入っていません")
                    if (kit.isAvailable(arenaPlayer, false)) {
                        arenaPlayer.kit = kit
                    } else {
                        val availableBuy = kit.checkRequirements(sender)
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