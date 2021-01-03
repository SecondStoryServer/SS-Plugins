package com.github.syari.ss.plugins.itemframecommand

import com.github.syari.ss.plugins.core.code.StringEditor.toColor
import org.bukkit.inventory.ItemStack

object ItemFrameCommand {
    private val Prefix = "&b額縁コマンド: /".toColor

    val ItemStack.isFrameCommandsItem
        get() = lore?.any { it.startsWith(Prefix) } ?: false

    val ItemStack.frameCommands
        get() = lore?.map { it.substringAfter(Prefix) }.orEmpty()
}