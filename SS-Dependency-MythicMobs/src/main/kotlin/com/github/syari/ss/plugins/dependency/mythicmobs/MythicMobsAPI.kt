package com.github.syari.ss.plugins.dependency.mythicmobs

import io.lumine.xikage.mythicmobs.MythicMobs
import io.lumine.xikage.mythicmobs.mobs.ActiveMob
import org.bukkit.Location
import org.bukkit.inventory.ItemStack

object MythicMobsAPI {
    private val api = MythicMobs.inst()

    fun getItem(id: String): ItemStack? {
        return api.itemManager.getItemStack(id)
    }

    fun spawnMythicMobs(id: String, location: Location): ActiveMob? {
        return api.mobManager.spawnMob(id, location)
    }
}
