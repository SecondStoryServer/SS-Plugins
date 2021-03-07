package com.github.syari.ss.plugins.demonkill.craft

import com.github.syari.spigot.api.config.configDirectory
import com.github.syari.spigot.api.config.type.ConfigDataType
import com.github.syari.spigot.api.config.type.ConfigSectionType
import com.github.syari.ss.plugins.core.code.IConfigLoader
import com.github.syari.ss.plugins.core.config.ConfigItemConverter
import com.github.syari.ss.plugins.core.item.itemStack
import com.github.syari.ss.plugins.demonkill.Main.Companion.plugin
import com.github.syari.ss.plugins.dependency.crackshot.CrackShotAPI
import com.github.syari.ss.plugins.dependency.crackshotplus.CrackShotPlusAPI
import com.github.syari.ss.plugins.dependency.mythicmobs.MythicMobsAPI
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.inventory.ItemStack

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
                    val item = get("$base.item", ConfigDataType.ItemStack(itemConverter)) ?: return@forEach
                    val upgrade = buildMap<ItemStack, Weapon.Upgrade> {
                        section("$base.upgrade")?.forEach upgrade@{
                            val upgradeItem = get("$base.upgrade.$it.item", ConfigDataType.ItemStack(itemConverter)) ?: return@upgrade
                            val request = get("$base.upgrade.$it.request", ConfigDataType.ItemStackList(itemConverter), listOf())
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
                    val item = get("$base.item", ConfigDataType.ItemStack(itemConverter)) ?: return@forEach
                    val upgrade = buildMap<ItemStack, Armor.Upgrade> {
                        section("$base.upgrade")?.forEach upgrade@{
                            val upgradeItem = get("$base.upgrade.$it.item", ConfigDataType.ItemStack(itemConverter)) ?: return@upgrade
                            val armor = get("$base.upgrade.$it.armor", ConfigDataType.Inventory(itemConverter), mapOf())
                            val request = get("$base.upgrade.$it.request", ConfigDataType.ItemStackList(itemConverter), listOf())
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
                    val item = get("$base.item", ConfigDataType.ItemStack(itemConverter)) ?: return@forEach
                    val list = buildMap<Int, Pair<ItemStack, List<ItemStack>>> {
                        section("$base.create", ConfigSectionType.Int)?.forEach create@{ slot ->
                            val createItem = get("$base.create.$slot.item", ConfigDataType.ItemStack(itemConverter)) ?: return@create
                            val request = get("$base.create.$slot.request", ConfigDataType.ItemStackList(itemConverter), listOf())
                            put(slot, createItem to request)
                        }
                    }
                    add(Other(item, list))
                }
            }
        }
    }

    private val itemTypeMap = mapOf<String, (String) -> ItemStack?>(
        "mc" to {
            val typeName = it.substringBefore(':').toUpperCase()
            val model = it.substringAfter(':', "")
            Material.getMaterial(typeName)?.let { material ->
                itemStack(material, customModelData = model.toIntOrNull())
            }
        },
        "mm" to {
            MythicMobsAPI.getItem(it)
        },
        "cs" to {
            CrackShotAPI.getItem(it)
        },
        "csp" to {
            CrackShotPlusAPI.getAttachment(it)
        }
    )

    private val itemConverter = ConfigItemConverter.FromName(itemTypeMap)
}
