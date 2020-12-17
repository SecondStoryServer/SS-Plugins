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
                    val groups = mutableListOf<BackupGroup>()
                    val nils = mutableListOf<String>()
                    args.slice(1).forEach { name ->
                        Backup.groups[name]?.let {
                            groups.add(it)
                        } ?: run {
                            nils.add(name)
                        }
                    }
                    if (groups.isNotEmpty()) {
                        sendWithPrefix("&6${groups.joinToString { it.name }} &fのバックアップを始めます")
                        Backup.create(groups)
                    }
                    if (nils.isNotEmpty()) {
                        sendError("&6${nils.joinToString()} &cは存在しませんでした")
                    }
                }
            }
        }
    }
}