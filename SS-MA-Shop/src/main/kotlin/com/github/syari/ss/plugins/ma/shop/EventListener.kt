package com.github.syari.ss.plugins.ma.shop

import com.github.syari.ss.plugins.core.code.StringEditor.toColor
import com.github.syari.ss.plugins.core.code.StringEditor.toUncolor
import org.bukkit.block.Sign
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.SignChangeEvent
import org.bukkit.event.player.PlayerInteractEvent

object EventListener: Listener {
    @EventHandler
    fun onCreateShop(e: SignChangeEvent) {
        val p = e.player
        val lines = e.lines.toUncolor
        if (lines[0].equals("[MA_Shop]", true)) {
            if (p.isOp) {
                e.lines.forEachIndexed { index, line ->
                    e.setLine(index, line.toColor)
                }
            } else {
                e.isCancelled = true
            }
        }
    }

    @EventHandler
    fun onOpenShop(e: PlayerInteractEvent) {
        val sign = e.clickedBlock?.state as? Sign ?: return
        val lines = sign.lines.toUncolor
        if (lines[0].equals("[MA_Shop]", true)) {
            Shop.get(lines[1])?.open(e.player)
        }
    }
}