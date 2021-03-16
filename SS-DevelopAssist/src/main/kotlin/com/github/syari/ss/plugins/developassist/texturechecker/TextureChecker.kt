package com.github.syari.ss.plugins.developassist.texturechecker

import com.github.syari.spigot.api.command.command
import com.github.syari.spigot.api.command.tab.CommandTabArgument.Companion.argument
import com.github.syari.spigot.api.config.config
import com.github.syari.spigot.api.config.type.ConfigDataType
import com.github.syari.ss.plugins.core.code.IConfigLoader
import com.github.syari.ss.plugins.core.inventory.CreateInventory.inventory
import com.github.syari.ss.plugins.core.message.template.ConstantMessage.OnlyPlayer
import com.github.syari.ss.plugins.core.message.template.ConstantMessage.ReloadConfig
import com.github.syari.ss.plugins.core.message.template.templateMessage
import com.github.syari.ss.plugins.developassist.Main.Companion.plugin
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object TextureChecker : IConfigLoader {
    override fun onEnable() {
        super.onEnable()
        registerCommand()
    }

    private lateinit var parentMaterials: List<Material>

    private fun registerCommand() {
        plugin.command("ctexture") {
            tab {
                argument { addAll("reload", "open") }
                argument("open") { addAll(Material.values().map(Material::name)) }
            }
            execute {
                val template = templateMessage("TextureChecker")
                when (args.lowerOrNull(0)) {
                    "reload" -> {
                        template.send(ReloadConfig)
                        load(sender)
                    }
                    "open" -> {
                        val player = sender as? Player ?: return@execute template.sendError(OnlyPlayer)
                        val typeName = args.getOrNull(1)
                        if (typeName != null) {
                            val type = Material.getMaterial(typeName.toUpperCase()) ?: return@execute template.sendError("存在しないマテリアルです")
                            openMaterial(player, type)
                        } else {
                            openMaterialList(player)
                        }
                    }
                    null -> {
                        template.sendCommandHelp(
                            "$label open" to "コンフィグで設定した一覧を開きます",
                            "$label open [material]" to "指定したマテリアルのテクスチャを確認します",
                            "$label reload" to "コンフィグを再読み込みします"
                        )
                    }
                    else -> {
                        val player = sender as? Player ?: return@execute template.sendError(OnlyPlayer)
                        openMaterialList(player)
                    }
                }
            }
        }
    }

    override fun load(sender: CommandSender) {
        plugin.config(sender, "texture-checker.yml") {
            parentMaterials = get("type", ConfigDataType.MaterialList).orEmpty()
        }
    }

    private fun openMaterialList(player: Player) {
        inventory("&9&l親アイテム", 6) {
            parentMaterials.forEachIndexed { index, material ->
                item(index, material, "&6&l${material.name}").event {
                    openMaterial(player, material)
                }
            }
        }.open(player)
    }

    private fun openMaterial(player: Player, material: Material, page: Int = 0) {
        when {
            page < 0 -> openMaterial(player, material)
            else -> inventory("&9&l${material.name}", 6) {
                val modelDataOffset = (page * 45)
                for (i in 0 until 45) {
                    item(i, material, "&6&l${i + modelDataOffset}", customModelData = i + modelDataOffset)
                }
                item(45..53, Material.BLACK_STAINED_GLASS_PANE)
                item(47, Material.ORANGE_STAINED_GLASS_PANE, "&d<<").event {
                    openMaterial(player, material, page - 1)
                }
                item(49, Material.RED_STAINED_GLASS_PANE, "&c戻る").event {
                    openMaterialList(player)
                }
                item(51, Material.ORANGE_STAINED_GLASS_PANE, "&d>>").event {
                    openMaterial(player, material, page + 1)
                }
            }.open(player)
        }
    }
}
