package com.github.syari.ss.plugins.backup

import com.github.syari.ss.plugins.backup.Main.Companion.plugin
import com.github.syari.ss.plugins.core.code.OnEnable
import com.github.syari.ss.plugins.core.command.create.CommandCreator.Companion.command
import com.github.syari.ss.plugins.core.command.create.CommandTabElement.Companion.element

object CommandCreator : OnEnable {
    override fun onEnable() {
        plugin.command("backup", "SS-Backup") {
            tab {
                arg { element("now", "upload", "reload") }
                arg("now **") { element(Backup.groups.keys) }
            }
            execute {
                when (args.whenIndex(0)) {
                    "now" -> {
                        if (args.size == 1) return@execute sendError("グループ名を入力してください")
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
                    "upload" -> {
                        val uploader = WebDAVUploader.uploader ?: return@execute sendError("アップロード先が設定されていません")
                        val listFiles = Backup.backupDirectory.listFiles()
                        if (listFiles.isNullOrEmpty()) return@execute sendError("ファイルが見つかりませんでした")
                        sendWithPrefix("&6${listFiles.size} &f個のファイルをアップロードします")
                        listFiles.forEach {
                            uploader.upload(it)
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
}
