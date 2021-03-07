package com.github.syari.ss.plugins.lobby.gadget

import com.github.syari.spigot.api.event.EventRegister
import com.github.syari.spigot.api.scheduler.runTaskLater
import com.github.syari.spigot.api.uuid.UUIDPlayer
import com.github.syari.ss.plugins.core.item.itemStack
import org.bukkit.Effect
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerToggleFlightEvent
import org.bukkit.inventory.ItemStack

object DoubleJump : Gadget(Material.LEATHER_BOOTS, "ダブルジャンプ", "lobby.gadget.doublejump") {
    private val leatherBoots = itemStack(Material.LEATHER_BOOTS, "&dダブルジャンプの靴")

    override fun onEnable(player: Player, itemStack: ItemStack) {
        player.inventory.boots = leatherBoots
        super.onEnable(player, itemStack)
    }

    override fun onDisable(player: Player, itemStack: ItemStack) {
        player.inventory.boots = null
        super.onDisable(player, itemStack)
    }

    object EventListener : EventRegister {
        private val allowFlightPlayers = mutableSetOf<UUIDPlayer>()

        private inline val Player.isEquipBoots: Boolean
            get() = inventory.boots?.itemMeta?.displayName == leatherBoots.itemMeta?.displayName

        private inline val Player.isAdventure
            get() = gameMode == GameMode.ADVENTURE

        private inline val Player.availableDoubleJump: Boolean
            get() = isAdventure && isEquipBoots

        override fun com.github.syari.spigot.api.event.Events.register() {
            event<PlayerJoinEvent> {
                val player = it.player
                val uuidPlayer = UUIDPlayer.from(player)
                if (allowFlightPlayers.contains(uuidPlayer)) {
                    allowFlightPlayers.remove(uuidPlayer)
                    player.allowFlight = false
                    player.isFlying = false
                }
            }
            event<PlayerToggleFlightEvent> {
                val player = it.player
                val uuidPlayer = UUIDPlayer.from(player)
                if (player.isAdventure.not()) {
                    if (player.gameMode != GameMode.CREATIVE) {
                        player.isFlying = false
                        player.allowFlight = false
                    }
                    return@event
                }
                it.isCancelled = true
                player.allowFlight = false
                player.isFlying = false
                player.velocity = player.location.direction.multiply(1.0).setY(1)
                allowFlightPlayers.add(uuidPlayer)
                val location = player.location
                location.world.playEffect(location, Effect.SMOKE, 5)
                location.world.playSound(location, Sound.ENTITY_ENDER_DRAGON_SHOOT, 2.0f, 0.0f)
                plugin.runTaskLater(20) {
                    if (player.isFlying) {
                        player.allowFlight = false
                        allowFlightPlayers.remove(uuidPlayer)
                    }
                }
            }
            event<PlayerMoveEvent> {
                val player = it.player
                val location = player.location
                val underBlock = location.subtract(0.0, 1.0, 0.0).block
                if (player.availableDoubleJump && underBlock.type != Material.AIR) {
                    player.allowFlight = true
                }
            }
        }
    }
}
