package com.github.syari.ss.plugins.core.sound

import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.entity.Entity

/**
 * サウンドデータ
 */
class CustomSound internal constructor(
    private val type: Sound,
    private val volume: Float,
    private val pitch: Float,
    private val category: SoundCategory
) {
    /**
     * 再生します
     * @param location 場所
     */
    fun play(location: Location) {
        location.world.playSound(location, type, category, volume, pitch)
    }

    /**
     * 再生します
     * @param entity 場所
     */
    fun play(entity: Entity) {
        play(entity.location)
    }

    /**
     * 繰り返し再生します
     * @param location 場所
     * @param number 繰り返し回数
     */
    fun repeat(
        location: Location,
        number: Int
    ) {
        for (i in 0 until number) {
            play(location)
        }
    }
}