package com.github.syari.ss.plugins.globalplayers

import com.github.syari.spigot.api.event.events
import com.github.syari.spigot.api.scheduler.runTaskLater
import com.github.syari.spigot.api.string.toColor
import com.github.syari.ss.plugins.core.code.OnEnable
import com.github.syari.ss.plugins.core.pluginMessage.SSPluginMessageEvent
import com.github.syari.ss.plugins.globalplayers.Main.Companion.plugin
import com.github.syari.ss.template.message.PluginMessageTemplateTabList
import com.mojang.authlib.GameProfile
import net.minecraft.server.v1_16_R3.EntityPlayer
import net.minecraft.server.v1_16_R3.PacketPlayOutPlayerInfo
import net.minecraft.server.v1_16_R3.PacketPlayOutPlayerInfo.EnumPlayerInfoAction
import net.minecraft.server.v1_16_R3.PlayerInteractManager
import org.bukkit.craftbukkit.v1_16_R3.CraftServer
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer
import org.bukkit.craftbukkit.v1_16_R3.util.CraftChatMessage

object TabUpdater : OnEnable {
    private var lastPlayerList = listOf<String>()

    override fun onEnable() {
        plugin.events {
            event<SSPluginMessageEvent> {
                val template = it.template as? PluginMessageTemplateTabList ?: return@event
                val playerList = template.playerNameList
                updateTabPlayers(EnumPlayerInfoAction.ADD_PLAYER, playerList)
                updateTabPlayers(EnumPlayerInfoAction.REMOVE_PLAYER, lastPlayerList.filterNot { playerList.contains(it) })
                lastPlayerList = playerList
            }
        }
    }

    private fun updateTabPlayers(action: EnumPlayerInfoAction, list: List<String>) {
        plugin.runTaskLater(5, true) {
            val server = (plugin.server as CraftServer).server
            val world = (plugin.server.worlds.first() as CraftWorld).handle
            val manager = PlayerInteractManager(world)
            list.forEach { fakePlayerName ->
                @Suppress("DEPRECATION") val fakePlayer = plugin.server.getOfflinePlayer(fakePlayerName)
                val profile = GameProfile(fakePlayer.uniqueId, fakePlayerName)
                val fakeEntityPlayer = EntityPlayer(server, world, profile, manager).apply {
                    this.listName = CraftChatMessage.fromStringOrNull(
                        when {
                            fakePlayer.isOp -> "&0&3"
                            fakePlayer.isOnline -> "&1&f"
                            else -> "&2&7"
                        }.toColor() + fakePlayerName
                    )
                }
                val packet = PacketPlayOutPlayerInfo(action, fakeEntityPlayer)
                plugin.server.onlinePlayers.forEach {
                    (it as CraftPlayer).handle.playerConnection.sendPacket(packet)
                }
            }
        }
    }
}
