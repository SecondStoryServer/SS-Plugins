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
        private val id: String,
        private val type: Material
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
            type: Material,
            amount: Int
        ): Item() {
            override val item = CustomItemStack.create(type, amount)
            override val needsText by lazy { "${item.i18NDisplayName} × $amount" }
        }

        class CrackShot(
            id: String,
            amount: Int
        ): Item() {
            override val item = CrackShotAPI.getItem(id, amount)
            override val needsText by lazy { "${item?.display} × $amount" }
        }

        class CrackShotPlus(
            id: String,
            amount: Int
        ): Item() {
            override val item = CrackShotPlusAPI.getAttachment(id, amount)
            override val needsText by lazy { "${item?.display} × $amount" }
        }

        class MythicMobs(
            id: String,
            amount: Int
        ): Item() {
            override val item = MythicMobsAPI.getItem(id, amount)
            override val needsText by lazy { "${item?.display} × $amount" }
        }
    }

    object UnAvailable: ShopElement()

    companion object {
        fun from(config: CustomFileConfig, path: String, line: String): ShopElement {
            val split = line.split("\\s+".toRegex())
            return when (split[0].toLowerCase()) {
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
                        val amount = split.getOrNull(2)?.toIntOrNull() ?: 1
                        Item.Minecraft(type, amount)
                    } else {
                        config.nullError(path, "Material(${split.getOrNull(1)})")
                        UnAvailable
                    }
                }
                "cs" -> {
                    val id = split.getOrNull(1)
                    if (id != null) {
                        val amount = split.getOrNull(2)?.toIntOrNull() ?: 1
                        Item.CrackShot(id, amount)
                    } else {
                        config.nullError(path, "String(${split.getOrNull(1)})")
                        UnAvailable
                    }
                }
                "csp" -> {
                    val id = split.getOrNull(1)
                    if (id != null) {
                        val amount = split.getOrNull(2)?.toIntOrNull() ?: 1
                        Item.CrackShotPlus(id, amount)
                    } else {
                        config.nullError(path, "String(${split.getOrNull(1)})")
                        UnAvailable
                    }
                }
                "mm" -> {
                    val id = split.getOrNull(1)
                    if (id != null) {
                        val amount = split.getOrNull(2)?.toIntOrNull() ?: 1
                        Item.MythicMobs(id, amount)
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
