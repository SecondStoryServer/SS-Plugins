@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.github.syari.ss.plugins.core.bossBar

import com.github.syari.ss.plugins.core.Main.Companion.plugin
import com.github.syari.ss.plugins.core.bossBar.CustomBossBar.Companion.bossBar
import com.github.syari.ss.plugins.core.code.OnDisable
import com.github.syari.ss.plugins.core.code.StringEditor.toColor
import org.bukkit.OfflinePlayer
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.boss.BossBar
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

/**
 * @see bossBar
 */
class CustomBossBar internal constructor(
    title: String,
    color: BarColor,
    style: BarStyle,
    private val public: Boolean
) {
    companion object : Listener, OnDisable {
        private val barList = mutableListOf<CustomBossBar>()

        /**
         * バーを自動で表示します
         * @see [CustomBossBar.onLogin]
         */
        @EventHandler
        fun onJoin(e: PlayerJoinEvent) {
            val player = e.player
            barList.forEach { it.onLogin(player) }
        }

        @EventHandler
        fun onQuit(e: PlayerQuitEvent) {
            val player = e.player
            barList.forEach { it.onLogout(player) }
        }

        /**
         * プラグインを無効になった時に全てのプレイヤーのバーを非表示にします
         */
        override fun onDisable() {
            barList.forEach { it.clearPlayer() }
        }

        /**
         * ボスバーを作成します
         * @param title 一番上に表示される文字
         * @param color バーの色
         * @param style バーの見た目
         * @param public 全てのプレイヤーに表示するか default: false
         * @return [CustomBossBar]
         */
        fun bossBar(
            title: String,
            color: BarColor,
            style: BarStyle,
            public: Boolean = false
        ) = CustomBossBar(title, color, style, public)
    }

    private val bar: BossBar = plugin.server.createBossBar(title.toColor, color, style)

    init {
        if (public) {
            plugin.server.onlinePlayers.forEach { bar.addPlayer(it) }
        }
        barList.add(this)
    }

    private val addOnLogin = mutableListOf<OfflinePlayer>()

    /**
     * 登録されたプレイヤーか取得します
     * @param player 対象プレイヤー
     * @return [Boolean]
     */
    fun containsPlayer(player: OfflinePlayer): Boolean {
        if (public) return true
        return if (player is Player) bar.players.contains(player) else addOnLogin.contains(player)
    }

    /**
     * プレイヤーを登録します
     * @param player 対象プレイヤー
     */
    fun addPlayer(player: OfflinePlayer) {
        if (public || containsPlayer(player)) return
        if (player is Player) bar.addPlayer(player)
        else addOnLogin.add(player)
    }

    /**
     * プレイヤーの登録を解除します
     * @param player 対象プレイヤー
     */
    fun removePlayer(player: OfflinePlayer) {
        if (public || !containsPlayer(player)) return
        if (player is Player) bar.removePlayer(player)
        else addOnLogin.remove(player)
    }

    /**
     * 全プレイヤーの登録を解除します
     */
    fun clearPlayer() {
        bar.removeAll()
    }

    /**
     * バーのタイトル
     */
    var title
        get() = bar.title
        set(value) {
            bar.setTitle(value.toColor)
        }

    /**
     * 自動で実行されます
     * @see onJoin
     */
    private fun onLogin(player: Player) {
        if (public) {
            bar.addPlayer(player)
        } else if (addOnLogin.contains(player)) {
            bar.addPlayer(player)
            addOnLogin.remove(player)
        }
    }

    /**
     * 自動で実行されます
     * @see onQuit
     */
    private fun onLogout(player: Player) {
        if (public) return
        bar.removePlayer(player)
        addOnLogin.add(player)
    }

    /**
     * バーのゲージ(0~1.0)
     */
    var progress
        get() = bar.progress
        set(value) {
            bar.progress = when {
                value < 0.0 -> 0.0
                1.0 < value -> 1.0
                else -> value
            }
        }

    /**
     * バーの一覧から削除します
     */
    fun delete() {
        barList.remove(this)
        clearPlayer()
    }
}
