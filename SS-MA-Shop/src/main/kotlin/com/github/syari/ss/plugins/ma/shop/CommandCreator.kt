package com.github.syari.ss.plugins.ma.shop

import com.github.syari.ss.plugins.core.code.OnEnable
import com.github.syari.ss.plugins.core.command.create.CreateCommand.createCommand
import com.github.syari.ss.plugins.core.command.create.CreateCommand.element
import com.github.syari.ss.plugins.core.command.create.CreateCommand.tab
import com.github.syari.ss.plugins.core.command.create.ErrorMessage
import com.github.syari.ss.plugins.ma.shop.Main.Companion.plugin
import org.bukkit.entity.Player

object CommandCreator: OnEnable {
    override fun onEnable() {
        createCommand(plugin, "mashop", "MA_Shop", tab { element("open", "reload") }, tab("open") {
            element(Shop.getNames())
        }) { sender, args ->
            when (args.whenIndex(0)) {
                "open" -> {
                    if (sender !is Player) return@createCommand sendError(ErrorMessage.OnlyPlayer)
                    val id = args.getOrNull(1) ?: return@createCommand sendError(ErrorMessage.NotEnterId)
                    val shopData = Shop.get(id) ?: return@createCommand sendError(ErrorMessage.NotExistId)
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