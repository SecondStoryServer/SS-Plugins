package com.github.syari.ss.plugins.lobby.gadget

import com.github.syari.spigot.api.util.uuid.UUIDPlayer
import com.github.syari.ss.plugins.core.item.CustomItemStack
import com.github.syari.ss.plugins.core.scheduler.CreateScheduler.runTimer
import com.github.syari.ss.plugins.core.scheduler.CustomTask
import com.github.syari.ss.plugins.lobby.Main.Companion.plugin
import org.bukkit.Material
import org.bukkit.entity.Player

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

    private val giveFireworkTask = mutableMapOf<UUIDPlayer, CustomTask>()

    override fun onEnable(player: Player, itemStack: CustomItemStack) {
        player.inventory.chestplate = elytra
        player.inventory.setItemInOffHand(fireworkRocket)
        plugin.runTimer(0, 20) {
            player.inventory.setItemInOffHand(fireworkRocket)
        }?.let {
            giveFireworkTask[UUIDPlayer.from(player)] = it
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
