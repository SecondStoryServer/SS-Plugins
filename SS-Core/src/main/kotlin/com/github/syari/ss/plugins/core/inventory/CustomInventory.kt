package com.github.syari.ss.plugins.core.inventory

import com.github.syari.ss.plugins.core.code.StringEditor.toColor
import com.github.syari.ss.plugins.core.inventory.CreateInventory.menuPlayer
import com.github.syari.ss.plugins.core.item.CustomItemStack
import com.github.syari.ss.plugins.core.player.UUIDPlayer
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

class CustomInventory internal constructor(
    val inventory: Inventory,
    id: List<String>
) {
    private val events = mutableMapOf<Pair<Int, ClickType?>, () -> Unit>()
    internal val id = id.joinToString("-").toColor

    /**
     * クリックイベントキャンセル
     */
    var cancel = true

    /**
     * インベントリイベント
     */
    var onEvent: ((InventoryEvent) -> Unit)? = null

    /**
     * クリックイベント
     */
    var onClick: ((InventoryClickEvent) -> Unit)? = null

    /**
     * クローズイベント
     */
    var onClose: ((InventoryCloseEvent) -> Unit)? = null

    /**
     * アイテム
     */
    var contents: Array<ItemStack>
        set(value) {
            inventory.contents = value
        }
        get() = inventory.contents

    /**
     * @param index 取得するアイテムのインデックス
     */
    fun getItem(index: Int): ItemStack? {
        return if (index in 0 until inventory.size) inventory.getItem(index) else null
    }

    /**
     * 見た目のみのアイテムを配置します
     * @param index アイテムの場所
     * @param material アイテムタイプ
     * @param customModelData カスタムモデルデータ
     */
    fun item(
        index: Iterable<Int>,
        material: Material,
        customModelData: Int? = null
    ) {
        val item = CustomItemStack.create(material, "").apply {
            this.customModelData = customModelData
        }.toOneItemStack
        index.forEach {
            item(it, item)
        }
    }

    /**
     * 見た目のみのアイテムを配置します
     * @param index アイテムの場所
     * @param material アイテムタイプ
     * @param customModelData カスタムモデルデータ
     */
    fun item(
        vararg index: Int,
        material: Material,
        customModelData: Int? = null
    ) {
        item(index.toList(), material, customModelData)
    }

    /**
     * 見た目のみのアイテムを配置します
     * @param index アイテムの場所
     * @param material アイテムタイプ
     * @param customModelData カスタムモデルデータ
     */
    fun item(
        index: IntRange,
        material: Material,
        customModelData: Int? = null
    ) {
        item(index.toList(), material, customModelData)
    }

    /**
     * @param index アイテムの場所
     * @param item アイテム
     * @return [ClickEvent]
     */
    fun item(
        index: Int,
        item: ItemStack
    ): ClickEvent {
        return if (index in 0 until inventory.size) {
            inventory.setItem(index, item)
            ClickEvent(this, listOf(index))
        } else {
            ClickEvent(this, listOf())
        }
    }

    /**
     * @param index アイテムの場所
     * @param item アイテム
     * @return [ClickEvent]
     */
    fun item(
        index: Int,
        item: CustomItemStack
    ): ClickEvent {
        return item(index, item.toOneItemStack)
    }

    /**
     * ```
     * item(inventory.firstEmpty(), item)
     * ```
     * @param item アイテム
     * @return [ClickEvent]
     */
    fun item(item: CustomItemStack): ClickEvent {
        return item(inventory.firstEmpty(), item)
    }

    /**
     * @param index アイテムの場所
     * @param material アイテムタイプ
     * @param display アイテム名
     * @param lore アイテムの説明文
     * @param amount アイテムの数
     * @param customModelData カスタムモデルデータ
     * @param shine エンチャントを付与する default: false
     * @return [ClickEvent]
     */
    fun item(
        index: Int,
        material: Material,
        display: String,
        lore: Collection<String>,
        amount: Int = 1,
        customModelData: Int? = null,
        shine: Boolean = false
    ): ClickEvent {
        return item(listOf(index), material, display, lore.toList(), amount, customModelData, shine)
    }

    /**
     * @param index アイテムの場所
     * @param material アイテムタイプ
     * @param display アイテム名
     * @param lore アイテムの説明文
     * @param amount アイテムの数
     * @param customModelData カスタムモデルデータ
     * @param shine エンチャントを付与する default: false
     * @return [ClickEvent]
     */
    fun item(
        index: Iterable<Int>,
        material: Material,
        display: String,
        lore: Collection<String>,
        amount: Int = 1,
        customModelData: Int? = null,
        shine: Boolean = false
    ): ClickEvent {
        return ClickEvent(
            this,
            index.map {
                item(
                    it,
                    CustomItemStack.create(
                        material, display, *lore.toTypedArray(), customModelData = customModelData, amount = amount
                    ).apply {
                        if (shine) {
                            addEnchant(Enchantment.DURABILITY, 0)
                            addItemFlag(ItemFlag.HIDE_ENCHANTS)
                        }
                    }
                )
            }.flatMap { it.slot }
        )
    }

    /**
     * @param index アイテムの場所
     * @param material アイテムタイプ
     * @param display アイテム名
     * @param lore アイテムの説明文
     * @param amount アイテムの数
     * @param customModelData カスタムモデルデータ
     * @param shine エンチャントを付与する default: false
     * @return [ClickEvent]
     */
    fun item(
        index: Int,
        material: Material,
        display: String,
        vararg lore: String,
        amount: Int = 1,
        customModelData: Int? = null,
        shine: Boolean = false
    ): ClickEvent {
        return item(listOf(index), material, display, lore.toList(), amount, customModelData, shine)
    }

    /**
     * @param index アイテムの場所
     * @param material アイテムタイプ
     * @param display アイテム名
     * @param lore アイテムの説明文
     * @param amount アイテムの数
     * @param customModelData カスタムモデルデータ
     * @param shine エンチャントを付与する default: false
     * @return [ClickEvent]
     */
    fun item(
        index: Iterable<Int>,
        material: Material,
        display: String,
        vararg lore: String,
        amount: Int = 1,
        customModelData: Int? = null,
        shine: Boolean = false
    ): ClickEvent {
        return item(index, material, display, lore.toList(), amount, customModelData, shine)
    }

    /**
     * アイテム単位でクリックイベントを設定します
     */
    data class ClickEvent(
        val inventory: CustomInventory,
        val slot: List<Int>
    ) {
        /**
         * @param clickType クリックタイプ
         * @param action クリックタイプが一致した時に実行する処理
         * @return [ClickEvent]
         */
        fun event(
            vararg clickType: ClickType,
            action: () -> Unit
        ): ClickEvent {
            clickType.forEach { type ->
                addEvent(type, action)
            }
            return this
        }

        /**
         * @param action アイテムがクリックされた時に実行する処理
         * @return [ClickEvent]
         */
        fun event(action: () -> Unit): ClickEvent {
            addEvent(null, action)
            return this
        }

        private fun addEvent(
            clickType: ClickType?,
            action: () -> Unit
        ) {
            slot.forEach {
                inventory.events[it to clickType] = action
            }
        }
    }

    /**
     * プレイヤーにインベントリを開かせます
     * @param player 対象プレイヤー
     */
    fun open(player: Player): CustomInventory {
        player.openInventory(inventory)
        val uuidPlayer = UUIDPlayer(player)
        uuidPlayer.menuPlayer = InventoryPlayerData(id, cancel, onEvent, onClick, onClose, events)
        return this
    }
}
