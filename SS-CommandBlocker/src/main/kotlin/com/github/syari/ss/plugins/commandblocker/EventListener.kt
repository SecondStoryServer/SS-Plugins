package com.github.syari.ss.plugins.commandblocker

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolLibrary.getProtocolManager
import com.comphenix.protocol.events.ListenerPriority
import com.comphenix.protocol.events.PacketAdapter
import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.events.PacketEvent
import com.comphenix.protocol.reflect.FieldAccessException
import com.github.syari.ss.plugins.commandblocker.AvailableCommand.availableCommands
import com.github.syari.ss.plugins.commandblocker.Main.Companion.plugin
import com.github.syari.ss.plugins.core.code.OnEnable
import com.github.syari.ss.plugins.core.message.Message.action
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import java.lang.reflect.InvocationTargetException

object EventListener: OnEnable, Listener {
    override fun onEnable() {
        getProtocolManager().addPacketListener(object: PacketAdapter(
            plugin, ListenerPriority.NORMAL, PacketType.Play.Client.TAB_COMPLETE
        ) {
            @EventHandler
            override fun onPacketReceiving(e: PacketEvent) {
                if (e.packetType === PacketType.Play.Client.TAB_COMPLETE) {
                    try {
                        val p = e.player
                        if (p.isOp) return
                        val split = (e.packet.getSpecificModifier(String::class.java).read(0) as String).split(
                            "\\s+".toRegex(),
                            2
                        )
                        val label = split[0].substring(1).toLowerCase()
                        val commands = p.availableCommands
                        if (split.size == 1) {
                            val completions = PacketContainer(PacketType.Play.Server.TAB_COMPLETE)
                            completions.stringArrays.write(
                                0,
                                commands.filter { it.startsWith(label) }.map { "/$it" }.toTypedArray()
                            )
                            try {
                                getProtocolManager().sendServerPacket(p, completions)
                                e.isCancelled = true
                            } catch (e: InvocationTargetException) {
                                e.printStackTrace()
                            }
                            return
                        } else if (commands.contains(label)) {
                            return
                        }
                        e.isCancelled = true
                    } catch (e: FieldAccessException) {
                    }
                }
            }
        })
    }

    @EventHandler(ignoreCancelled = true)
    fun on(e: PlayerCommandPreprocessEvent) {
        val p = e.player
        if (p.isOp) return
        val label = e.message.split(Regex("\\s+"), 2)[0].substring(1).toLowerCase()
        if (p.availableCommands.contains(label)) return
        e.isCancelled = true
        p.action("&c&l&n実行できないコマンドです")
    }
}