package com.github.syari.ss.plugins.backup

import com.github.syari.ss.plugins.backup.Main.Companion.plugin
import com.github.syari.ss.plugins.core.Main.Companion.console
import com.github.syari.ss.plugins.core.code.IConfigLoader
import com.github.syari.ss.plugins.core.config.CreateConfig.config
import com.github.syari.ss.plugins.core.config.type.ConfigDataType
import org.bukkit.command.CommandSender

object ConfigLoader : IConfigLoader {
    override fun onEnable() {
        load(console)
    }

    override fun load(sender: CommandSender) {
        plugin.config(sender, "config.yml") {
            Backup.groups = section("group")?.map { name ->
                val preCommands = get("group.$name.settings.command.pre", ConfigDataType.STRINGLIST, listOf(), false)
                val postCommands = get("group.$name.settings.command.post", ConfigDataType.STRINGLIST, listOf(), false)
                val settings = BackupSettings(preCommands, postCommands)
                val worldNames = get("group.$name.world", ConfigDataType.STRINGLIST, listOf(), false)
                val pluginNames = get("group.$name.plugin", ConfigDataType.STRINGLIST, listOf(), false)
                val otherPaths = get("group.$name.other", ConfigDataType.STRINGLIST, listOf(), false)
                BackupGroup.from(name, worldNames, pluginNames, otherPaths, settings)
            }?.associateBy { it.name }.orEmpty()
            val webDavUrl = get("webdav.url", ConfigDataType.STRING, false)
            val webDavUser = get("webdav.user", ConfigDataType.STRING, false)
            val webDavPass = get("webdav.pass", ConfigDataType.STRING, false)
            WebDAVUploader.uploader = WebDAVUploader.from(webDavUrl, webDavUser, webDavPass)
        }
    }
}
