package com.github.syari.ss.plugins.lobby.gadget

import com.github.syari.spigot.api.scheduler.runTaskTimer
import com.github.syari.spigot.api.util.uuid.UUIDPlayer
import com.github.syari.ss.plugins.core.item.CustomItemStack
import com.github.syari.ss.plugins.lobby.Main.Companion.plugin
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitTask

object Elytra : Gadget(Material.ELYTRA, "エリトラ", "lobby.gadget.elytra") {
    private val elytra = CustomItemStack.create(
        Material.ELYTRA,
        "&dエリトラ"
    ).apply {
        unbreakable = true
    }.toOneItemStack

    private val fireworkRocket = CustomItemStack.create(
        Material.FIREWORK_ROCKET,
        "&d花火"
    ).toOneItemStack

    private val giveFireworkTask = mutableMapOf<UUIDPlayer, BukkitTask>()

    override fun onEnable(player: Player, itemStack: CustomItemStack) {
        player.inventory.chestplate = elytra
        player.inventory.setItemInOffHand(fireworkRocket)
        giveFireworkTask[UUIDPlayer.from(player)] = plugin.runTaskTimer(20) {
            player.inventory.setItemInOffHand(fireworkRocket)
        }
        super.onEnable(player, itemStack)
    }

    override fun onDisable(player: Player, itemStack: CustomItemStack) {
        player.inventory.chestplate = null
        player.inventory.setItemInOffHand(null)
        giveFireworkTask.remove(UUIDPlayer.from(player))?.cancel()
        super.onDisable(player, itemStack)
    }
}
