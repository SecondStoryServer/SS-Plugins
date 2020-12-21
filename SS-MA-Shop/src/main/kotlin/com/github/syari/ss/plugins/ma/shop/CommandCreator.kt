package com.github.syari.ss.plugins.ma.shop

import com.github.syari.ss.plugins.core.code.OnEnable
import com.github.syari.ss.plugins.core.command.create.CreateCommand.command
import com.github.syari.ss.plugins.core.command.create.CreateCommand.element
import com.github.syari.ss.plugins.core.command.create.CreateCommand.tab
import com.github.syari.ss.plugins.core.command.create.ErrorMessage
import com.github.syari.ss.plugins.ma.shop.Main.Companion.plugin
import org.bukkit.entity.Player

object CommandCreator: OnEnable {
    override fun onEnable() {
        command(plugin, "mashop", "MA_Shop", tab { element("open", "reload") }, tab("open") {
            element(Shop.getNames())
        }) { sender, args ->
            when (args.whenIndex(0)) {
                "open" -> {
                    if (sender !is Player) return@command sendError(ErrorMessage.OnlyPlayer)
                    val id = args.getOrNull(1) ?: return@command sendError(ErrorMessage.NotEnterId)
                    val shopData = Shop.get(id) ?: return@command sendError(ErrorMessage.NotExistId)
                    shopData.open(sender)
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