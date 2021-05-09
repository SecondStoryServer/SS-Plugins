package com.github.syari.ss.template.message

import com.google.common.io.ByteArrayDataInput
import com.google.common.io.ByteArrayDataOutput

interface PluginMessageTemplate {
    companion object {
        const val ChannelName = "ss:pipe"

        fun readFrom(dataInput: ByteArrayDataInput): PluginMessageTemplate? {
            return when (dataInput.readUTF().toLowerCase()) {
                PluginMessageTemplateTabList.SubChannelName -> PluginMessageTemplateTabList.readFrom(dataInput)
                PluginMessageTemplateChatChannel.SubChannelName -> PluginMessageTemplateChatChannel.readFrom(dataInput)
                else -> null
            }
        }
    }

    val subChannel: String
    val queue: Boolean

    fun writeTo(dataOutput: ByteArrayDataOutput)
}
