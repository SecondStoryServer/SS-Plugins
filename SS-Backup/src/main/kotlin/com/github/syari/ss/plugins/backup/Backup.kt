package com.github.syari.ss.plugins.backup

object Backup {
    lateinit var groups: Map<String, BackupGroup>

    fun create(groups: List<BackupGroup>) {
        Main.plugin.server.dispatchCommand(com.github.syari.ss.plugins.core.Main.console, "save-off")
        groups.forEach(BackupGroup::create)
        Main.plugin.server.dispatchCommand(com.github.syari.ss.plugins.core.Main.console, "save-on")
    }
}