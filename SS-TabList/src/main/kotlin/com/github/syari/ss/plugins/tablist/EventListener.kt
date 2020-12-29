package com.github.syari.ss.plugins.tablist

import com.github.syari.ss.plugins.core.pluginMessage.SSPluginMessageEvent
import com.github.syari.ss.plugins.tablist.Main.Companion.plugin
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
        val server = (plugin.server as CraftServer).server
        val world = (plugin.server.worlds.first() as CraftWorld).handle
        val manager = PlayerInteractManager(world)
        list.forEach { fakePlayerName ->
            @Suppress("DEPRECATION") val fakePlayer = plugin.server.getOfflinePlayer(fakePlayerName)
            val profile = GameProfile(fakePlayer.uniqueId, fakePlayerName)
            val fakeEntityPlayer = EntityPlayer(server, world, profile, manager)
            val packet = PacketPlayOutPlayerInfo(action, fakeEntityPlayer)
            plugin.server.onlinePlayers.forEach {
                (it as CraftPlayer).handle.playerConnection.sendPacket(packet)
            }
        }
    }
}