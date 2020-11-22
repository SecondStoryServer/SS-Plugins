package com.github.syari.ss.plugins.core.config.dataType

import com.github.syari.ss.plugins.core.config.CustomConfig
import com.github.syari.ss.plugins.core.sound.CustomSound
import com.github.syari.ss.plugins.core.sound.CustomSoundList
import org.bukkit.Sound
import org.bukkit.SoundCategory

object ConfigSoundDataType: ConfigDataType<CustomSoundList> {
    override val typeName = "CustomSoundList"

    override fun get(
        config: CustomConfig,
        path: String,
        notFoundError: Boolean
    ): CustomSoundList? {
        val getList = config.get(path, ConfigDataType.STRINGLIST, notFoundError) ?: return null
        return CustomSoundList().apply {
            getList.forEachIndexed { index, line ->
                val split = line.split("-")
                when (val size = split.size) {
                    2 -> {
                        if (split[0].toLowerCase() == "delay") {
                            val delay = split[1].toLongOrNull()
                            if (delay != null) {
                                addDelay(delay)
                            } else {
                                config.nullError("$path:$index", "Long(${split[1]})")
                                return@forEachIndexed
                            }
                        }
                    }
                    3, 4 -> {
                        val rawType = split[0].toUpperCase()
                        val type = Sound.values().firstOrNull { rawType == it.name }
                        if (type == null) {
                            config.nullError("$path:$index", "Sound($rawType)")
                            return@forEachIndexed
                        }
                        val volume = split[1].toFloatOrNull()
                        if (volume == null) {
                            config.nullError("$path:$index", "Float(${split[1]})")
                            return@forEachIndexed
                        }
                        val pitch = split[2].toFloatOrNull()
                        if (pitch == null) {
                            config.nullError("$path:$index", "Float(${split[2]})")
                            return@forEachIndexed
                        }
                        val loop = if (size == 3) {
                            1
                        } else {
                            val get = split[3].toIntOrNull()
                            if (get == null) {
                                config.nullError("$path:$index", "Int(${split[3]})")
                                return@forEachIndexed
                            }
                            get
                        }
                        for (i in 0 until loop) {
                            addSound(CustomSound(type, volume, pitch, SoundCategory.MASTER))
                        }
                    }
                    else -> config.formatMismatchError("$path:$index")
                }
            }
        }
    }
}