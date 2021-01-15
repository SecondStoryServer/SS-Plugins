package com.github.syari.ss.plugins.core.config.type.data

import com.github.syari.ss.plugins.core.config.CustomConfig
import com.github.syari.ss.plugins.core.config.type.ConfigDataType
import com.github.syari.ss.plugins.core.particle.CustomParticle
import com.github.syari.ss.plugins.core.particle.CustomParticleList
import org.bukkit.Material
import org.bukkit.Particle

object ConfigParticleDataType : ConfigDataType<CustomParticleList> {
    override val typeName = "CustomParticleList"

    override fun get(
        config: CustomConfig,
        path: String,
        notFoundError: Boolean
    ): CustomParticleList? {
        val getList = config.get(path, ConfigDataType.STRINGLIST, notFoundError) ?: return null
        return CustomParticleList(config.plugin).apply {
            getList.forEachIndexed { index, line ->
                val split = line.split("-")
                when (split.size) {
                    2 -> {
                        if (split[0].equals("delay", true)) {
                            val delay = split[1].toLongOrNull()
                            if (delay != null) {
                                addDelay(delay)
                            } else {
                                config.nullError("$path:$index", "Long(${split[1]})")
                            }
                        }
                    }
                    3, 4, 6 -> {
                        val rawType = split[0].toUpperCase()
                        val type = Particle.values().firstOrNull { rawType == it.name } ?: return@forEachIndexed config.nullError("$path:$index", "Particle($rawType)")
                        val lastIndex = split.lastIndex
                        val speed = split[lastIndex - 1].toDoubleOrNull() ?: return@forEachIndexed config.nullError(
                            "$path:$index", "Double(${split[lastIndex - 1]})"
                        )
                        val count = split[lastIndex].toIntOrNull() ?: return@forEachIndexed config.nullError("$path:$index", "Int(${split[lastIndex]})")
                        addParticle(
                            when (type) {
                                Particle.ITEM_CRACK, Particle.BLOCK_CRACK, Particle.BLOCK_DUST, Particle.FALLING_DUST -> {
                                    val material = Material.getMaterial(split[2]) ?: return@forEachIndexed config.nullError(
                                        "$path:$index", "Material(${split[2]})"
                                    )
                                    when (type) {
                                        Particle.ITEM_CRACK -> CustomParticle.ItemCrack(material, count, speed)
                                        Particle.BLOCK_CRACK -> CustomParticle.BlockCrack(material, count, speed)
                                        Particle.BLOCK_DUST -> CustomParticle.BlockDust(material, count, speed)
                                        Particle.FALLING_DUST -> CustomParticle.FallingDust(material, count, speed)
                                        else -> return@forEachIndexed // Unreachable
                                    }
                                }
                                Particle.REDSTONE -> {
                                    try {
                                        val red = split[2].toInt()
                                        val green = split[3].toInt()
                                        val blue = split[4].toInt()
                                        CustomParticle.RedStone(red, blue, green, count, speed)
                                    } catch (ex: NumberFormatException) {
                                        return@forEachIndexed config.nullError(
                                            "$path:$index", "Int(${split[2]}, ${split[3]}, ${split[4]})"
                                        )
                                    }
                                }
                                else -> CustomParticle.Normal(type, count, speed)
                            }
                        )
                    }
                }
            }
        }
    }
}
