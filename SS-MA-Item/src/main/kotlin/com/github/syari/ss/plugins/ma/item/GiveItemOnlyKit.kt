package com.github.syari.ss.plugins.ma.item

import com.github.syari.ss.plugins.core.code.OnEnable
import com.github.syari.ss.plugins.core.command.create.CommandCreator.Companion.command
import com.github.syari.ss.plugins.core.command.create.CommandTabElement.Companion.element
import com.github.syari.ss.plugins.core.item.CustomItemStack
import com.github.syari.ss.plugins.core.item.ItemStackPlus.give
import com.github.syari.ss.plugins.ma.item.Main.Companion.plugin
import org.bukkit.Material
import su.nightexpress.ama.api.ArenaAPI
import su.nightexpress.ama.manager.kits.ArenaKit

object GiveItemOnlyKit: OnEnable {
    override fun onEnable() {
        plugin.command("ma-give", "GiveItemOnlyKit") {
            tab {
                arg { element(ArenaAPI.getArenaManager().players.map { it.player.name }) }
                arg("*") { element(ArenaAPI.getKitManager().kits.map(ArenaKit::getName)) }
                arg("* *") { element(Material.values().map(Material::name)) }
            }
            execute {
                val player = args.getPlayer(0, true) ?: return@execute
                val kitId = args.getOrNull(1) ?: return@execute sendError("キット名を入力してください")
                val typeName = args.getOrNull(2) ?: return@execute sendError("アイテムを入力してください")
                val arenaPlayer = ArenaAPI.getArenaManager().getArenaPlayer(player) ?: return@execute sendError("アリーナにいないプレイヤーです")
                val kit = ArenaAPI.getKitManager().getKitById(kitId) ?: return@execute sendError("存在しないキットです")
                if (arenaPlayer.kit == kit) {
                    val type = Material.getMaterial(typeName.toUpperCase()) ?: return@execute sendError("存在しないアイテムです")
                    val amount = args.getOrNull(3)?.toIntOrNull() ?: 1
                    player.give(CustomItemStack.create(type, amount))
                }
            }
        }
    }
}