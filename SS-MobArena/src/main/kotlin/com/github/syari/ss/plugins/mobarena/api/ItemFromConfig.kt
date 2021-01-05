package com.github.syari.ss.plugins.mobarena.api

import com.github.syari.ss.plugins.dependency.crackshot.CrackShotAPI
import com.github.syari.ss.plugins.dependency.crackshotplus.CrackShotPlusAPI
import com.github.syari.ss.plugins.dependency.mythicmobs.MythicMobsAPI
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object ItemFromConfig {
    fun get(line: String): ItemStack? {
        val split = line.split("\\s+".toRegex())
        val id = split.getOrNull(1) ?: return null
        val amount = split.getOrNull(2)?.toIntOrNull() ?: 1
        return when (split[0].toLowerCase()) {
            "mc" -> {
                val type = Material.getMaterial(id.toUpperCase()) ?: return null
                ItemStack(type, amount)
            }
            "mm" -> {
                MythicMobsAPI.getItem(id, amount)?.toOneItemStack
            }
            "cs" -> {
                CrackShotAPI.getItem(id, amount)?.toOneItemStack
            }
            "csp" -> {
                CrackShotPlusAPI.getAttachment(id, amount)?.toOneItemStack
            }
            else -> null
        }
    }
}