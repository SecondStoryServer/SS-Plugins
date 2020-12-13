package com.github.syari.ss.plugins.dependency.crackshot

import com.github.syari.ss.plugins.core.item.CustomItemStack
import com.shampaggon.crackshot.CSUtility

object CrackShotAPI {
    private val api = CSUtility()

    fun getItem(id: String, amount: Int): CustomItemStack? {
        return CustomItemStack.fromNullable(api.generateWeapon(id), amount)
    }
}