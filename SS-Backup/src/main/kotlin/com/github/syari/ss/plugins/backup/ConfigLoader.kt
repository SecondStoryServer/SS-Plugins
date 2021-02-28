package com.github.syari.ss.plugins.backup

import com.github.syari.spigot.api.config.config
import com.github.syari.spigot.api.config.type.ConfigDataType
import com.github.syari.ss.plugins.backup.Main.Companion.plugin
import com.github.syari.ss.plugins.core.code.IConfigLoader
import org.bukkit.command.CommandSender

object ConfigLoader : IConfigLoader {
    override fun load(sender: CommandSender) {
        plugin.config(sender, "config.yml") {
            Backup.groups = section("group")?.map { name ->
                val preCommands = get("group.$name.settings.command.pre", ConfigDataType.StringList, listOf(), false)
                val postCommands = get("group.$name.settings.command.post", ConfigDataType.StringList, listOf(), false)
                val settings = BackupSettings(preCommands, postCommands)
                val worldNames = get("group.$name.world", ConfigDataType.StringList, listOf(), false)
                val pluginNames = get("group.$name.plugin", ConfigDataType.StringList, listOf(), false)
                val otherPaths = get("group.$name.other", ConfigDataType.StringList, listOf(), false)
                BackupGroup.from(name, worldNames, pluginNames, otherPaths, settings)
            }?.associateBy { it.name }.orEmpty()
            val webDavUrl = get("webdav.url", ConfigDataType.String, false)
            val webDavUser = get("webdav.user", ConfigDataType.String, false)
            val webDavPass = get("webdav.pass", ConfigDataType.String, false)
            WebDAVUploader.uploader = WebDAVUploader.from(webDavUrl, webDavUser, webDavPass)
        }
    }
}
