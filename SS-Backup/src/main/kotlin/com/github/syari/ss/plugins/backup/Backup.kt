package com.github.syari.ss.plugins.backup

import com.github.syari.ss.plugins.backup.Main.Companion.plugin
import com.github.syari.ss.plugins.backup.event.PostBackupEvent
import com.github.syari.ss.plugins.backup.event.PreBackupEvent
import java.io.File

object Backup {
    val backupDirectory by lazy { File(plugin.dataFolder, "history") }
    lateinit var groups: Map<String, BackupGroup>

    fun create(groups: List<BackupGroup>) {
        PreBackupEvent(groups).callEvent()
        groups.forEach(BackupGroup::create)
        PostBackupEvent(groups).callEvent()
    }
}
