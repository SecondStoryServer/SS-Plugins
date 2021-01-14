package com.github.syari.ss.plugins.core.particle

import com.github.syari.ss.plugins.core.scheduler.CreateScheduler.runListWithDelay
import com.github.syari.ss.plugins.core.scheduler.CustomTask
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.plugin.java.JavaPlugin

/**
 * [CustomParticle] をまとめたクラス
 */
class CustomParticleList(val plugin: JavaPlugin) {
    private val listWithDelay = mutableMapOf<Long, MutableSet<CustomParticle>>()
    private var accumulateDelay = 0L

    /**
     * パーティクルを追加します
     * @param particle パーティクル
     */
    fun addParticle(particle: CustomParticle) {
        listWithDelay.getOrPut(accumulateDelay, ::mutableSetOf).add(particle)
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
     * @param action 実行する処理
     */
    private fun run(action: (CustomParticle) -> Unit): Set<CustomTask> {
        return plugin.runListWithDelay(listWithDelay, action)
    }

    /**
     * パーティクルを生成します
     * @param location 場所
     */
    fun spawn(location: Iterable<Location>) {
        location.forEach {
            spawn(it)
        }
    }

    /**
     * パーティクルを生成します
     * @param location 場所
     */
    fun spawn(location: Location): Set<CustomTask> {
        return run { it.spawn(location) }
    }

    /**
     * パーティクルを生成します
     * @param entity 場所
     */
    fun spawn(entity: Entity): Set<CustomTask> {
        return run { it.spawn(entity) }
    }

    /**
     * 合計待機時間を取得します
     * @return [Long]
     */
    fun getRequireTime(): Long {
        return accumulateDelay
    }
}