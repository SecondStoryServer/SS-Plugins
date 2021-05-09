package com.github.syari.ss.template.message

import com.google.common.io.ByteArrayDataInput
import com.google.common.io.ByteArrayDataOutput

fun ByteArrayDataInput.readUTFList() = readUTF().split(",")
fun ByteArrayDataOutput.writeUTFList(list: Iterable<String>) = writeUTF(list.joinToString(","))
