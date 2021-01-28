package com.github.syari.ss.plugins.developassist.itemcreator

import com.github.syari.ss.plugins.core.code.OnEnable
import com.github.syari.ss.plugins.core.command.create.CommandCreator.Companion.command
import com.github.syari.ss.plugins.core.command.create.CommandTabElement.Companion.element
import com.github.syari.ss.plugins.core.command.create.ErrorMessage
import com.github.syari.ss.plugins.core.item.Base64Item
import com.github.syari.ss.plugins.core.item.CustomItemStack
import com.github.syari.ss.plugins.core.item.ItemStackPlus.give
import com.github.syari.ss.plugins.core.message.JsonBuilder
import com.github.syari.ss.plugins.core.message.JsonBuilder.Companion.buildJson
import com.github.syari.ss.plugins.core.message.Message.send
import com.github.syari.ss.plugins.developassist.Main.Companion.plugin
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

object ItemCreator : OnEnable {
    override fun onEnable() {
        fun String.replaceSpace() = replace("<sp>", " ")

        plugin.command("citem", "ItemCreator") {
            tab {
                arg { element("name", "lore", "type", "model", "unbreak", "base64") }
                arg("lore") { element("edit", "insert", "add", "remove", "clear") }
                arg("type") { element(Material.values().map(Material::name)) }
            }
            execute {
                val player = sender as? Player ?: return@execute sendError(ErrorMessage.OnlyPlayer)

                fun getHeldItem(): Pair<ItemStack, CustomItemStack>? {
                    val itemStack = player.inventory.itemInMainHand
                    val item = CustomItemStack.create(itemStack)
                    return if (item.type == Material.AIR) {
                        sendError("アイテムを持ってください")
                        null
                    } else {
                        itemStack to item
                    }
                }

                when (args.whenIndex(0)) {
                    "name" -> {
                        val (_, item) = getHeldItem() ?: return@execute
                        item.display = args.slice(1).joinToString(" ").replaceSpace()
                        sendWithPrefix("アイテム名を変更しました")
                    }
                    "lore" -> {
                        val (_, item) = getHeldItem() ?: return@execute
                        when (args.whenIndex(1)) {
                            "edit" -> {
                                item.lore = args.slice(2).map(String::replaceSpace)
                                sendWithPrefix("説明文を変更しました")
                            }
                            "insert" -> {
                                val line = args.getOrNull(2)?.toIntOrNull() ?: return@execute sendError("行数を入力してください")
                                if (line < 0 || item.lore.size < line) return@execute sendError("挿入できない行です")
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
                                val line = args.getOrNull(2)?.toIntOrNull() ?: return@execute sendError("行数を入力してください")
                                if (line < 0 || item.lore.size <= line) return@execute sendError("存在しない行です")
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
                            }
                        }
                    }
                    "type" -> {
                        val typeName = args.getOrNull(1)?.toUpperCase()
                        if (typeName != null) {
                            val (_, item) = getHeldItem() ?: return@execute
                            val type = Material.getMaterial(typeName) ?: return@execute sendError("存在しないアイテムタイプです")
                            item.type = type
                            sendWithPrefix("アイテムタイプを変更しました")
                        } else {
                            val item = player.inventory.itemInMainHand
                            val itemTypeName = item.type.name
                            sendWithPrefix(
                                buildJson {
                                    append(itemTypeName, JsonBuilder.Hover.Text("&6コピー"), JsonBuilder.Click.Clipboard(itemTypeName))
                                }
                            )
                        }
                    }
                    "model" -> {
                        val (_, item) = getHeldItem() ?: return@execute
                        val customModelData = args.getOrNull(1)?.toIntOrNull()
                        item.customModelData = customModelData
                        if (customModelData != null) {
                            sendWithPrefix("モデルデータ値を変更しました")
                        } else {
                            sendWithPrefix("モデルデータ値を削除しました")
                        }
                    }
                    "unbreak" -> {
                        val (_, item) = getHeldItem() ?: return@execute
                        item.unbreakable = !item.unbreakable
                        if (item.unbreakable) {
                            sendWithPrefix("耐久無限を付与しました")
                        } else {
                            sendWithPrefix("耐久無限を削除しました")
                        }
                    }
                    "base64" -> {
                        args.getOrNull(1)?.let { base64 ->
                            val itemStack = Base64Item.fromBase64(base64) ?: return@execute sendError("アイテムの取得に失敗しました")
                            player.give(itemStack)
                        } ?: run {
                            val (itemStack, item) = getHeldItem() ?: return@execute
                            val base64 = Base64Item.toBase64(itemStack) ?: return@execute sendError("Base64の取得に失敗しました")
                            val displayName = item.itemMeta?.displayName?.ifBlank { null } ?: item.i18NDisplayName ?: item.type.name
                            sendWithPrefix(
                                buildJson {
                                    append(displayName, JsonBuilder.Hover.Item(item))
                                    append("  ")
                                    append("&b[Base64]", JsonBuilder.Hover.Text("&6コピー"), JsonBuilder.Click.Clipboard(base64))
                                }
                            )
                        }
                    }
                    else -> {
                        sendHelp(
                            "citem name [Name]" to "アイテム名を変更します", //
                            "citem lore" to "説明文を変更します", //
                            "citem type" to "アイテムタイプを出力します", //
                            "citem type [Material]" to "アイテムタイプを変更します", //
                            "citem model [Value]" to "モデルデータ値を変更します", //
                            "citem unbreak" to "耐久無限を変更します", //
                            "citem base64" to "アイテムをBase64に変換します", //
                            "citem base64 [value]" to "Base64をアイテムに変換します",
                        )

                        sender.send("&7* &a<sp> &7は &a空白 &7に置き換わります")
                    }
                }
            }
        }
    }
}
