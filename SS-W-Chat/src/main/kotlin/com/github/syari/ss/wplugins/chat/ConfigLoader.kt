package com.github.syari.ss.wplugins.chat

import com.github.syari.ss.wplugins.chat.Main.Companion.plugin
import com.github.syari.ss.wplugins.chat.channel.ChatChannel
import com.github.syari.ss.wplugins.chat.channel.ChatChannelOption
import com.github.syari.ss.wplugins.chat.discord.Discord
import com.github.syari.ss.wplugins.chat.discord.DiscordListenChannel
import com.github.syari.ss.wplugins.core.Main.Companion.console
import com.github.syari.ss.wplugins.core.code.OnEnable
import com.github.syari.ss.wplugins.core.config.CreateConfig.config
import com.github.syari.ss.wplugins.core.config.dataType.ConfigDataType
import com.github.syari.ss.wplugins.core.player.UUIDPlayer
import net.md_5.bungee.api.CommandSender

object ConfigLoader : OnEnable {
    override fun onEnable() {
        load(console)
    }

    @OptIn(ExperimentalStdlibApi::class)
    fun load(sender: CommandSender) {
        plugin.config(sender, "config.yml") {
            ChatChannelOption.list = section("channel", false)?.associate {
                val regex = get("channel.$it.regex", ConfigDataType.STRING, it, false).toRegex()
                val discordChannelId = get("channel.$it.discord", ConfigDataType.LONG, false)
                val templateDiscord = get("channel.$it.template.discord", ChatTemplate.ConfigDataType, ChatTemplate.Discord, false)
                val prefix = get("channel.$it.prefix", ConfigDataType.STRING, false)
                val players = get("channel.$it.player", ConfigDataType.UUIDLIST, false).orEmpty()
                regex to ChatChannelOption(discordChannelId, templateDiscord, prefix, players.map(::UUIDPlayer))
            }.orEmpty()
            Discord.joinUrl = get("discord.url", ConfigDataType.STRING, false)
            Discord.listenChannels = buildMap {
                section("discord.channel", false)?.forEach { name ->
                    val prefix = get("discord.channel.$name.prefix", ConfigDataType.STRING, "&d", false)
                    val listenChannel = get("discord.channel.$name.id", ConfigDataType.LONG) ?: return@forEach
                    val maxLength = get("discord.channel.$name.max-length", ConfigDataType.INT, false)
                    val channel = ChatChannel.get(name)
                    put(listenChannel, DiscordListenChannel(prefix, channel, maxLength))
                }
            }
            ChatChannel.reloadOption()
        }
    }
}
