package com.github.syari.ss.wplugins.chat

import com.github.syari.ss.wplugins.chat.Main.Companion.plugin
import com.github.syari.ss.wplugins.chat.channel.ChatChannel
import com.github.syari.ss.wplugins.core.code.OnEnable
import com.github.syari.ss.wplugins.core.command.CommandCreator.Companion.command
import com.github.syari.ss.wplugins.core.command.CommandTabElement.Companion.element
import com.github.syari.ss.wplugins.core.command.ErrorMessage
import net.md_5.bungee.api.connection.ProxiedPlayer

object CommandCreator : OnEnable {
    override fun onEnable() {
        plugin.command("chat", "Chat") {
            tab {
                arg { element("channel", "reload") }
                arg("channel") { element(ChatChannel.nameList) }
            }
            execute {
                when (args.whenIndex(0)) {
                    "channel" -> {
                        val player = sender as? ProxiedPlayer ?: return@execute sendError(ErrorMessage.OnlyPlayer)
                        val name = args.getOrNull(1) ?: return@execute sendError("チャンネルを入力してください")
                        ChatSender.get(player).channel = ChatChannel.getOrNull(name) ?: return@execute sendError("チャンネルが存在しません")
                    }
                    "reload" -> {
                        sendWithPrefix("コンフィグをリロードします")
                        ConfigLoader.load(sender)
                    }
                    else -> {
                        sendHelp(
                            "chat channel" to "チャンネルを変更します",
                            "chat reload" to "コンフィグをリロードします"
                        )
                    }
                }
            }
        }
    }
}
