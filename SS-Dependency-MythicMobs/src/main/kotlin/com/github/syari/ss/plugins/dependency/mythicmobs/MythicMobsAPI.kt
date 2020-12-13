package com.github.syari.ss.plugins.dependency.mythicmobs

import com.github.syari.ss.plugins.core.item.CustomItemStack
import io.lumine.xikage.mythicmobs.MythicMobs

object MythicMobsAPI {
    private val api = MythicMobs.inst()

    fun getItem(id: String, amount: Int): CustomItemStack? {
        return CustomItemStack.fromNullable(api.itemManager.getItemStack(id), amount)
    }
}