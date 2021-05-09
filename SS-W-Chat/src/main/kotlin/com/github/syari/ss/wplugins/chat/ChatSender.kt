package com.github.syari.ss.wplugins.chat

import com.github.syari.ss.wplugins.chat.channel.ChatChannel
import com.github.syari.ss.wplugins.core.player.UUIDPlayer
import net.md_5.bungee.api.connection.ProxiedPlayer

class ChatSender(private val uuidPlayer: UUIDPlayer) {
    var channel: ChatChannel = ChatChannel.Global

    val player by uuidPlayer::player

    companion object {
        private val list = mutableMapOf<UUIDPlayer, ChatSender>()

        fun get(player: ProxiedPlayer) = get(UUIDPlayer(player))

        fun get(uuidPlayer: UUIDPlayer) = list.getOrPut(uuidPlayer) { ChatSender(uuidPlayer) }
    }
}
