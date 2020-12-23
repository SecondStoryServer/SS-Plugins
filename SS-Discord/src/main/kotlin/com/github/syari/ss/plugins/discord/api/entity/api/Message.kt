package com.github.syari.ss.plugins.discord.api.entity.api

import com.github.syari.ss.plugins.discord.api.entity.Mentionable.Companion.replaceAll

class Message internal constructor(
    val channel: TextChannel,
    val member: Member,
    val content: String,
    private val mentionMembers: List<Member>,
    private val mentionRoles: List<Role>,
    private val mentionChannels: List<TextChannel>,
    private val mentionEmojis: List<Emoji>
) {
    private inline val allMention
        get() = mentionMembers + mentionRoles + mentionChannels + mentionEmojis

    val contentDisplay = content.replaceAll(allMention)

    override fun toString(): String {
        return "Message(#${channel.name} ${member.displayName}: ${contentDisplay})"
    }
}