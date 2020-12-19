package com.github.syari.ss.plugins.soundchecker

import org.bukkit.Material
import org.bukkit.Sound

fun soundToMaterial(sound: Sound) = when (sound) {
    else -> Material.STONE
}