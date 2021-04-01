package com.github.syari.ss.plugins.mobarena.lobby

import com.github.syari.ss.plugins.core.inventory.CreateInventory.inventory
import com.github.syari.ss.plugins.core.item.itemStack
import com.github.syari.ss.plugins.mobarena.MobArenaManager
import com.github.syari.ss.plugins.mobarena.MobArenaManager.inMobArena
import com.github.syari.ss.plugins.mobarena.arena.MobArenaStatus
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType

object ArenaSelector : LobbyItem {
    override val item = itemStack(Material.IRON_SWORD, "&a&lアリーナに参加する")

    override fun onClick(player: Player) {
        inventory("&9&lアリーナ選択", 3) {
            MobArenaManager.arenas.forEachIndexed { index, arena ->
                val (material, color) = when (arena.status) {
                    MobArenaStatus.StandBy -> Material.GRAY_DYE to "&7"
                    MobArenaStatus.WaitReady -> Material.LIME_DYE to "&a"
                    MobArenaStatus.NowPlay -> Material.PINK_DYE to "&c"
                }
                item(index, material, color + arena.name, "&6&l◀︎ &a左クリックで参加", "&6&l▶︎ &a右クリックで観戦")
                    .event(ClickType.LEFT) {
                        if (player.inMobArena.not()) {
                            arena.join(player)
                        }
                    }
                    .event(ClickType.RIGHT) {
                        if (player.inMobArena.not()) {
                            arena.spec(player)
                        }
                    }
            }
        }.open(player)
    }
}
