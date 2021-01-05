package com.github.syari.ss.plugins.dependency.mythicmobs

import com.github.syari.ss.plugins.core.item.CustomItemStack
import io.lumine.xikage.mythicmobs.MythicMobs
import io.lumine.xikage.mythicmobs.mobs.ActiveMob
import org.bukkit.Location

object MythicMobsAPI {
    private val api = MythicMobs.inst()

    fun getItem(id: String, amount: Int): CustomItemStack? {
        return CustomItemStack.fromNullable(api.itemManager.getItemStack(id), amount)
    }

    fun spawnMythicMobs(id: String, location: Location): ActiveMob? {
        return api.mobManager.spawnMob(id, location)
    }
}