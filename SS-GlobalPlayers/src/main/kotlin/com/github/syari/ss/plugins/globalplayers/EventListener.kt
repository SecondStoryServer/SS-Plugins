package com.github.syari.ss.plugins.globalplayers

import com.github.syari.ss.plugins.core.code.StringEditor.toColor
import com.github.syari.ss.plugins.core.pluginMessage.SSPluginMessageEvent
import com.github.syari.ss.plugins.core.scheduler.CreateScheduler.runLater
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
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

object EventListener: Listener {
    private var lastPlayerList = listOf<String>()

    @EventHandler
    fun on(e: SSPluginMessageEvent) {
        val template = e.template as? PluginMessageTemplateTabList ?: return
        val playerList = template.playerNameList
        updatePlayers(EnumPlayerInfoAction.ADD_PLAYER, playerList)
        updatePlayers(EnumPlayerInfoAction.REMOVE_PLAYER, lastPlayerList.filterNot { playerList.contains(it) })
        lastPlayerList = playerList
    }

    private fun updatePlayers(action: EnumPlayerInfoAction, list: List<String>) {
        runLater(plugin, 5) {
            val server = (plugin.server as CraftServer).server
            val world = (plugin.server.worlds.first() as CraftWorld).handle
            val manager = PlayerInteractManager(world)
            list.forEach { fakePlayerName ->
                @Suppress("DEPRECATION") val fakePlayer = plugin.server.getOfflinePlayer(fakePlayerName)
                val displayName = when {
                    fakePlayer.isOp -> "&0&3"
                    fakePlayer.isOnline -> "&1&f"
                    else -> "&2&7"
                }.toColor + fakePlayerName
                val profile = GameProfile(fakePlayer.uniqueId, displayName)
                val fakeEntityPlayer = EntityPlayer(server, world, profile, manager)
                val packet = PacketPlayOutPlayerInfo(action, fakeEntityPlayer)
                plugin.server.onlinePlayers.forEach {
                    (it as CraftPlayer).handle.playerConnection.sendPacket(packet)
                }
            }
        }
    }
}