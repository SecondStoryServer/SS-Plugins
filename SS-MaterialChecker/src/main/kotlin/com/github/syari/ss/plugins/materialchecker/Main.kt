package com.github.syari.ss.plugins.materialchecker

import com.github.syari.ss.plugins.core.code.SSPlugin
import com.github.syari.ss.plugins.core.command.create.CommandCreator.Companion.command
import com.github.syari.ss.plugins.core.message.JsonBuilder
import com.github.syari.ss.plugins.core.message.JsonBuilder.Companion.buildJson
import org.bukkit.entity.Player

class Main : SSPlugin() {
    override fun onEnable() {
        command("cmaterial", "MaterialChecker") {
            execute {
                val player = sender as? Player ?: return@execute sendError("プレイヤーからのみ実行可能です")
                val item = player.inventory.itemInMainHand
                val itemTypeName = item.type.name
                sendWithPrefix(
                    buildJson {
                        append(itemTypeName, JsonBuilder.Hover.Text("&6コピー"), click = JsonBuilder.Click.Clipboard(itemTypeName))
                    }
                )
            }
        }
    }
}
