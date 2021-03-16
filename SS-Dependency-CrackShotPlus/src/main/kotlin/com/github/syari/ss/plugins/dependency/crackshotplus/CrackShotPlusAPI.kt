package com.github.syari.ss.plugins.dependency.crackshotplus

import me.DeeCaaD.CrackShotPlus.API
import me.DeeCaaD.CrackShotPlus.CSPapi
import org.bukkit.inventory.ItemStack

object CrackShotPlusAPI {
    fun getCrackShotItem(id: String): ItemStack? {
        return API.getCSUtility().generateWeapon(id)?.let {
            CSPapi.updateItemStackFeaturesNonPlayer(id, it)
        }
    }

    fun getAttachment(id: String): ItemStack? {
        return CSPapi.getAttachmentItemStack(id)
    }
}
