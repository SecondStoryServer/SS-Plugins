package com.github.syari.ss.plugins.itemcreator

import com.github.syari.ss.plugins.core.code.SSPlugin
import com.github.syari.ss.plugins.core.command.create.CreateCommand.createCommand
import com.github.syari.ss.plugins.core.command.create.ErrorMessage
import com.github.syari.ss.plugins.core.item.CustomItemStack
import com.github.syari.ss.plugins.core.message.Message.send
import org.bukkit.Material
import org.bukkit.entity.Player

class Main: SSPlugin() {
    override fun onEnable() {
        createCommand(this, "citem", "ItemCreator") { sender, args ->
            if (sender !is Player) return@createCommand sendError(ErrorMessage.OnlyPlayer)
            val item = CustomItemStack.create(sender.inventory.itemInMainHand)
            if (item.type == Material.AIR) return@createCommand sendError("アイテムを持ってください")
            when (args.whenIndex(0)) {
                "name" -> {
                    item.display = args.getOrNull(1)
                    sendWithPrefix("アイテム名を変更しました")
                }
                "lore" -> {
                    fun String.replaceSpace() = replace("<sp>", " ")

                    when (args.whenIndex(1)) {
                        "edit" -> {
                            item.lore = args.slice(2).map(String::replaceSpace)
                            sendWithPrefix("説明文を変更しました")
                        }
                        "insert" -> {
                            val line = args.getOrNull(2)?.toIntOrNull() ?: return@createCommand sendError("行数を入力してください")
                            if (line < 0 || item.lore.size < line) return@createCommand sendError("挿入できない行です")
                            item.editLore {
                                addAll(line, args.slice(3).map(String::replaceSpace))
                            }
                            sendWithPrefix("説明文を挿入しました")
                        }
                        "add" -> {
                            item.editLore {
                                addAll(args.slice(2).map(String::replaceSpace))
                            }
                            sendWithPrefix("説明文を追加しました")
                        }
                        "remove" -> {
                            val line = args.getOrNull(2)?.toIntOrNull() ?: return@createCommand sendError("行数を入力してください")
                            if (line < 0 || item.lore.size <= line) return@createCommand sendError("存在しない行です")
                            item.editLore {
                                removeAt(line)
                            }
                            sendWithPrefix("説明文を削除しました ")
                        }
                        "clear" -> {
                            item.lore = listOf()
                            sendWithPrefix("説明文を削除しました ")
                        }
                        else -> {
                            sendHelp(
                                "citem lore edit [Lore...]" to "説明文を変更します", //
                                "citem lore insert [Line] [Lore...]" to "指定行目に説明文を挿入します", //
                                "citem lore add [Lore...]" to "末尾に説明文を追加します", //
                                "citem lore remove [Line]" to "指定行目の説明文を削除します", //
                                "citem lore clear" to "全ての説明文を削除します"
                            )
                            sender.send("&a<sp> &fは &a空白 &fに置き換わります")
                        }
                    }
                }
                "model" -> {
                    val customModelData = args.getOrNull(1)?.toIntOrNull()
                    item.customModelData = customModelData
                    if (customModelData != null) {
                        sendWithPrefix("モデルデータ値を変更しました")
                    } else {
                        sendWithPrefix("モデルデータ値を削除しました")
                    }
                }
                else -> sendHelp(
                    "citem name [Name]" to "アイテム名を変更します", //
                    "citem lore" to "説明文を変更します", //
                    "citem model" to "モデルデータ値を変更します"
                )
            }
        }
    }
}