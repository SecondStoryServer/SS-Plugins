@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.github.syari.ss.wplugins.core.player

import com.github.syari.ss.wplugins.core.Main.Companion.plugin
import net.md_5.bungee.api.connection.ProxiedPlayer
import java.util.UUID

/**
 * [UUID] としてプレイヤーを保存しておく
 */
data class UUIDPlayer(private val uniqueId: UUID) : Comparable<UUIDPlayer> {
    constructor(player: ProxiedPlayer) : this(player.uniqueId)

    /**
     * プレイヤーに変換します
     */
    val player get(): ProxiedPlayer? = plugin.proxy.getPlayer(uniqueId)

    /**
     * 名前を取得します
     */
    val name get() = player?.name

    /**
     * オンラインか取得します
     */
    val isOnline get() = player != null

    /**
     * [UUID.compareTo]
     */
    override fun compareTo(other: UUIDPlayer) = uniqueId.compareTo(other.uniqueId)

    /**
     * UUIDを文字列として取得します
     */
    override fun toString() = uniqueId.toString()
}
