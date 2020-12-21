package com.github.syari.ss.plugins.ma.item

import com.github.syari.ss.plugins.core.code.OnEnable
import com.github.syari.ss.plugins.core.command.create.CreateCommand.command
import com.github.syari.ss.plugins.core.command.create.CreateCommand.element
import com.github.syari.ss.plugins.core.command.create.CreateCommand.tab
import com.github.syari.ss.plugins.core.item.CustomItemStack
import com.github.syari.ss.plugins.core.item.ItemStackPlus.give
import com.github.syari.ss.plugins.ma.item.Main.Companion.plugin
import org.bukkit.Material
import su.nightexpress.ama.api.ArenaAPI
import su.nightexpress.ama.manager.kits.ArenaKit

object GiveItemOnlyKit: OnEnable {
    override fun onEnable() {
        command(plugin,
                "ma-give",
                "GiveItemOnlyKit",
                tab { element(ArenaAPI.getArenaManager().players.map { it.player.name }) },
                tab("*") { element(ArenaAPI.getKitManager().kits.map(ArenaKit::getName)) },
                tab("* *") { element(Material.values().map(Material::name)) }) { _, args ->
            val player = args.getPlayer(0, true) ?: return@command
            val kitId = args.getOrNull(1) ?: return@command sendError("キット名を入力してください")
            val typeName = args.getOrNull(2) ?: return@command sendError("アイテムを入力してください")
            val arenaPlayer = ArenaAPI.getArenaManager().getArenaPlayer(player) ?: return@command sendError("アリーナにいないプレイヤーです")
            val kit = ArenaAPI.getKitManager().getKitById(kitId) ?: return@command sendError("存在しないキットです")
            if (arenaPlayer.kit == kit) {
                val type = Material.getMaterial(typeName.toUpperCase()) ?: return@command sendError("存在しないアイテムです")
                val amount = args.getOrNull(3)?.toIntOrNull() ?: 1
                player.give(CustomItemStack.create(type, amount))
            }
        }
    }
}