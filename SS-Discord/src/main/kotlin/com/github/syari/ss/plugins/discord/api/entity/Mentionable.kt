package com.github.syari.ss.plugins.discord.api.entity

interface Mentionable {
    val asMentionRegex: Regex

    val asMentionDisplay: String

    companion object {
        internal fun String.replaceAll(mentionable: Iterable<Mentionable>): String {
            var result = this
            mentionable.forEach {
                result = result.replace(it.asMentionRegex, it.asMentionDisplay)
            }
            return result
        }
    }
}