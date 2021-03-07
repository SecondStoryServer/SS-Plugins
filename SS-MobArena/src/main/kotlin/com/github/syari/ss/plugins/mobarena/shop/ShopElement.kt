package com.github.syari.ss.plugins.mobarena.shop

import com.github.syari.spigot.api.config.CustomConfig
import com.github.syari.ss.plugins.core.item.ItemStackPlus.give
import com.github.syari.ss.plugins.core.item.ItemStackPlus.hasItem
import com.github.syari.ss.plugins.core.item.ItemStackPlus.removeItem
import com.github.syari.ss.plugins.core.item.itemStack
import com.github.syari.ss.plugins.dependency.crackshot.CrackShotAPI
import com.github.syari.ss.plugins.dependency.crackshotplus.CrackShotPlusAPI
import com.github.syari.ss.plugins.dependency.mythicmobs.MythicMobsAPI
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import com.github.syari.spigot.api.config.type.ConfigDataType as IConfigDataType
import com.github.syari.spigot.api.item.displayName as eDisplayName

sealed class ShopElement {
    open fun give(player: Player) = true
    open fun has(player: Player) = false
    open fun remove(player: Player) {}
    open val display = itemStack(Material.BARRIER, "&cエラー")
    open val targetText = ""
    open val needsText = ""

    class Jump(
        private val id: String,
        private val type: Material,
        private val model: Int?
    ) : ShopElement() {
        override fun give(player: Player) = Shop.get(id)?.openShop(player)?.run { false } ?: true

        override val display by lazy { itemStack(type, "&6${Shop.get(id)?.name}", customModelData = model) }
        override val targetText = "クリックで開く"
    }

    sealed class Item : ShopElement() {
        open val item: ItemStack? = null

        override val display by lazy {
            item?.clone()?.apply {
                eDisplayName = "&b" + (displayName.ifEmpty { null } ?: i18NDisplayName) + " × " + amount
            } ?: itemStack(Material.STONE, "&cエラー")
        }

        override val targetText = "クリックで購入する"

        override fun give(player: Player): Boolean {
            item?.let { player.give(it) }
            return true
        }

        override fun has(player: Player) = item?.let { player.hasItem(it, it.amount) } ?: false

        override fun remove(player: Player) {
            item?.let { player.removeItem(it, it.amount) }
        }

        class Minecraft(
            type: Material,
            amount: Int
        ) : Item() {
            override val item = itemStack(type, amount = amount)
            override val needsText by lazy { "${item.i18NDisplayName} × $amount" }
        }

        class Custom(
            override val item: ItemStack,
            amount: Int
        ) : Item() {
            override val needsText by lazy { "${item.displayName} × $amount" }
        }
    }

    object UnAvailable : ShopElement()

    object ConfigDataType : IConfigDataType<List<ShopElement>> {
        override val typeName = "ShopElement"

        override fun get(config: CustomConfig, path: String, notFoundError: Boolean): List<ShopElement>? {
            return config.get(path, IConfigDataType.StringList)?.map { line ->
                val split = line.split("\\s+".toRegex())
                when (val elementType = split[0].toLowerCase()) {
                    "jump" -> {
                        split.getOrNull(1)?.let { id ->
                            val (type, model) = split.getOrNull(2)?.let {
                                val (typeName, model) = if (it.contains(':')) {
                                    val s = it.split(':')
                                    s[0] to s[1].toIntOrNull()
                                } else {
                                    it to null
                                }
                                val type = Material.getMaterial(typeName.toUpperCase()) ?: run {
                                    config.nullError(path, "Material($it)")
                                    null
                                }
                                type to model
                            } ?: (Material.COMPASS to null)
                            type?.let { Jump(id, type, model) }
                        } ?: UnAvailable
                    }
                    "mc" -> {
                        split.getOrNull(1)?.let { Material.getMaterial(it) }?.let { type ->
                            val amount = split.getOrNull(2)?.let {
                                it.toIntOrNull() ?: run {
                                    config.nullError(path, "Int($it)")
                                    null
                                }
                            } ?: 1
                            Item.Minecraft(type, amount)
                        } ?: run {
                            config.nullError(path, "Material(${split.getOrNull(1)})")
                            UnAvailable
                        }
                    }
                    "cs", "csp", "mm" -> {
                        split.getOrNull(1)?.let { id ->
                            val amount = split.getOrNull(2)?.let {
                                it.toIntOrNull() ?: run {
                                    config.nullError(path, "Int($it)")
                                    null
                                }
                            } ?: 1
                            when (elementType) {
                                "cs" -> {
                                    CrackShotAPI.getItem(id)
                                }
                                "csp" -> {
                                    CrackShotPlusAPI.getAttachment(id)
                                }
                                "mm" -> {
                                    MythicMobsAPI.getItem(id)
                                }
                                else -> error("Unreachable")
                            }?.let {
                                Item.Custom(it.asQuantity(amount), amount)
                            } ?: run {
                                config.nullError(path, "String($id)")
                                UnAvailable
                            }
                        } ?: run {
                            config.nullError(path, "String(${split.getOrNull(1)})")
                            UnAvailable
                        }
                    }
                    else -> UnAvailable
                }
            }
        }

        override fun set(config: CustomConfig, path: String, value: List<ShopElement>?) {
            throw UnsupportedOperationException()
        }
    }
}
