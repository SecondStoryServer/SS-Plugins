package com.github.syari.ss.plugins.discord.api.handle

import com.github.syari.ss.plugins.discord.api.DiscordAPI
import com.github.syari.ss.plugins.discord.api.entity.Emoji
import com.github.syari.ss.plugins.discord.api.entity.Guild
import com.github.syari.ss.plugins.discord.api.entity.Member
import com.github.syari.ss.plugins.discord.api.entity.Message
import com.github.syari.ss.plugins.discord.api.entity.Role
import com.github.syari.ss.plugins.discord.api.entity.TextChannel
import com.github.syari.ss.plugins.discord.api.util.json.getArrayOrNull
import com.github.syari.ss.plugins.discord.api.util.json.getOrNull
import com.google.gson.JsonObject
import java.util.regex.Pattern

object MessageCreateHandler: GatewayHandler {
    override fun handle(json: JsonObject) {
        handleGuild(json)
    }

    private fun handleGuild(json: JsonObject) {
        val guildId = json.getOrNull("guild_id")?.asLong ?: return
        val guild = Guild.get(guildId) ?: return
        val channelId = json["channel_id"].asLong
        val channel = guild.getTextChannel(channelId) ?: return
        val userJson = json["author"].asJsonObject
        val memberJson = json["member"].asJsonObject
        val member = Member.from(memberJson, userJson)
        val content = json["content"].asString
        val mentionMembers = getMentionMembers(json)
        val mentionRoles = getMentionRoles(guild, json)
        val mentionChannels = getMentionChannels(guild, content)
        val mentionEmojis = getMentionEmojis(guild, content)
        val message = Message(channel, member, content, mentionMembers, mentionRoles, mentionChannels, mentionEmojis)
        DiscordAPI.messageReceiveEvent.invoke(message)
    }

    private fun getMentionMembers(parent: JsonObject): List<Member> {
        val array = parent.getArrayOrNull("mentions")
        return array?.map {
            val child = it.asJsonObject
            val memberJson = child["member"].asJsonObject
            Member.from(memberJson, child)
        } ?: emptyList()
    }

    private fun getMentionRoles(guild: Guild, parent: JsonObject): List<Role> {
        return parent.getArrayOrNull("mention_roles")?.mapNotNull {
            guild.getRole(it.asLong)
        } ?: emptyList()
    }

    private fun getMentionChannels(guild: Guild, content: String): List<TextChannel> {
        val pattern = Pattern.compile(TextChannel.REGEX)
        val matcher = pattern.matcher(content)
        return mutableListOf<TextChannel>().apply {
            while (matcher.find()) {
                val channelId = matcher.group(1).toLongOrNull() ?: continue
                val channel = guild.getTextChannel(channelId) ?: continue
                add(channel)
            }
        }
    }

    private fun getMentionEmojis(guild: Guild, content: String): List<Emoji> {
        val pattern = Pattern.compile(Emoji.REGEX)
        val matcher = pattern.matcher(content)
        return mutableListOf<Emoji>().apply {
            while (matcher.find()) {
                val emojiId = matcher.group(2).toLongOrNull() ?: continue
                val emoji = guild.getEmoji(emojiId) ?: continue
                add(emoji)
            }
        }
    }
}