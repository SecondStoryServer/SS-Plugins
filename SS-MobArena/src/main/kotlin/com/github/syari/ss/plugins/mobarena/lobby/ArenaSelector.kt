package com.github.syari.ss.plugins.mobarena.lobby

import com.github.syari.ss.plugins.core.inventory.CreateInventory.inventory
import com.github.syari.ss.plugins.core.item.itemStack
import com.github.syari.ss.plugins.mobarena.MobArenaManager
import com.github.syari.ss.plugins.mobarena.MobArenaManager.inMobArena
import com.github.syari.ss.plugins.mobarena.arena.MobArenaStatus
import org.bukkit.Material
import org.bukkit.entity.Player

object ArenaSelector : LobbyItem {
    override val item = itemStack(Material.IRON_SWORD, "&a&lアリーナに参加する")

    override fun onClick(player: Player) {
        inventory("&9&lアリーナ選択", 3) {
            MobArenaManager.arenas.forEachIndexed { index, arena ->
                val (material, color) = when (arena.status) {
                    MobArenaStatus.StandBy -> Material.LIGHT_GRAY_DYE to "&7"
                    MobArenaStatus.WaitReady -> Material.LIME_DYE to "&a"
                    MobArenaStatus.NowPlay -> Material.RED_DYE to "&c"
                }
                item(index, material, color + arena.name).event {
                    if (player.inMobArena.not()) {
                        arena.join(player)
                    }
                }
            }
        }.open(player)
    }
}
