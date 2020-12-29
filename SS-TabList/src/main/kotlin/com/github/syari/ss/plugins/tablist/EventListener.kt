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
import java.util.UUID

object EventListener: Listener {
    @EventHandler
    fun on(e: SSPluginMessageEvent) {
        val template = e.template as? PluginMessageTemplateTabList ?: return
        val server = (plugin.server as CraftServer).server
        val world = (plugin.server.worlds.first() as CraftWorld).handle
        val manager = PlayerInteractManager(world)
        template.playerNameList.forEach { fakePlayerName ->
            val profile = GameProfile(UUID.randomUUID(), fakePlayerName)
            val fakePlayer = EntityPlayer(server, world, profile, manager)
            val packet = PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, fakePlayer)
            plugin.server.onlinePlayers.forEach {
                (it as CraftPlayer).handle.playerConnection.sendPacket(packet)
            }
        }
    }
}