package com.github.syari.ss.plugins.developassist.texturechecker

import com.github.syari.ss.plugins.core.Main.Companion.console
import com.github.syari.ss.plugins.core.code.IConfigLoader
import com.github.syari.ss.plugins.core.command.create.CommandCreator.Companion.command
import com.github.syari.ss.plugins.core.command.create.CommandTabElement.Companion.element
import com.github.syari.ss.plugins.core.command.create.ErrorMessage
import com.github.syari.ss.plugins.core.config.CreateConfig.config
import com.github.syari.ss.plugins.core.config.type.ConfigDataType
import com.github.syari.ss.plugins.core.inventory.CreateInventory.inventory
import com.github.syari.ss.plugins.developassist.Main.Companion.plugin
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object TextureChecker : IConfigLoader {
    override fun onEnable() {
        load(console)
        registerCommand()
    }

    private lateinit var parentMaterials: List<Material>

    private fun registerCommand() {
        plugin.command("ctexture", "TextureChecker") {
            tab {
                arg { element("reload", "open") }
                arg("open") { element(Material.values().map(Material::name)) }
            }
            execute {
                when (args.whenIndex(0)) {
                    "reload" -> {
                        sendWithPrefix("コンフィグを読み込みます")
                        load(sender)
                    }
                    "open" -> {
                        val player = sender as? Player ?: return@execute sendError(ErrorMessage.OnlyPlayer)
                        val typeName = args.getOrNull(1)
                        if (typeName != null) {
                            val type = Material.getMaterial(typeName.toUpperCase()) ?: return@execute sendError("存在しないマテリアルです")
                            openMaterial(player, type)
                        } else {
                            openMaterialList(player)
                        }
                    }
                    else -> {
                    }
                }
            }
        }
    }

    override fun load(sender: CommandSender) {
        plugin.config(sender, "texture-checker.yml") {
            parentMaterials = get("type", ConfigDataType.MATERIALLIST).orEmpty()
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
                    item(i, material, "&6&l${i + modelDataOffset}", customModelData = modelDataOffset)
                }
                item(45..53, Material.BLACK_STAINED_GLASS_PANE)
                item(47, Material.ORANGE_STAINED_GLASS_PANE, "&d<<").event {
                    openMaterial(player, material, page - 1)
                }
                item(51, Material.ORANGE_STAINED_GLASS_PANE, "&d>>").event {
                    openMaterial(player, material, page + 1)
                }
            }.open(player)
        }
    }
}
