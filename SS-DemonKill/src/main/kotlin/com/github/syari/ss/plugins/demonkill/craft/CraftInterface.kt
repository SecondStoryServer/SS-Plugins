package com.github.syari.ss.plugins.demonkill.craft

import com.github.syari.ss.plugins.core.inventory.CreateInventory.inventory
import com.github.syari.ss.plugins.core.inventory.CustomInventory
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

object CraftInterface {
    fun openRoot(player: Player) {
        inventory("&9&lクラフト", 3) {
            item(0..26, Material.GRAY_STAINED_GLASS_PANE)
            item(10, Material.IRON_SWORD, "&6武器").event {
                openWeapon(player)
            }
            item(13, Material.IRON_CHESTPLATE, "&6防具").event {
                openArmor(player)
            }
            item(16, Material.APPLE, "&6その他").event {
                openOther(player)
            }
        }.open(player)
    }

    private fun openWeapon(player: Player) {
        inventory("&9&l武器", 3) {
            item((0..9) + (17..26), Material.GRAY_STAINED_GLASS_PANE)
            setUpgradeItems(player, Weapon.getFromInventory(player), ::openWeapon)
            item(22, Material.BARRIER, "&c閉じる").event {
                openRoot(player)
            }
        }.open(player)
    }

    private fun openArmor(player: Player) {
        inventory("&9&l防具", 3) {
            item((0..9) + (17..26), Material.GRAY_STAINED_GLASS_PANE)
            setUpgradeItems(player, Armor.getFromInventory(player), ::openArmor)
            item(22, Material.BARRIER, "&c閉じる").event {
                openRoot(player)
            }
        }.open(player)
    }

    private fun CustomInventory.setUpgradeItems(player: Player, baseItem: Upgradeable?, onClose: (Player) -> Unit) {
        val upgrade = when (baseItem) {
            is Weapon -> baseItem.upgrade
            is Armor -> baseItem.upgrade
            else -> {
                item(4, Material.AIR, "")
                return
            }
        }.map { it.key to it.value.request }
        item(4, baseItem.item)
        val indexList = when (upgrade.size) {
            1 -> listOf(13)
            2 -> listOf(11, 15)
            3 -> listOf(10, 13, 16)
            4 -> listOf(10, 12, 14, 16)
            5 -> listOf(11, 12, 13, 14, 15)
            6 -> listOf(10, 11, 12, 13, 14, 15)
            7 -> listOf(10, 11, 12, 13, 14, 15, 16)
            else -> return
        }
        var index = 0
        upgrade.forEach { (upgradeItem, request) ->
            item(indexList[index], upgradeItem).event {
                openUpgrade(player, baseItem, upgradeItem, request, onClose)
            }
            index ++
        }
    }

    private fun openUpgrade(player: Player, baseItem: Upgradeable, upgradeItem: ItemStack, request: List<ItemStack>, onClose: (Player) -> Unit) {
        inventory("&9&lアイテム強化", 4) {
            item((0..9) + (17..18) + (26..35), Material.GRAY_STAINED_GLASS_PANE)
            item(listOf(15, 24), Material.GRAY_STAINED_GLASS_PANE)
            item(10, baseItem.item)
            request.subList(0, 4.coerceAtMost(request.size)).forEachIndexed { index, requestItem ->
                item(11 + index, requestItem)
            }
            if (4 < request.size) {
                request.subList(4, 9.coerceAtMost(request.size)).forEachIndexed { index, requestItem ->
                    item(19 + index, requestItem)
                }
            }
            item(16, upgradeItem)
            item(25, Material.ANVIL, "&6作成")
            item(31, Material.BARRIER, "&c閉じる").event {
                onClose(player)
            }
        }.open(player)
    }

    private fun openOther(player: Player) {
        inventory("&9&lその他", 6) {
            item((0..9) + (17..18) + (26..27) + (35..36) + (44..54), Material.GRAY_STAINED_GLASS_PANE)
            val itemList = Other.getFromInventory(player)?.list
            itemList?.forEach { index, (item, request) ->
                item(index, item).event {
                    inventory("&9&lアイテム作成", 4) {
                        item((0..9) + (17..18) + (26..35), Material.GRAY_STAINED_GLASS_PANE)
                        item(listOf(15, 24), Material.GRAY_STAINED_GLASS_PANE)
                        request.subList(0, 5.coerceAtMost(request.size)).forEachIndexed { index, requestItem ->
                            item(10 + index, requestItem)
                        }
                        if (4 < request.size) {
                            request.subList(5, 10.coerceAtMost(request.size)).forEachIndexed { index, requestItem ->
                                item(19 + index, requestItem)
                            }
                        }
                        item(16, item)
                        item(25, Material.CRAFTING_TABLE, "&6作成")
                        item(31, Material.BARRIER, "&c閉じる").event {
                            openOther(player)
                        }
                    }.open(player)
                }
            }
            item(49, Material.BARRIER, "&c閉じる").event {
                openRoot(player)
            }
        }.open(player)
    }
}
