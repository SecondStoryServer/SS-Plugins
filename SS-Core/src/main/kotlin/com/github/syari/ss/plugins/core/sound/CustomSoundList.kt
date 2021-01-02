package com.github.syari.ss.plugins.core.sound

import com.github.syari.ss.plugins.core.scheduler.CreateScheduler.runListWithDelay
import com.github.syari.ss.plugins.core.scheduler.CustomTask
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.plugin.java.JavaPlugin

/**
 * [CustomSound] をまとめたクラス
 */
class CustomSoundList(val plugin: JavaPlugin) {
    private val listWithDelay = mutableMapOf<Long, MutableSet<CustomSound>>()
    private var accumulateDelay = 0L

    /**
     * サウンドを追加します
     * @param sound サウンド
     */
    fun addSound(sound: CustomSound) {
        listWithDelay.getOrPut(accumulateDelay) { mutableSetOf() }.add(sound)
    }

    /**
     * 待機時間を追加します
     * @param delay tick
     */
    fun addDelay(delay: Long) {
        accumulateDelay += delay
    }

    /**
     * 実行します
     * @param run 実行する処理
     */
    private fun run(run: (CustomSound) -> Unit): Set<CustomTask> {
        return plugin.runListWithDelay(listWithDelay, run)
    }

    /**
     * サウンドを再生します
     * @param location 場所
     */
    fun play(location: Location): Set<CustomTask> {
        return run { it.play(location) }
    }

    /**
     * サウンドを再生します
     * @param entity 場所
     */
    fun play(entity: Entity): Set<CustomTask> {
        return run { it.play(entity) }
    }

    /**
     * 合計待機時間を取得します
     * @return [Long]
     */
    fun getRequireTime(): Long {
        return accumulateDelay
    }
}