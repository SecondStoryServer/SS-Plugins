package com.github.syari.ss.plugins.dependency.crackshot

import com.shampaggon.crackshot.CSUtility
import org.bukkit.inventory.ItemStack

object CrackShotAPI {
    private val api = CSUtility()

    fun getItem(id: String): ItemStack? {
        return api.generateWeapon(id)
    }
}
