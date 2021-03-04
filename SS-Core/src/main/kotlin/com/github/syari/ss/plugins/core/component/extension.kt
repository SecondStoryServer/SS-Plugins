package com.github.syari.ss.plugins.core.component

import com.github.syari.ss.plugins.core.item.NBTItem.nbtTag
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.ItemTag
import net.md_5.bungee.api.chat.hover.content.Item
import org.bukkit.inventory.ItemStack

fun hoverItem(item: ItemStack) = HoverEvent(HoverEvent.Action.SHOW_ITEM, Item(item.type.key.key, 1, ItemTag.ofNbt(item.nbtTag)))
