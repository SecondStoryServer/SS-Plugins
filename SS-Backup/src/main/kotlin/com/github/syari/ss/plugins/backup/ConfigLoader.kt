package com.github.syari.ss.plugins.backup

import com.github.syari.ss.plugins.backup.Main.Companion.plugin
import com.github.syari.ss.plugins.core.Main.Companion.console
import com.github.syari.ss.plugins.core.code.OnEnable
import com.github.syari.ss.plugins.core.config.CreateConfig.config
import com.github.syari.ss.plugins.core.config.dataType.ConfigDataType
import org.bukkit.command.CommandSender
import java.io.File

object ConfigLoader: OnEnable {
    override fun onEnable() {
        load(console)
    }

    fun load(sender: CommandSender) {
        config(plugin, sender, "config.yml") {
            val backupDirectoryPath = get("backup.directory", ConfigDataType.STRING, "backup")
            Backup.backupDirectory = File(backupDirectoryPath)
            Backup.groups = section("group")?.map { name ->
                val worldNames = get("group.$name.world", ConfigDataType.STRINGLIST, listOf(), false)
                val pluginNames = get("group.$name.plugin", ConfigDataType.STRINGLIST, listOf(), false)
                val otherPaths = get("group.$name.other", ConfigDataType.STRINGLIST, listOf(), false)
                BackupGroup(name, worldNames, pluginNames, otherPaths)
            }?.associateBy { it.name }.orEmpty()
        }
    }
}