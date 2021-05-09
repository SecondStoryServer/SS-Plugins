package com.github.syari.ss.template.message

import com.google.common.io.ByteArrayDataInput
import com.google.common.io.ByteArrayDataOutput

class PluginMessageTemplateTabList(val playerNameList: List<String>) : PluginMessageTemplate {
    companion object {
        const val SubChannelName = "tablist"

        fun readFrom(dataInput: ByteArrayDataInput): PluginMessageTemplateTabList {
            return PluginMessageTemplateTabList(dataInput.readUTFList())
        }
    }

    override val subChannel = SubChannelName
    override val queue = false

    override fun writeTo(dataOutput: ByteArrayDataOutput) {
        dataOutput.writeUTFList(playerNameList)
    }
}
