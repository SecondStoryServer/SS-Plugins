package com.github.syari.ss.plugins.assist

import com.github.syari.spigot.api.config.config
import com.github.syari.spigot.api.config.type.ConfigDataType
import com.github.syari.spigot.api.event.events
import com.github.syari.spigot.api.uuid.UUIDPlayer
import com.github.syari.spigot.api.uuid.uuidOrNull
import com.github.syari.ss.plugins.assist.Main.Companion.plugin
import com.github.syari.ss.plugins.core.code.IConfigLoader
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.permissions.PermissionAttachment

object Permission : IConfigLoader {
    private val permissionAttachments = mutableMapOf<UUIDPlayer, PermissionAttachment>()
    private var permissions = mapOf<UUIDPlayer, List<String>>()

    override fun onEnable() {
        super.onEnable()
        plugin.events {
            event<PlayerJoinEvent> {
                applyPermissionToPlayer(it.player)
            }
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    override fun load(sender: CommandSender) {
        permissionAttachments.values.forEach(PermissionAttachment::remove)
        permissionAttachments.clear()
        plugin.config(sender, "permission.yml") {
            permissions = buildMap {
                section("")?.forEach {
                    val uuid = uuidOrNull(it) ?: return@forEach
                    put(UUIDPlayer(uuid), get(it, ConfigDataType.StringList, listOf()))
                }
            }
        }
        plugin.server.onlinePlayers.forEach(::applyPermissionToPlayer)
    }

    private fun applyPermissionToPlayer(player: Player) {
        val uuidPlayer = UUIDPlayer.from(player)
        val attachment = permissionAttachments.getOrPut(uuidPlayer) { player.addAttachment(plugin) }
        permissions[uuidPlayer]?.forEach { permission ->
            attachment.setPermission(permission, true)
        }
    }
}
