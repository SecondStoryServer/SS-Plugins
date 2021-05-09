package com.github.syari.ss.wplugins.chat.channel

import com.github.syari.ss.wplugins.chat.ChatSender
import com.github.syari.ss.wplugins.chat.Main.Companion.plugin
import com.github.syari.ss.wplugins.chat.converter.MessageConverter
import com.github.syari.ss.wplugins.core.Main.Companion.console
import com.github.syari.ss.wplugins.core.code.StringEditor.toUncolor
import com.github.syari.ss.wplugins.core.message.JsonBuilder
import com.github.syari.ss.wplugins.core.message.JsonBuilder.Companion.buildJson
import com.github.syari.ss.wplugins.core.message.Message.send
import com.github.syari.ss.wplugins.core.player.UUIDPlayer
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.connection.ProxiedPlayer

sealed class ChatChannel(val channelName: String) {
    companion object {
        fun get(name: String) = if (name == Global.channelName) Global else Private.get(name)

        fun getOrNull(name: String) = if (name == Global.channelName) Global else Private.getOrNull(name)

        val nameList
            get() = listOf(Global.channelName) + Private.nameList + Direct.nameList

        fun reloadOption() {
            Global.reloadOption()
            Private.reloadOption()
        }
    }

    protected var options = listOf<ChatChannelOption>()

    open fun reloadOption() {
        options = ChatChannelOption.get(channelName)
    }

    abstract fun send(message: TextComponent)

    fun sendConsoleLog(name: String, message: String, isDiscord: Boolean) {
        console.send("Chat($channelName:${if (isDiscord) "Discord" else "Server"}) $name: $message")
    }

    fun send(player: ProxiedPlayer, message: String) {
        val convertMessage = MessageConverter.convert(message.toUncolor)
        send(
            buildJson {
                val prefix = options.firstOrNull { it.prefix != null }?.prefix.orEmpty()
                val name = player.displayName
                val serverName = player.server.info.name
                append("$prefix$name: ", JsonBuilder.Hover.Text("&bServer: &f$serverName"))
                when (convertMessage) {
                    is MessageConverter.ConvertResult.WithURL -> {
                        convertMessage.messageWithClickableUrl.forEach {
                            if (it.second) {
                                append(it.first, JsonBuilder.Hover.Text("&aリンクを開く"), JsonBuilder.Click.OpenURL(it.first))
                            } else {
                                append(it.first)
                            }
                        }
                    }
                    else -> {
                        append(convertMessage.formatMessage)
                    }
                }
            }
        )
        val stringMessage = convertMessage.formatMessage.toUncolor
        options.forEach { it.discordChannel?.send(it.templateDiscord.get(channelName, player.displayName, stringMessage)) }
        sendConsoleLog(player.name, stringMessage, false)
    }

    object Global : ChatChannel("global") {
        init {
            reloadOption()
        }

        override fun send(message: TextComponent) {
            plugin.proxy.players.forEach { it.send(message) }
        }
    }

    class Private(name: String) : ChatChannel(name) {
        companion object {
            private val list = mutableMapOf<String, Private>()

            fun get(name: String) = list.getOrPut(name) { Private(name) }

            fun getOrNull(name: String) = list[name]

            val nameList
                get() = list.keys

            fun reloadOption() {
                list.values.forEach(ChatChannel::reloadOption)
            }
        }

        private val list = mutableSetOf<ChatSender>()

        init {
            reloadOption()
        }

        override fun reloadOption() {
            removePlayers(options.flatMap(ChatChannelOption::players).map(ChatSender::get))
            super.reloadOption()
            addPlayers(options.flatMap(ChatChannelOption::players).map(ChatSender::get))
        }

        fun addPlayers(players: Collection<ChatSender>) {
            list.addAll(players)
        }

        fun removePlayers(players: Collection<ChatSender>) {
            list.removeAll(players)
        }

        override fun send(message: TextComponent) {
            list.forEach { it.player?.send(message) }
        }
    }

    class Direct(name: String, private val players: Set<UUIDPlayer>) : ChatChannel(name) {
        companion object {
            private val list = mutableMapOf<Set<UUIDPlayer>, Direct>()

            fun get(from: ProxiedPlayer, to: ProxiedPlayer): Direct {
                val players = setOf(UUIDPlayer(from), UUIDPlayer(to))
                val channelName = "direct/${from.name}-${to.name}"
                return list.getOrPut(players) { Direct(channelName, players) }
            }

            val nameList
                get() = list.values.map(Direct::channelName)
        }

        override fun send(message: TextComponent) {
            players.forEach { it.player?.send(message) }
        }
    }
}
