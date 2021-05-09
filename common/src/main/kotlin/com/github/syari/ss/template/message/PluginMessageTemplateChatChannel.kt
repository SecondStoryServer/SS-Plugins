package com.github.syari.ss.template.message

import com.google.common.io.ByteArrayDataInput
import com.google.common.io.ByteArrayDataOutput

class PluginMessageTemplateChatChannel(val task: UpdateTask, val channelName: String, val playerNameList: List<String>) :
    PluginMessageTemplate {
    companion object {
        const val SubChannelName = "chatchannel"

        fun readFrom(dataInput: ByteArrayDataInput): PluginMessageTemplateChatChannel {
            val task = UpdateTask.get(dataInput.readByte())
            val channelName = dataInput.readUTF()
            val playerNameList = dataInput.readUTFList()
            return PluginMessageTemplateChatChannel(task, channelName, playerNameList)
        }
    }

    enum class UpdateTask(val value: Byte) {
        AddPlayer(0),
        RemovePlayer(1),
        SetSpeaker(2),
        UnSetSpeaker(3);

        companion object {
            fun get(value: Byte) = values().first { it.value == value }
        }
    }

    override val subChannel = SubChannelName
    override val queue = false

    override fun writeTo(dataOutput: ByteArrayDataOutput) {
        dataOutput.writeByte(task.value.toInt())
        dataOutput.writeUTF(channelName)
        dataOutput.writeUTFList(playerNameList)
    }
}
