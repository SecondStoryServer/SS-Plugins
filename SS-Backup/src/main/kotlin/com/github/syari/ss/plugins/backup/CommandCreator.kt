package com.github.syari.ss.plugins.backup

import com.github.syari.spigot.api.command.command
import com.github.syari.spigot.api.command.tab.CommandTabArgument.Companion.argument
import com.github.syari.ss.plugins.backup.Main.Companion.plugin
import com.github.syari.ss.plugins.core.code.OnEnable
import com.github.syari.ss.plugins.core.message.template.ConstantMessage.ReloadConfig
import com.github.syari.ss.plugins.core.message.template.templateMessage

object CommandCreator : OnEnable {
    override fun onEnable() {
        plugin.command("backup") {
            tab {
                argument { addAll("now", "upload", "reload") }
                argument("now **") { addAll(Backup.groups.keys) }
            }
            execute {
                val template = templateMessage("Backup")
                when (args.lowerOrNull(0)) {
                    "now" -> {
                        if (args.size == 1) return@execute template.sendError("グループ名を入力してください")
                        val groups = mutableListOf<BackupGroup>()
                        val nils = mutableListOf<String>()
                        args.subList(1, args.size).forEach { name ->
                            Backup.groups[name]?.let {
                                groups.add(it)
                            } ?: run {
                                nils.add(name)
                            }
                        }
                        if (groups.isNotEmpty()) {
                            template.send("&6${groups.joinToString { it.name }} &fのバックアップを始めます")
                            Backup.create(groups)
                        }
                        if (nils.isNotEmpty()) {
                            template.sendError("&6${nils.joinToString()} &cは存在しませんでした")
                        }
                    }
                    "upload" -> {
                        val uploader = WebDAVUploader.uploader ?: return@execute template.sendError("アップロード先が設定されていません")
                        val listFiles = Backup.backupDirectory.listFiles()
                        if (listFiles.isNullOrEmpty()) return@execute template.sendError("ファイルが見つかりませんでした")
                        template.send("&6${listFiles.size} &f個のファイルをアップロードします")
                        listFiles.forEach {
                            uploader.upload(it)
                        }
                    }
                    "reload" -> {
                        template.send(ReloadConfig)
                        ConfigLoader.load(sender)
                    }
                    else -> template.sendCommandHelp(
                        "$label now" to "バックアップを実行します",
                        "$label reload" to "コンフィグを再読み込みします"
                    )
                }
            }
        }
    }
}
