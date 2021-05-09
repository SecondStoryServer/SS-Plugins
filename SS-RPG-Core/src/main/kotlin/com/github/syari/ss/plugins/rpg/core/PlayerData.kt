package com.github.syari.ss.plugins.rpg.core

import com.github.syari.spigot.api.config.config
import com.github.syari.spigot.api.config.type.ConfigDataType
import com.github.syari.spigot.api.uuid.UUIDPlayer
import com.github.syari.ss.plugins.core.console
import com.github.syari.ss.plugins.rpg.core.Main.Companion.plugin

/**
 * プレイヤーデータ
 * @param uuidPlayer プレイヤー
 */
data class PlayerData(val uuidPlayer: UUIDPlayer) {
    /**
     * コンフィグパス
     */
    object Path {
        const val Exp = "exp"
    }

    private val config = plugin.config(console, "player/$uuidPlayer.yml")

    /**
     * プレイヤー経験値
     */
    var exp = config.get(Path.Exp, ConfigDataType.Long, 0L)
        private set

    /**
     * プレイヤーレベル
     */
    val level
        get() = LevelCalculator.totalExpToLevel(exp)

    /**
     * プレイヤーデータに変更を加える
     */
    fun edit(action: Editor.() -> Unit) {
        action(Editor(this))
        config.set(Path.Exp, ConfigDataType.Long, exp)
        config.save()
    }

    /**
     * 変更を加えるためのクラス
     */
    class Editor(data: PlayerData) {
        /**
         * 経験値
         */
        var exp by data::exp
    }
}
