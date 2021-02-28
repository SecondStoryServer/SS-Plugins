package com.github.syari.ss.plugins.lobby.gadget

import com.github.syari.spigot.api.scheduler.runTaskTimer
import com.github.syari.spigot.api.util.uuid.UUIDPlayer
import com.github.syari.ss.plugins.core.item.itemStack
import com.github.syari.ss.plugins.lobby.Main.Companion.plugin
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitTask

object Elytra : Gadget(Material.ELYTRA, "エリトラ", "lobby.gadget.elytra") {
    private val elytra = itemStack(Material.ELYTRA, "&dエリトラ").apply {
        isUnbreakable = true
    }

    private val fireworkRocket = itemStack(Material.FIREWORK_ROCKET, "&d花火")

    private val giveFireworkTask = mutableMapOf<UUIDPlayer, BukkitTask>()

    override fun onEnable(player: Player, itemStack: ItemStack) {
        player.inventory.chestplate = elytra
        player.inventory.setItemInOffHand(fireworkRocket)
        giveFireworkTask[UUIDPlayer.from(player)] = plugin.runTaskTimer(20) {
            player.inventory.setItemInOffHand(fireworkRocket)
        }
        super.onEnable(player, itemStack)
    }

    override fun onDisable(player: Player, itemStack: ItemStack) {
        player.inventory.chestplate = null
        player.inventory.setItemInOffHand(null)
        giveFireworkTask.remove(UUIDPlayer.from(player))?.cancel()
        super.onDisable(player, itemStack)
    }
}
