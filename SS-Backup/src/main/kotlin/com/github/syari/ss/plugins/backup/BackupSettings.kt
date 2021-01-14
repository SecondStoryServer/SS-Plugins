package com.github.syari.ss.plugins.backup

import com.github.syari.ss.plugins.core.command.RunCommand

class BackupSettings(private val preCommands: List<String>, private val postCommands: List<String>) {
    fun runPreCommand() {
        preCommands.forEach(RunCommand::runCommandFromConsole)
    }

    fun runPostCommand() {
        postCommands.forEach(RunCommand::runCommandFromConsole)
    }
}
