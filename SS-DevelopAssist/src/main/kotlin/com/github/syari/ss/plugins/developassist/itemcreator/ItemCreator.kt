package com.github.syari.ss.plugins.developassist.itemcreator

import com.github.syari.spigot.api.command.command
import com.github.syari.spigot.api.command.tab.CommandTabArgument.Companion.argument
import com.github.syari.ss.plugins.core.code.OnEnable
import com.github.syari.ss.plugins.core.item.Base64Item
import com.github.syari.ss.plugins.core.item.CustomItemStack
import com.github.syari.ss.plugins.core.item.ItemStackPlus.give
import com.github.syari.ss.plugins.core.message.JsonBuilder
import com.github.syari.ss.plugins.core.message.JsonBuilder.Companion.buildJson
import com.github.syari.ss.plugins.core.message.Message.send
import com.github.syari.ss.plugins.core.message.template.ConstantMessage.OnlyPlayer
import com.github.syari.ss.plugins.core.message.template.templateMessage
import com.github.syari.ss.plugins.developassist.Main.Companion.plugin
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

object ItemCreator : OnEnable {
    override fun onEnable() {
        fun String.replaceSpace() = replace("<sp>", " ")

        plugin.command("citem") {
            tab {
                argument { add("name", "lore", "type", "model", "unbreak", "base64") }
                argument("lore") { add("edit", "insert", "add", "remove", "clear") }
                argument("type") { addAll(Material.values().map(Material::name)) }
            }
            execute {
                val template = templateMessage("ItemCreator")
                val player = sender as? Player ?: return@execute template.sendError(OnlyPlayer)

                fun getHeldItem(): Pair<ItemStack, CustomItemStack>? {
                    val itemStack = player.inventory.itemInMainHand
                    val item = CustomItemStack.create(itemStack)
                    return if (item.type == Material.AIR) {
                        template.sendError("アイテムを持ってください")
                        null
                    } else {
                        itemStack to item
                    }
                }

                when (args.lowerOrNull(0)) {
                    "name" -> {
                        val (_, item) = getHeldItem() ?: return@execute
                        item.display = args.subList(1, args.size).joinToString(" ").replaceSpace()
                        template.send("アイテム名を変更しました")
                    }
                    "lore" -> {
                        val (_, item) = getHeldItem() ?: return@execute
                        when (args.lowerOrNull(1)) {
                            "edit" -> {
                                item.lore = args.subList(2, args.size).map(String::replaceSpace)
                                template.send("説明文を変更しました")
                            }
                            "insert" -> {
                                val line = args.getOrNull(2)?.toIntOrNull() ?: return@execute template.sendError("行数を入力してください")
                                if (line < 0 || item.lore.size < line) return@execute template.sendError("挿入できない行です")
                                item.editLore {
                                    addAll(line, args.subList(3, args.size).map(String::replaceSpace))
                                }
                                template.send("説明文を挿入しました")
                            }
                            "add" -> {
                                item.editLore {
                                    addAll(args.subList(2, args.size).map(String::replaceSpace))
                                }
                                template.send("説明文を追加しました")
                            }
                            "remove" -> {
                                val line = args.getOrNull(2)?.toIntOrNull() ?: return@execute template.sendError("行数を入力してください")
                                if (line < 0 || item.lore.size <= line) return@execute template.sendError("存在しない行です")
                                item.editLore {
                                    removeAt(line)
                                }
                                template.send("説明文を削除しました ")
                            }
                            "clear" -> {
                                item.lore = listOf()
                                template.send("説明文を削除しました ")
                            }
                            else -> {
                                template.sendCommandHelp(
                                    "$label lore edit [Lore...]" to "説明文を変更します",
                                    "$label lore insert [Line] [Lore...]" to "指定行目に説明文を挿入します",
                                    "$label lore add [Lore...]" to "末尾に説明文を追加します",
                                    "$label lore remove [Line]" to "指定行目の説明文を削除します",
                                    "$label lore clear" to "全ての説明文を削除します"
                                )
                            }
                        }
                    }
                    "type" -> {
                        val typeName = args.getOrNull(1)?.toUpperCase()
                        if (typeName != null) {
                            val (_, item) = getHeldItem() ?: return@execute
                            val type = Material.getMaterial(typeName) ?: return@execute template.sendError("存在しないアイテムタイプです")
                            item.type = type
                            template.send("アイテムタイプを変更しました")
                        } else {
                            val item = player.inventory.itemInMainHand
                            val itemTypeName = item.type.name
                            template.send(
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
                            template.send("モデルデータ値を変更しました")
                        } else {
                            template.send("モデルデータ値を削除しました")
                        }
                    }
                    "unbreak" -> {
                        val (_, item) = getHeldItem() ?: return@execute
                        item.unbreakable = !item.unbreakable
                        if (item.unbreakable) {
                            template.send("耐久無限を付与しました")
                        } else {
                            template.send("耐久無限を削除しました")
                        }
                    }
                    "base64" -> {
                        args.getOrNull(1)?.let { base64 ->
                            val itemStack = Base64Item.fromBase64(base64) ?: return@execute template.sendError("アイテムの取得に失敗しました")
                            player.give(itemStack)
                        } ?: run {
                            val (itemStack, item) = getHeldItem() ?: return@execute
                            val base64 = Base64Item.toBase64(itemStack) ?: return@execute template.sendError("Base64の取得に失敗しました")
                            val displayName = item.itemMeta?.displayName?.ifBlank { null } ?: item.i18NDisplayName ?: item.type.name
                            template.send(
                                buildJson {
                                    append(displayName, JsonBuilder.Hover.Item(item))
                                    append("  ")
                                    append("&b[Base64]", JsonBuilder.Hover.Text("&6コピー"), JsonBuilder.Click.Clipboard(base64))
                                }
                            )
                        }
                    }
                    else -> {
                        template.sendCommandHelp(
                            "$label name [Name]" to "アイテム名を変更します",
                            "$label lore" to "説明文を変更します",
                            "$label type" to "アイテムタイプを出力します",
                            "$label type [Material]" to "アイテムタイプを変更します",
                            "$label model [Value]" to "モデルデータ値を変更します",
                            "$label unbreak" to "耐久無限を変更します",
                            "$label base64" to "アイテムをBase64に変換します",
                            "$label base64 [value]" to "Base64をアイテムに変換します",
                        )

                        sender.send("&7* &a<sp> &7は &a空白 &7に置き換わります")
                    }
                }
            }
        }
    }
}
