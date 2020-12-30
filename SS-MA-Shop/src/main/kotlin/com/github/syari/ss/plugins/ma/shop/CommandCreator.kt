package com.github.syari.ss.plugins.ma.shop

import com.github.syari.ss.plugins.core.code.OnEnable
import com.github.syari.ss.plugins.core.command.create.CommandCreator.Companion.command
import com.github.syari.ss.plugins.core.command.create.CommandTabElement.Companion.element
import com.github.syari.ss.plugins.core.command.create.ErrorMessage
import com.github.syari.ss.plugins.ma.shop.Main.Companion.plugin
import org.bukkit.entity.Player

object CommandCreator: OnEnable {
    override fun onEnable() {
        plugin.command("ma-shop", "MA-Shop") {
            tab {
                arg { element("open", "reload") }
                arg("open") { element(Shop.getNames()) }
            }
            execute {
                when (args.whenIndex(0)) {
                    "open" -> {
                        val player = sender as? Player ?: return@execute sendError(ErrorMessage.OnlyPlayer)
                        val id = args.getOrNull(1) ?: return@execute sendError(ErrorMessage.NotEnterId)
                        val shopData = Shop.get(id) ?: return@execute sendError(ErrorMessage.NotExistId)
                        shopData.open(player)
                    }
                    "reload" -> {
                        sendWithPrefix("コンフィグをリロードします")
                        ConfigLoader.loadConfig(sender)
                    }
                    else -> sendHelp(
                        "/mashop open <ID>" to "ショップを開きます", //
                        "/mashop reload" to "コンフィグを再読み込みします"
                    )
                }
            }
        }
    }
}