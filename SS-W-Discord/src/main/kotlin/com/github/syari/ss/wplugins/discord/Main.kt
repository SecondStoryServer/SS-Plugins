package com.github.syari.ss.wplugins.discord

import com.github.syari.ss.wplugins.core.Main.Companion.console
import com.github.syari.ss.wplugins.core.code.SSPlugin
import com.github.syari.ss.wplugins.core.config.CreateConfig.config
import com.github.syari.ss.wplugins.core.config.dataType.ConfigDataType
import com.github.syari.ss.wplugins.discord.api.ConnectStatus

class Main : SSPlugin() {
    override fun onEnable() {
        config(console, "config.yml") {
            val token = get("token", ConfigDataType.STRING)
            if (token != null) {
                Discord.login(token)
                if (Discord.connectStatus == ConnectStatus.CONNECTED) {
                    logger.info("DiscordBotの接続に成功しました")
                }
            }
        }
    }
}
