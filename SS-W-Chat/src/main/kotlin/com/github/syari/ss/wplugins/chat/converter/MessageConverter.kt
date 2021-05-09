package com.github.syari.ss.wplugins.chat.converter

import com.github.syari.ss.wplugins.chat.converter.IMEConverter.toIME
import com.github.syari.ss.wplugins.chat.converter.KanaConverter.toKana

object MessageConverter {
    private val UrlRegex = """(https?://[\w/:%#$&?()~.=+\-]+)""".toRegex()

    interface ConvertResult {
        val formatMessage: String

        class OnlyMessage(message: String) : ConvertResult {
            override val formatMessage = "&f$message"
        }

        class WithURL(message: String) : ConvertResult {
            override val formatMessage = "&f$message"

            @OptIn(ExperimentalStdlibApi::class)
            val messageWithClickableUrl = run {
                fun MutableList<Pair<String, Boolean>>.addRange(first: Int, end: Int) {
                    if (first <= end) {
                        add(message.substring(first..end).toKana() to false)
                    }
                }

                val urlRanges = UrlRegex.findAll(message).map(MatchResult::range)
                buildList {
                    var first = 0
                    urlRanges.forEach {
                        addRange(first, it.first - 1)
                        add(message.substring(it) to true)
                        first = it.last + 1
                    }
                    addRange(first, message.lastIndex)
                }
            }
        }

        class WithConverted(message: String, converted: String) : ConvertResult {
            override val formatMessage = "&f$converted &7($message)"
        }
    }

    fun convert(message: String): ConvertResult {
        return if (message.firstOrNull() == '.') {
            ConvertResult.OnlyMessage(message.substring(1))
        } else if (matchesHalfWidthChar(message)) {
            if (UrlRegex.containsMatchIn(message)) {
                ConvertResult.WithURL(message)
            } else {
                val converted = message.toKana().toIME()
                if (message == converted) {
                    ConvertResult.OnlyMessage(message)
                } else {
                    ConvertResult.WithConverted(message, converted)
                }
            }
        } else {
            ConvertResult.OnlyMessage(message)
        }
    }

    private fun matchesHalfWidthChar(text: String): Boolean {
        return text.matches("^[a-zA-Z0-9!-/:-@\\[-`{-~\\s]*\$".toRegex())
    }
}
