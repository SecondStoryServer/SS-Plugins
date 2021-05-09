package com.github.syari.ss.wplugins.core.command

import com.github.syari.ss.wplugins.core.code.StringEditor.toColor
import com.github.syari.ss.wplugins.core.message.Message.send
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.chat.TextComponent

class CommandExecuteAction internal constructor(
    private val prefix: String,
    val sender: CommandSender,
    val args: CommandExecuteArgument
) {
    init {
        args.executeAction = this
    }

    /**
     * ```
     * sendWithPrefix("&c$message")
     * ```
     * @param message 本文
     */
    fun sendError(message: String) {
        sendWithPrefix("&c$message")
    }

    /**
     * よく使うエラーが列挙として定義されています
     * @param errorMessage エラーの種類
     */
    fun sendError(errorMessage: ErrorMessage) {
        sendError(errorMessage.message)
    }

    /**
     * [prefix] が接頭についたメッセージを送信します
     * @param message 本文
     */
    fun sendWithPrefix(message: String) {
        sender.send("&b[$prefix] &r$message")
    }

    /**
     * [prefix] が接頭についたメッセージを送信します
     * @param message 本文
     */
    fun sendWithPrefix(message: TextComponent) {
        sender.send(TextComponent("&b[$prefix] ".toColor).apply { addExtra(message) })
    }

    /**
     * コマンドヘルプを送信します
     * ```
     * Format: "/$first &7$second"
     * ```
     * @param command コマンド一覧
     */
    fun sendHelp(vararg command: Pair<String, String>) {
        sendList("コマンド一覧", command.map { "/${it.first} &7${it.second}" })
    }

    /**
     * @param title リストのタイトル
     * @param element リストの要素
     */
    fun sendList(
        title: String,
        vararg element: String
    ) {
        sendList(title, element.toList())
    }

    /**
     * @param title リストのタイトル
     * @param element リストの要素
     */
    fun sendList(
        title: String = "",
        element: Iterable<String>
    ) {
        if (title.isNotEmpty()) sendWithPrefix("&f$title")
        sender.send(
            buildString {
                element.forEach {
                    appendLine("&7- &a$it")
                }
            }
        )
    }
}
