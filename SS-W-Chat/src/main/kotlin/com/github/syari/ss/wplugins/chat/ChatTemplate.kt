package com.github.syari.ss.wplugins.chat

import com.github.syari.ss.wplugins.core.config.CustomConfig
import com.github.syari.ss.wplugins.core.config.dataType.ConfigDataType as IConfigDataType

class ChatTemplate(private val text: String) {
    fun get(channelName: String, senderName: String, message: String): String {
        return text
            .replace("\$channelName", channelName)
            .replace("\$senderName", senderName)
            .replace("\$message", message)
    }

    object ConfigDataType : IConfigDataType<ChatTemplate> {
        override val typeName = "String(ChatTemplate)"

        override fun get(config: CustomConfig, path: String, notFoundError: Boolean): ChatTemplate? {
            return config.get(path, IConfigDataType.STRING, notFoundError)?.let(::ChatTemplate)
        }
    }

    companion object {
        val Discord = ChatTemplate("\$senderName: \$message")
    }
}
