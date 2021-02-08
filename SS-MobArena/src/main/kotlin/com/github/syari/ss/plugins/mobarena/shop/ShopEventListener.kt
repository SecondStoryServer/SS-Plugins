package com.github.syari.ss.plugins.mobarena.shop

import com.github.syari.spigot.api.event.register.EventRegister
import com.github.syari.spigot.api.event.register.Events
import com.github.syari.ss.plugins.core.code.StringEditor.toColor
import com.github.syari.ss.plugins.core.code.StringEditor.toUncolor
import org.bukkit.block.Sign
import org.bukkit.event.block.SignChangeEvent
import org.bukkit.event.player.PlayerInteractEvent

object ShopEventListener : EventRegister {
    override fun Events.register() {
        event<SignChangeEvent> {
            val player = it.player
            val lines = it.lines.toUncolor
            if (lines[0].equals("[MA_Shop]", true)) {
                if (player.isOp) {
                    it.lines.forEachIndexed { index, line ->
                        it.setLine(index, line.toColor)
                    }
                } else {
                    it.isCancelled = true
                }
            }
        }
        event<PlayerInteractEvent> {
            val sign = it.clickedBlock?.state as? Sign ?: return@event
            val lines = sign.lines.toUncolor
            if (lines[0].equals("[MA_Shop]", true)) {
                Shop.get(lines[1])?.openShop(it.player)
            }
        }
    }
}
