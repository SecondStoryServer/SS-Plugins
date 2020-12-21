package com.github.syari.ss.plugins.core.config.dataType

import com.github.syari.ss.plugins.core.config.CustomConfig
import com.github.syari.ss.plugins.core.sound.CustomSound
import com.github.syari.ss.plugins.core.sound.CustomSoundList
import org.bukkit.Sound
import org.bukkit.SoundCategory

object ConfigSoundDataType: ConfigDataType<CustomSoundList> {
    override val typeName = "CustomSoundList"

    override fun get(
        config: CustomConfig, path: String, notFoundError: Boolean
    ): CustomSoundList? {
        val getList = config.get(path, ConfigDataType.STRINGLIST, notFoundError) ?: return null
        return CustomSoundList().apply {
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
                                return@forEachIndexed
                            }
                        }
                    }
                    3, 4 -> {
                        val rawType = split[0].toUpperCase()
                        val type = Sound.values().firstOrNull { rawType == it.name } ?: return@forEachIndexed run {
                            config.nullError("$path:$index", "Sound($rawType)")
                        }
                        val volume = split[1].toFloatOrNull() ?: return@forEachIndexed run {
                            config.nullError("$path:$index", "Float(${split[1]})")
                        }
                        val pitch = split[2].toFloatOrNull() ?: return@forEachIndexed run {
                            config.nullError("$path:$index", "Float(${split[2]})")
                        }
                        val loop = split.getOrNull(3)?.let {
                            it.toIntOrNull() ?: return@forEachIndexed run {
                                config.nullError("$path:$index", "Int(${split[3]})")
                            }
                        } ?: 1
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