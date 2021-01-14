package com.github.syari.ss.plugins.dependency.crackshotplus

import com.github.syari.ss.plugins.core.item.CustomItemStack
import me.DeeCaaD.CrackShotPlus.CSPapi

object CrackShotPlusAPI {
    fun getAttachment(id: String, amount: Int): CustomItemStack? {
        return CustomItemStack.fromNullable(CSPapi.getAttachmentItemStack(id), amount)
    }
}
