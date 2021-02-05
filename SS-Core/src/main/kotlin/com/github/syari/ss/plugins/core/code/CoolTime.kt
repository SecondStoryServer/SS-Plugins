@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.github.syari.ss.plugins.core.code

import com.github.syari.ss.plugins.core.scheduler.CreateScheduler.runLater
import org.bukkit.plugin.java.JavaPlugin

/**
 * 指定データ型のクールタイム
 * @param T データ型
 * @param plugin スケジューラに使用するプラグイン
 */
class CoolTime<T>(private val plugin: JavaPlugin) {
    companion object {
        fun <T> JavaPlugin.coolTime() = CoolTime<T>(this)
    }

    private val coolTimeList = mutableSetOf<T>()

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
        value: T,
        coolTime: Long
    ) {
        val success = coolTimeList.add(value)
        if (!success) return
        plugin.runLater(coolTime) {
            coolTimeList.remove(value)
        }
    }
}
