package com.github.syari.ss.plugins.mobarena.shop

import com.github.syari.spigot.api.event.EventRegister
import com.github.syari.spigot.api.event.Events
import com.github.syari.spigot.api.string.toColor
import com.github.syari.spigot.api.string.toUncolor
import org.bukkit.block.Sign
import org.bukkit.event.block.SignChangeEvent
import org.bukkit.event.player.PlayerInteractEvent

object ShopEventListener : EventRegister {
    override fun Events.register() {
        event<SignChangeEvent> {
            val player = it.player
            val lines = it.lines.map(String::toUncolor)
            if (lines[0].equals("[MA_Shop]", true)) {
                if (player.isOp) {
                    it.lines.forEachIndexed { index, line ->
                        it.setLine(index, line.toColor())
                    }
                } else {
                    it.isCancelled = true
                }
            }
        }
        event<PlayerInteractEvent> {
            val sign = it.clickedBlock?.state as? Sign ?: return@event
            val lines = sign.lines.map(String::toUncolor)
            if (lines[0].equals("[MA_Shop]", true)) {
                Shop.get(lines[1])?.openShop(it.player)
            }
        }
    }
}
