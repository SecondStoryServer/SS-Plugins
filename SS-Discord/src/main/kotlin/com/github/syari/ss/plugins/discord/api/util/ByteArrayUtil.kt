package com.github.syari.ss.plugins.discord.api.util

internal object ByteArrayUtil {
    fun ByteArray.takeLastAsByteArray(n: Int): ByteArray {
        return ByteArray(n).also { result ->
            for (i in 0 until n) {
                result[i] = this[size - n + i]
            }
        }
    }

    fun Collection<ByteArray>.concat(): ByteArray {
        val length = sumBy { it.size }
        return ByteArray(length).also { output ->
            var pos = 0
            forEach {
                System.arraycopy(it, 0, output, pos, it.size)
                pos += it.size
            }
        }
    }
}