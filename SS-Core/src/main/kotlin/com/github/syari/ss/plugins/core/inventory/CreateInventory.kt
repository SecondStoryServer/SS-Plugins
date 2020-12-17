package com.github.syari.ss.plugins.core.inventory

import com.github.syari.ss.plugins.core.Main.Companion.corePlugin
import com.github.syari.ss.plugins.core.code.StringEditor.toColor
import com.github.syari.ss.plugins.core.inventory.CreateInventory.runWithId
import com.github.syari.ss.plugins.core.player.UUIDPlayer
import com.github.syari.ss.plugins.core.scheduler.CreateScheduler.runLater
import org.bukkit.Bukkit.createInventory
import org.bukkit.OfflinePlayer
import org.bukkit.entity.HumanEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory

object CreateInventory: Listener {
    @EventHandler
    fun onInventoryClick(e: InventoryClickEvent) {
        val player = e.whoClicked as Player
        val uuidPlayer = UUIDPlayer(player)
        uuidPlayer.menuPlayer?.run {
            if (cancel) {
                e.isCancelled = true
            }
            if (e.inventory == e.clickedInventory) {
                runEvent(e.slot, e.click)
            }
            onClick(e)
            if (e.click == ClickType.MIDDLE && player.isOp) {
                val item = e.currentItem
                if (item != null) {
                    e.isCancelled = true
                    player.inventory.addItem(item.asQuantity(64))
                }
            }
        }
    }

    @EventHandler
    fun onInventoryClose(e: InventoryCloseEvent) {
        val player = e.player as Player
        val uuidPlayer = UUIDPlayer(player)
        uuidPlayer.menuPlayer?.run {
            onClose(e)
            uuidPlayer.menuPlayer = null
            runLater(corePlugin, 5) {
                player.updateInventory()
            }
        }
    }

    /**
     * @param inventory [Inventory]
     * @param id インベントリのID
     * @return [CustomInventory]
     */
    fun inventory(
        inventory: Inventory, vararg id: String
    ): CustomInventory {
        return CustomInventory(inventory, id.toList())
    }

    /**
     * @param display インベントリのタイトル
     * @param type インベントリの種類
     * @param id インベントリのID
     * @return [CustomInventory]
     */
    fun inventory(
        display: String, type: InventoryType, vararg id: String
    ): CustomInventory {
        return inventory(createInventory(null, type, display.toColor), *id)
    }

    /**
     * @param display インベントリのタイトル
     * @param type インベントリの種類
     * @param id インベントリのID
     * @param run インベントリに対して実行する処理
     * @return [CustomInventory]
     */
    fun inventory(
        display: String, type: InventoryType, vararg id: String, run: CustomInventory.() -> Unit
    ): CustomInventory {
        return inventory(display, type, *id).apply(run)
    }

    /**
     * @param display インベントリのタイトル
     * @param line インベントリの行数 default: 3
     * @param id インベントリのID
     * @param run インベントリに対して実行する処理
     * @return [CustomInventory]
     */
    fun inventory(
        display: String, line: Int = 3, vararg id: String, run: CustomInventory.() -> Unit
    ): CustomInventory {
        return inventory(createInventory(null, (if (line in 1..6) line else 3) * 9, display.toColor), *id).apply(run)
    }

    private val menuPlayers = mutableMapOf<UUIDPlayer, InventoryPlayerData>()

    /**
     * インベントリのプレイヤーデータ
     */
    internal var UUIDPlayer.menuPlayer
        get() = menuPlayers[this]
        set(value) {
            val uuidPlayer = this
            if (value != null) {
                menuPlayers[uuidPlayer] = value
            } else {
                menuPlayers.remove(uuidPlayer)
            }
        }

    /**
     * プレイヤーが [id] から始まるインベントリを開いているかどうかを取得します
     * @param player プレイヤー
     * @param id インベントリのID
     */
    fun isOpenInventory(
        player: OfflinePlayer, vararg id: String
    ): Boolean {
        return UUIDPlayer(player).menuPlayer?.isOpenInventory(id) ?: false
    }

    /**
     * プレイヤーが [id] から始まるインベントリを開いているかどうかを取得します
     * @param uuidPlayer プレイヤー
     * @param id インベントリのID
     */
    fun isOpenInventory(
        uuidPlayer: UUIDPlayer, vararg id: String
    ): Boolean {
        return uuidPlayer.menuPlayer?.isOpenInventory(id) ?: false
    }

    /**
     * プレイヤーが指定のインベントリを開いているかどうかを取得します
     * @param player プレイヤー
     * @param inventory インベントリ
     */
    fun isOpenInventory(
        player: OfflinePlayer, inventory: CustomInventory
    ): Boolean {
        return isOpenInventory(player, inventory.id)
    }

    /**
     * プレイヤーが指定のインベントリを開いているかどうかを取得します
     * @param uuidPlayer プレイヤー
     * @param inventory インベントリ
     */
    fun isOpenInventory(
        uuidPlayer: UUIDPlayer, inventory: CustomInventory
    ): Boolean {
        return isOpenInventory(uuidPlayer, inventory.id)
    }

    /**
     * プレイヤーがインベントリを開いているかどうかを取得します
     * @param player プレイヤー
     */
    fun isOpenInventory(player: OfflinePlayer): Boolean {
        return isOpenInventory(UUIDPlayer(player))
    }

    /**
     * プレイヤーがインベントリを開いているかどうかを取得します
     * @param uuidPlayer プレイヤー
     */
    fun isOpenInventory(uuidPlayer: UUIDPlayer): Boolean {
        return uuidPlayer.menuPlayer != null
    }

    /**
     * [id] から始まる インベントリID を持つプレイヤーに処理を行います
     * @param id インベントリのID
     * @param run プレイヤーに対して実行する処理
     */
    fun runWithId(
        vararg id: String, run: (Player) -> Unit
    ) {
        menuPlayers.forEach { (uuidPlayer, playerData) ->
            if (playerData.isOpenInventory(id)) {
                val player = uuidPlayer.player ?: return@forEach
                run.invoke(player)
            }
        }
    }

    /**
     * 該当プレイヤーに再度インベントリを開かせます
     * @see runWithId
     * @param id インベントリのID
     * @param inventory プレイヤーに開かせるインベントリ
     */
    fun reopen(
        vararg id: String, inventory: CustomInventory
    ) {
        runWithId(*id) {
            inventory.open(it)
        }
    }

    /**
     * 該当プレイヤーに再度インベントリを開かせます
     * @see runWithId
     * @param id インベントリのID
     * @param run プレイヤーに対して実行する処理
     */
    fun reopen(
        vararg id: String, run: (Player) -> CustomInventory
    ) {
        runWithId(*id) {
            run.invoke(it).open(it)
        }
    }

    /**
     * 該当プレイヤーのインベントリを閉じます
     * @see runWithId
     * @param id インベントリのID
     */
    fun close(vararg id: String) {
        runWithId(*id) {
            it.closeInventory()
        }
    }

    /**
     * @param humanEntity インベントリを閉じるプレイヤー
     */
    fun close(humanEntity: HumanEntity) {
        humanEntity.closeInventory()
    }
}