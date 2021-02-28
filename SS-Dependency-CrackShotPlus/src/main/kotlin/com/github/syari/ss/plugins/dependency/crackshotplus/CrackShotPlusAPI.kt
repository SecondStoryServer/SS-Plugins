package com.github.syari.ss.plugins.dependency.crackshotplus

import me.DeeCaaD.CrackShotPlus.CSPapi
import org.bukkit.inventory.ItemStack

object CrackShotPlusAPI {
    fun getAttachment(id: String, amount: Int): ItemStack? {
        return CSPapi.getAttachmentItemStack(id)?.asQuantity(amount)
    }
}
