package com.github.syari.ss.plugins.core.particle

import com.destroystokyo.paper.ParticleBuilder
import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.entity.Entity
import org.bukkit.inventory.ItemStack

/**
 * パーティクルデータ
 */
sealed class CustomParticle(
    type: Particle,
    count: Int,
    speed: Double,
    offsetX: Double,
    offsetY: Double,
    offsetZ: Double
) {
    /**
     * @see ParticleBuilder
     */
    val builder = ParticleBuilder(type).count(count).offset(offsetX, offsetY, offsetZ).extra(speed)

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
    fun spawn(location: Location) {
        builder.location(location).spawn()
    }

    /**
     * パーティクルを生成します
     * @param entity 場所
     */
    fun spawn(entity: Entity) {
        spawn(entity.location)
    }

    /**
     * 基本パーティクル
     * @param type パーティクルの種類
     * @param count カウント
     * @param speed 速度
     * @param offsetX Xのずれ default: 0.0
     * @param offsetY Yのずれ default: 0.0
     * @param offsetZ Zのずれ default: 0.0
     */
    class Normal(
        type: Particle,
        count: Int,
        speed: Double,
        offsetX: Double = 0.0,
        offsetY: Double = 0.0,
        offsetZ: Double = 0.0
    ) : CustomParticle(type, count, speed, offsetX, offsetY, offsetZ)

    /**
     * アイテムクラック
     * @param material アイテムタイプ
     * @param count カウント
     * @param speed 速度
     * @param offsetX Xのずれ default: 0.0
     * @param offsetY Yのずれ default: 0.0
     * @param offsetZ Zのずれ default: 0.0
     */
    class ItemCrack(
        material: Material,
        count: Int,
        speed: Double,
        offsetX: Double = 0.0,
        offsetY: Double = 0.0,
        offsetZ: Double = 0.0
    ) : CustomParticle(Particle.ITEM_CRACK, count, speed, offsetX, offsetY, offsetZ) {
        init {
            builder.data(ItemStack(material))
        }
    }

    /**
     * ブロッククラック
     * @param material ブロックタイプ
     * @param count カウント
     * @param speed 速度
     * @param offsetX Xのずれ default: 0.0
     * @param offsetY Yのずれ default: 0.0
     * @param offsetZ Zのずれ default: 0.0
     */
    class BlockCrack(
        material: Material,
        count: Int,
        speed: Double,
        offsetX: Double = 0.0,
        offsetY: Double = 0.0,
        offsetZ: Double = 0.0
    ) : CustomParticle(Particle.BLOCK_CRACK, count, speed, offsetX, offsetY, offsetZ) {
        init {
            builder.data(material.createBlockData())
        }
    }

    /**
     * ブロックダスト
     * @param material ブロックタイプ
     * @param count カウント
     * @param speed 速度
     * @param offsetX Xのずれ default: 0.0
     * @param offsetY Yのずれ default: 0.0
     * @param offsetZ Zのずれ default: 0.0
     */
    class BlockDust(
        material: Material,
        count: Int,
        speed: Double,
        offsetX: Double = 0.0,
        offsetY: Double = 0.0,
        offsetZ: Double = 0.0
    ) : CustomParticle(Particle.BLOCK_DUST, count, speed, offsetX, offsetY, offsetZ) {
        init {
            builder.data(material.createBlockData())
        }
    }

    /**
     * フォーリングダスト
     * @param material ブロックタイプ
     * @param count カウント
     * @param speed 速度
     * @param offsetX Xのずれ default: 0.0
     * @param offsetY Yのずれ default: 0.0
     * @param offsetZ Zのずれ default: 0.0
     */
    class FallingDust(
        material: Material,
        count: Int,
        speed: Double,
        offsetX: Double = 0.0,
        offsetY: Double = 0.0,
        offsetZ: Double = 0.0
    ) : CustomParticle(Particle.FALLING_DUST, count, speed, offsetX, offsetY, offsetZ) {
        init {
            builder.data(material.createBlockData())
        }
    }

    /**
     * レッドストーン
     * @param color 色
     * @param count カウント
     * @param speed 速度
     * @param offsetX Xのずれ default: 0.0
     * @param offsetY Yのずれ default: 0.0
     * @param offsetZ Zのずれ default: 0.0
     */
    class RedStone(
        color: Color,
        count: Int,
        speed: Double,
        offsetX: Double = 0.0,
        offsetY: Double = 0.0,
        offsetZ: Double = 0.0
    ) : CustomParticle(Particle.REDSTONE, count, speed, offsetX, offsetY, offsetZ) {
        init {
            builder.color(color)
        }

        /**
         * レッドストーン
         * @param red R
         * @param blue G
         * @param green B
         * @param count カウント
         * @param speed 速度
         * @param offsetX Xのずれ default: 0.0
         * @param offsetY Yのずれ default: 0.0
         * @param offsetZ Zのずれ default: 0.0
         */
        constructor(
            red: Int,
            green: Int,
            blue: Int,
            count: Int,
            speed: Double,
            offsetX: Double = 0.0,
            offsetY: Double = 0.0,
            offsetZ: Double = 0.0
        ) : this(
            Color.fromRGB(convertColor(red), convertColor(green), convertColor(blue)), count, speed, offsetX, offsetY, offsetZ
        )

        companion object {
            /**
             * ランダムなRGBコードを取得します
             */
            inline val randomColor get() = (0..255).random()

            /**
             * 色を 0..255 に丸め込みます
             * @param value 色
             * @return [Int]
             */
            private fun convertColor(value: Int): Int {
                return when {
                    value < 0 -> 0
                    255 < value -> 255
                    else -> value
                }
            }

            /**
             * ランダムな色のレッドストーン
             * @param count カウント
             * @param speed 速度
             * @param offsetX Xのずれ default: 0.0
             * @param offsetY Yのずれ default: 0.0
             * @param offsetZ Zのずれ default: 0.0
             * @return [RedStone]
             */
            fun random(
                count: Int,
                speed: Double,
                offsetX: Double = 0.0,
                offsetY: Double = 0.0,
                offsetZ: Double = 0.0
            ): RedStone {
                return RedStone(randomColor, randomColor, randomColor, count, speed, offsetX, offsetY, offsetZ)
            }
        }
    }
}
