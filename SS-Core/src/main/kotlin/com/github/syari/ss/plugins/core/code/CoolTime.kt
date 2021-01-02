package com.github.syari.ss.plugins.core.code

import com.github.syari.ss.plugins.core.scheduler.CreateScheduler.runLater
import org.bukkit.plugin.java.JavaPlugin

/**
 * 指定データ型のクールタイム
 * @param T データ型
 * @param plugin スケジューラに使用するプラグイン
 */
class CoolTime<T>(private val plugin: JavaPlugin) {
    private val coolTimeList = mutableSetOf<T>()

    /**
     * 使用可能かどうか(クールタイム中ではないか)を取得します
     * @param value データ
     * @return [Boolean]
     */
    fun isAvailable(value: T): Boolean {
        return !contains(value)
    }

    /**
     * クールタイム中かどうかを取得します
     * @param value データ
     * @return [Boolean]
     */
    fun contains(value: T): Boolean {
        return coolTimeList.contains(value)
    }

    /**
     * クールタイム状態に変更します
     * @param value データ
     * @param coolTime 削除されるまでの時間 Tick
     */
    fun add(
        value: T, coolTime: Long
    ) {
        val success = coolTimeList.add(value)
        if (!success) return
        plugin.runLater(coolTime) {
            coolTimeList.remove(value)
        }
    }
}