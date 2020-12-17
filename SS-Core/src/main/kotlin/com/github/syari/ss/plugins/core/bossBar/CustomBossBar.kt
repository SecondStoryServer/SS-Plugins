package com.github.syari.ss.plugins.core.bossBar

import com.github.syari.ss.plugins.core.Main.Companion.corePlugin
import com.github.syari.ss.plugins.core.bossBar.CreateBossBar.barList
import com.github.syari.ss.plugins.core.code.StringEditor.toColor
import org.bukkit.OfflinePlayer
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.boss.BossBar
import org.bukkit.entity.Player

/**
 * ボスバー
 * @see CreateBossBar.createBossBar
 */
class CustomBossBar internal constructor(
    title: String, color: BarColor, style: BarStyle, private val public: Boolean
) {
    private val bar: BossBar = corePlugin.server.createBossBar(title.toColor, color, style)

    init {
        if (public) {
            corePlugin.server.onlinePlayers.forEach { bar.addPlayer(it) }
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
     * @see CreateBossBar.onJoin
     */
    internal fun onLogin(player: Player) {
        if (public) {
            bar.addPlayer(player)
        } else if (addOnLogin.contains(player)) {
            bar.addPlayer(player)
            addOnLogin.remove(player)
        }
    }

    /**
     * 自動で実行されます
     * @see CreateBossBar.onQuit
     */
    internal fun onLogout(player: Player) {
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