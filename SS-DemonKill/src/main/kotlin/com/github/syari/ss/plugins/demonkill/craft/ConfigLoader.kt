package com.github.syari.ss.plugins.demonkill.craft

import com.github.syari.spigot.api.config.configDirectory
import com.github.syari.spigot.api.config.type.ConfigDataType
import com.github.syari.spigot.api.config.type.ConfigSectionType
import com.github.syari.ss.plugins.core.code.IConfigLoader
import com.github.syari.ss.plugins.core.config.ConfigItemConverter
import com.github.syari.ss.plugins.core.config.Inventory
import com.github.syari.ss.plugins.core.config.Item
import com.github.syari.ss.plugins.core.config.ItemList
import com.github.syari.ss.plugins.core.item.CustomItemStack
import com.github.syari.ss.plugins.demonkill.Main.Companion.plugin
import com.github.syari.ss.plugins.dependency.crackshot.CrackShotAPI
import com.github.syari.ss.plugins.dependency.crackshotplus.CrackShotPlusAPI
import com.github.syari.ss.plugins.dependency.mythicmobs.MythicMobsAPI
import org.bukkit.Material
import org.bukkit.command.CommandSender

object ConfigLoader : IConfigLoader {
    override fun load(sender: CommandSender) {
        loadWeapon(sender)
        loadArmor(sender)
        loadOther(sender)
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun loadWeapon(sender: CommandSender) {
        Weapon.list = buildSet {
            plugin.configDirectory(sender, "Craft/Weapon") {
                section("")?.forEach { base ->
                    val item = get("$base.item", ConfigDataType.Item(itemConverter)) ?: return@forEach
                    val upgrade = buildMap<CustomItemStack, Weapon.Upgrade> {
                        section("$base.upgrade")?.forEach upgrade@{
                            val upgradeItem = get("$base.upgrade.$it.item", ConfigDataType.Item(itemConverter)) ?: return@upgrade
                            val request = get("$base.upgrade.$it.request", ConfigDataType.ItemList(itemConverter), listOf())
                            put(upgradeItem, Weapon.Upgrade(request))
                        }
                    }
                    add(Weapon(item, upgrade))
                }
            }
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun loadArmor(sender: CommandSender) {
        Armor.list = buildSet {
            plugin.configDirectory(sender, "Craft/Armor") {
                section("")?.forEach { base ->
                    val item = get("$base.item", ConfigDataType.Item(itemConverter)) ?: return@forEach
                    val upgrade = buildMap<CustomItemStack, Armor.Upgrade> {
                        section("$base.upgrade")?.forEach upgrade@{
                            val upgradeItem = get("$base.upgrade.$it.item", ConfigDataType.Item(itemConverter)) ?: return@upgrade
                            val armor = get("$base.upgrade.$it.armor", ConfigDataType.Inventory(itemConverter), mapOf())
                            val request = get("$base.upgrade.$it.request", ConfigDataType.ItemList(itemConverter), listOf())
                            put(upgradeItem, Armor.Upgrade(armor, request))
                        }
                    }
                    add(Armor(item, upgrade))
                }
            }
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun loadOther(sender: CommandSender) {
        Other.list = buildSet {
            plugin.configDirectory(sender, "Craft/Other") {
                section("")?.forEach { base ->
                    val item = get("$base.item", ConfigDataType.Item(itemConverter)) ?: return@forEach
                    val list = buildMap<Int, Pair<CustomItemStack, List<CustomItemStack>>> {
                        section("$base.create", ConfigSectionType.Int)?.forEach create@{ slot ->
                            val createItem = get("$base.create.$slot.item", ConfigDataType.Item(itemConverter)) ?: return@create
                            val request = get("$base.create.$slot.request", ConfigDataType.ItemList(itemConverter), listOf())
                            put(slot, createItem to request)
                        }
                    }
                    add(Other(item, list))
                }
            }
        }
    }

    private val itemTypeMap = mapOf<String, (String, Int) -> CustomItemStack?>(
        "mc" to { id, amount ->
            val typeName = id.substringBefore(':').toUpperCase()
            val model = id.substringAfter(':', "")
            Material.getMaterial(typeName)?.let { material ->
                CustomItemStack.create(material, amount).apply {
                    model.toIntOrNull()?.let {
                        customModelData = it
                    }
                }
            }
        },
        "mm" to { id, amount ->
            MythicMobsAPI.getItem(id, amount)
        },
        "cs" to { id, amount ->
            CrackShotAPI.getItem(id, amount)
        },
        "csp" to { id, amount ->
            CrackShotPlusAPI.getAttachment(id, amount)
        }
    )

    private val itemConverter = ConfigItemConverter.Format(itemTypeMap)
}
