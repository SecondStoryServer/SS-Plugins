package com.github.syari.ss.plugins.backup

import com.github.syari.ss.plugins.backup.Main.Companion.plugin
import java.io.File

object Backup {
    val backupDirectory by lazy { File(plugin.dataFolder, "history") }
    lateinit var groups: Map<String, BackupGroup>

    fun create(groups: List<BackupGroup>) {
        plugin.server.dispatchCommand(com.github.syari.ss.plugins.core.Main.console, "save-off")
        groups.forEach(BackupGroup::create)
        plugin.server.dispatchCommand(com.github.syari.ss.plugins.core.Main.console, "save-on")
    }
}