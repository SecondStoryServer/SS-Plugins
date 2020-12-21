package com.github.syari.ss.plugins.backup

import com.github.syari.ss.plugins.backup.Main.Companion.plugin
import com.github.syari.ss.plugins.core.code.OnEnable
import com.github.syari.ss.plugins.core.command.create.CreateCommand.command
import com.github.syari.ss.plugins.core.command.create.CreateCommand.element
import com.github.syari.ss.plugins.core.command.create.CreateCommand.tab

object CommandCreator: OnEnable {
    override fun onEnable() {
        command(plugin, "backup", "SS-Backup", tab { element("now", "reload") }, tab("now **") {
            element(Backup.groups.keys)
        }) { sender, args ->
            when (args.whenIndex(0)) {
                "now" -> {
                    if (args.size == 1) return@command sendError("グループ名を入力してください")
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
                "reload" -> {
                    sendWithPrefix("コンフィグを読み込みます")
                    ConfigLoader.load(sender)
                }
                else -> sendHelp(
                    "backup now" to "バックアップを実行します", //
                    "backup reload" to "コンフィグを再読み込みします"
                )
            }
        }
    }
}