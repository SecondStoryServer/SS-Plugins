package com.github.syari.ss.plugins.backup

import com.github.syari.ss.plugins.backup.Main.Companion.plugin
import com.github.syari.ss.plugins.core.Main.Companion.console
import com.github.syari.ss.plugins.core.code.OnEnable
import com.github.syari.ss.plugins.core.config.CreateConfig.config
import com.github.syari.ss.plugins.core.config.dataType.ConfigDataType
import org.bukkit.command.CommandSender

object ConfigLoader: OnEnable {
    override fun onEnable() {
        load(console)
    }

    fun load(sender: CommandSender) {
        plugin.config(sender, "config.yml") {
            Backup.groups = section("group")?.map { name ->
                val worldNames = get("group.$name.world", ConfigDataType.STRINGLIST, listOf(), false)
                val pluginNames = get("group.$name.plugin", ConfigDataType.STRINGLIST, listOf(), false)
                val otherPaths = get("group.$name.other", ConfigDataType.STRINGLIST, listOf(), false)
                BackupGroup.from(name, worldNames, pluginNames, otherPaths)
            }?.associateBy { it.name }.orEmpty()
            val webDavUrl = get("webdav.url", ConfigDataType.STRING, false)
            val webDavUser = get("webdav.user", ConfigDataType.STRING, false)
            val webDavPass = get("webdav.pass", ConfigDataType.STRING, false)
            WebDAVUploader.uploader = WebDAVUploader.from(webDavUrl, webDavUser, webDavPass)
        }
    }
}