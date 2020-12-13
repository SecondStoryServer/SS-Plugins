package com.github.syari.ss.plugins.checker.material

import com.github.syari.ss.plugins.core.code.SSPlugin
import com.github.syari.ss.plugins.core.command.create.CreateCommand.createCommand
import com.github.syari.ss.plugins.core.message.JsonBuilder
import com.github.syari.ss.plugins.core.message.JsonBuilder.Companion.buildJson
import org.bukkit.entity.Player

class Main: SSPlugin() {
    override fun onEnable() {
        createCommand(this, "check-material", "MaterialChecker") { sender, args ->
            if (sender !is Player) return@createCommand sendError("プレイヤーからのみ実行可能です")
            if (sender.isOp) {
                val item = sender.inventory.itemInMainHand
                val itemTypeName = item.type.name
                sendWithPrefix(buildJson {
                    append(itemTypeName, click = JsonBuilder.Click.Clipboard(itemTypeName))
                })
            }
        }
    }
}