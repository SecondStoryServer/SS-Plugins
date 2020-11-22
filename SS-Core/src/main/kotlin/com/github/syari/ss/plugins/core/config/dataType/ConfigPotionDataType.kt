package com.github.syari.ss.plugins.core.config.dataType

import com.github.syari.ss.plugins.core.config.CustomConfig
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object ConfigPotionDataType: ConfigDataType<List<PotionEffect>> {
    override val typeName = "List<PotionEffect>"

    override fun get(
        config: CustomConfig,
        path: String,
        notFoundError: Boolean
    ): List<PotionEffect>? {
        val getList = config.get(path, ConfigDataType.STRINGLIST, notFoundError) ?: return null
        return mutableListOf<PotionEffect>().apply {
            getList.forEachIndexed { index, line ->
                val split = line.split("-")
                if (split.size < 3) {
                    return@forEachIndexed config.formatMismatchError("$path:$index")
                }
                val type = PotionEffectType.getByName(split[0])
                        ?: return@forEachIndexed config.nullError("$path:$index", "Potion(${split[0]})")
                val duration = split[1].toIntOrNull()
                        ?: return@forEachIndexed config.typeMismatchError("$path:$index", "Int(${split[1]})")
                val level = split[2].toIntOrNull()
                        ?: return@forEachIndexed config.typeMismatchError("$path:$index", "Int(${split[2]})")
                val particle = split.getOrNull(3)?.toBoolean() ?: true
                PotionEffect(type, duration, level, true, particle)
            }
        }
    }
}