package com.github.syari.ss.plugins.core.bossBar

import com.github.syari.ss.plugins.core.code.OnDisable
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

object CreateBossBar: Listener, OnDisable {
    internal val barList = mutableListOf<CustomBossBar>()

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
    fun createBossBar(
        title: String,
        color: BarColor,
        style: BarStyle,
        public: Boolean = false
    ) =
        CustomBossBar(title, color, style, public)
}