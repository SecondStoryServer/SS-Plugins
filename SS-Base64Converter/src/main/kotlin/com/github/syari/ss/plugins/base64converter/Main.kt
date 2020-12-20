package com.github.syari.ss.plugins.base64converter

import com.github.syari.ss.plugins.core.code.SSPlugin
import com.github.syari.ss.plugins.core.command.create.CreateCommand.createCommand
import com.github.syari.ss.plugins.core.item.Base64Item
import com.github.syari.ss.plugins.core.message.JsonBuilder
import org.bukkit.entity.Player

class Main: SSPlugin() {
    override fun onEnable() {
        createCommand(this, "cbase64", "Base64Converter") { sender, _ ->
            if (sender !is Player) return@createCommand sendError("プレイヤーからのみ実行可能です")
            val item = sender.inventory.itemInMainHand
            val base64 = Base64Item.toBase64(item) ?: return@createCommand sendError("Base64の取得に失敗しました")
            val displayName = item.itemMeta?.displayName?.ifBlank { null } ?: item.i18NDisplayName ?: item.type.name
            sendWithPrefix(JsonBuilder.buildJson {
                append(displayName, JsonBuilder.Hover.Item(item))
                append("  ")
                append("&b[Base64]", JsonBuilder.Hover.Text("&6コピー"), JsonBuilder.Click.Clipboard(base64))
            })
        }
    }
}