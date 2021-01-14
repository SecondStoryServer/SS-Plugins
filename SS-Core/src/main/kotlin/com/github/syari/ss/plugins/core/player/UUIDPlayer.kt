package com.github.syari.ss.plugins.core.player

import com.github.syari.ss.plugins.core.code.UUIDConverter.toUUIDOrNull
import org.bukkit.Bukkit.getOfflinePlayer
import org.bukkit.Bukkit.getPlayer
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import java.util.UUID

/**
 * [UUID] としてプレイヤーを保存しておく
 */
data class UUIDPlayer(private val uniqueId: UUID) {
    constructor(player: OfflinePlayer) : this(player.uniqueId)

    /**
     * プレイヤーに変換します
     */
    val player get(): Player? = getPlayer(uniqueId)

    /**
     * オフラインプレイヤーに変換します
     */
    val offlinePlayer get(): OfflinePlayer? = getOfflinePlayer(uniqueId)

    /**
     * 名前を取得します
     */
    val name get() = offlinePlayer?.name

    /**
     * オンラインか取得します
     */
    val isOnline get() = offlinePlayer?.isOnline ?: false

    /**
     * UUIDを文字列として取得します
     */
    override fun toString() = uniqueId.toString()

    companion object {
        /**
         * 文字列をUUIDPlayerに変換します
         * 失敗したら null を返します
         * @param uuid UUID
         * @return [UUIDPlayer]?
         */
        fun create(uuid: String): UUIDPlayer? {
            return uuid.toUUIDOrNull()?.let { UUIDPlayer(it) }
        }
    }
}
