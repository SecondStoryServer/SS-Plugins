package com.github.syari.ss.plugins.backup

import com.github.syari.ss.plugins.backup.Main.Companion.plugin
import com.github.syari.ss.plugins.core.code.OnEnable
import com.github.syari.ss.plugins.core.command.create.CreateCommand.createCommand
import com.github.syari.ss.plugins.core.command.create.CreateCommand.element
import com.github.syari.ss.plugins.core.command.create.CreateCommand.tab

object CommandCreator: OnEnable {
    override fun onEnable() {
        createCommand(plugin, "backup", "SS-Backup", tab { _, _ -> element("now") }) { _, args ->
            when (args.whenIndex(0)) {
                "now" -> {
                    if (args.size == 1) return@createCommand sendError("グループ名を入力してください")
                    val groups = args.slice(1).mapNotNull { Backup.groups[it] }
                    val names = groups.joinToString(", ") { it.name }
                    sendWithPrefix("&6$names &fのバックアップを始めます")
                    Backup.create(groups)
                }
            }
        }
    }
}