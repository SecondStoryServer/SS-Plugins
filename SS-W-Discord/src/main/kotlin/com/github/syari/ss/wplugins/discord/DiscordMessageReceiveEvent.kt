package com.github.syari.ss.wplugins.discord

import com.github.syari.ss.wplugins.core.event.CustomEvent
import com.github.syari.ss.wplugins.discord.api.entity.Message

class DiscordMessageReceiveEvent(val message: Message) : CustomEvent() {
    inline val channel
        get() = message.channel
    inline val member
        get() = message.member
    inline val content
        get() = message.content
    inline val contentDisplay
        get() = message.contentDisplay
}
