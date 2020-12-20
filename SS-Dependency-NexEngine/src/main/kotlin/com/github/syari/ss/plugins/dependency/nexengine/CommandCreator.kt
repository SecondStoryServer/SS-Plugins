package com.github.syari.ss.plugins.dependency.nexengine

import com.github.syari.ss.plugins.core.code.OnEnable
import com.github.syari.ss.plugins.core.command.create.CreateCommand
import com.github.syari.ss.plugins.core.message.JsonBuilder
import com.github.syari.ss.plugins.dependency.nexengine.Main.Companion.plugin
import com.github.syari.ss.plugins.dependency.nexengine.NexEngineAPI.engine
import org.bukkit.entity.Player

object CommandCreator: OnEnable {
    override fun onEnable() {
        CreateCommand.createCommand(plugin, "cbase64", "Base64Converter") { sender, _ ->
            if (sender !is Player) return@createCommand sendError("プレイヤーからのみ実行可能です")
            val item = sender.inventory.itemInMainHand
            val base64 = engine.nms.toBase64(item) ?: return@createCommand sendError("Base64の取得に失敗しました")
            val displayName = item.itemMeta?.displayName ?: item.i18NDisplayName ?: item.type.name
            sendWithPrefix(JsonBuilder.buildJson {
                append("$displayName &bBase64", "&6コピー", click = JsonBuilder.Click.Clipboard(base64))
            })
        }
    }
}