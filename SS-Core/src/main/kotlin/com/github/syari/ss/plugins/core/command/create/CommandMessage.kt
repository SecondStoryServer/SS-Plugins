package com.github.syari.ss.plugins.core.command.create

import com.github.syari.ss.plugins.core.code.StringEditor.toColor
import com.github.syari.ss.plugins.core.message.Message.send
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.command.CommandSender

class CommandMessage internal constructor(
    private val prefix: String, private val sender: CommandSender
) {
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
     * @return [SendHelpIfOp]
     */
    fun sendHelp(vararg command: Pair<String, String>): SendHelpIfOp {
        sendList("コマンド一覧", command.map { "/${it.first} &7${it.second}" })
        return SendHelpIfOp(this)
    }

    class SendHelpIfOp(private val message: CommandMessage) {
        /**
         * @param command OPに対してのみ表示するコマンド一覧
         */
        fun ifOp(vararg command: Pair<String, String>) {
            if (message.sender.isOp) {
                message.sendList("", command.map { "/${it.first} &7${it.second}" })
            }
        }
    }

    /**
     * @param title リストのタイトル
     * @param element リストの要素
     */
    fun sendList(
        title: String, vararg element: String
    ) {
        sendList(title, element.toList())
    }

    /**
     * @param title リストのタイトル
     * @param element リストの要素
     */
    fun sendList(
        title: String = "", element: Iterable<String>
    ) {
        if (title.isNotEmpty()) sendWithPrefix("&f$title")
        sender.send(buildString {
            element.forEach {
                appendLine("&7- &a$it")
            }
        })
    }
}