package com.github.syari.ss.plugins.ma.shop

import com.github.syari.ss.plugins.core.config.CustomFileConfig
import com.github.syari.ss.plugins.core.item.CustomItemStack
import com.github.syari.ss.plugins.core.item.ItemStackPlus.give
import com.github.syari.ss.plugins.core.item.ItemStackPlus.hasItem
import com.github.syari.ss.plugins.core.item.ItemStackPlus.removeItem
import com.github.syari.ss.plugins.dependency.crackshot.CrackShotAPI
import com.github.syari.ss.plugins.dependency.crackshotplus.CrackShotPlusAPI
import com.github.syari.ss.plugins.dependency.mythicmobs.MythicMobsAPI
import org.bukkit.Material
import org.bukkit.entity.Player

sealed class ShopElement {
    open fun give(player: Player) = true
    open fun has(player: Player) = false
    open fun remove(player: Player) {}
    open val display = CustomItemStack.create(Material.BARRIER, "&cエラー")
    open val targetText = ""
    open val needsText = ""

    class Jump(
        private val id: String, private val type: Material
    ): ShopElement() {
        override fun give(player: Player) = Shop.get(id)?.open(player)?.run { false } ?: true

        override val display by lazy { CustomItemStack.create(type, "&6${Shop.get(id)?.name}") }
        override val targetText = "クリックで開く"
    }

    sealed class Item: ShopElement() {
        open val item: CustomItemStack? = null

        override val display by lazy {
            item?.apply {
                display = "&b" + (display?.ifEmpty { null } ?: i18NDisplayName) + " × " + amount
            } ?: CustomItemStack.create(Material.STONE, "&cエラー")
        }
        override val targetText = "クリックで購入する"

        override fun give(player: Player): Boolean {
            item?.let { player.give(it) }
            return true
        }

        override fun has(player: Player) = item?.let { player.hasItem(it) } ?: false

        override fun remove(player: Player) {
            item?.let { player.removeItem(it) }
        }

        class Minecraft(
            type: Material, amount: Int
        ): Item() {
            override val item = CustomItemStack.create(type, amount)
            override val needsText by lazy { "${item.i18NDisplayName} × $amount" }
        }

        class Custom(
            override val item: CustomItemStack, amount: Int
        ): Item() {
            override val needsText by lazy { "${item.display} × $amount" }
        }
    }

    object UnAvailable: ShopElement()

    companion object {
        fun from(config: CustomFileConfig, path: String, line: String): ShopElement {
            val split = line.split("\\s+".toRegex())
            return when (val elementType = split[0].toLowerCase()) {
                "jump" -> {
                    val id = split.getOrNull(1)
                    if (id != null) {
                        val type = split.getOrNull(2)?.let {
                            Material.getMaterial(it) ?: run {
                                config.nullError(path, "Material(${it})")
                                null
                            }
                        } ?: Material.COMPASS
                        Jump(id, type)
                    } else {
                        UnAvailable
                    }
                }
                "mc" -> {
                    val type = split.getOrNull(1)?.let { Material.getMaterial(it) }
                    if (type != null) {
                        val amount = split.getOrNull(2)?.let {
                            it.toIntOrNull() ?: run {
                                config.nullError(path, "Int($it)")
                                null
                            }
                        } ?: 1
                        Item.Minecraft(type, amount)
                    } else {
                        config.nullError(path, "Material(${split.getOrNull(1)})")
                        UnAvailable
                    }
                }
                "cs", "csp", "mm" -> {
                    val id = split.getOrNull(1)
                    if (id != null) {
                        val amount = split.getOrNull(2)?.let {
                            it.toIntOrNull() ?: run {
                                config.nullError(path, "Int($it)")
                                null
                            }
                        } ?: 1
                        val item = when (elementType) {
                            "cs" -> {
                                CrackShotAPI.getItem(id, amount)
                            }
                            "csp" -> {
                                CrackShotPlusAPI.getAttachment(id, amount)
                            }
                            "mm" -> {
                                MythicMobsAPI.getItem(id, amount)
                            }
                            else -> error("Unreachable")
                        }
                        if (item != null) {
                            Item.Custom(item, amount)
                        } else {
                            config.nullError(path, "String($id)")
                            UnAvailable
                        }
                    } else {
                        config.nullError(path, "String(${split.getOrNull(1)})")
                        UnAvailable
                    }
                }
                else -> UnAvailable
            }
        }
    }
}
