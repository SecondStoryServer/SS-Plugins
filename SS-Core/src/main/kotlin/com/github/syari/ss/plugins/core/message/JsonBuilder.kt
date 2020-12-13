package com.github.syari.ss.plugins.core.message

import com.github.syari.ss.plugins.core.code.StringEditor.toColor
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.chat.hover.content.Text

class JsonBuilder {
    companion object {
        inline fun buildJson(action: JsonBuilder.() -> Unit) = JsonBuilder().apply(action).toTextComponent
    }

    private val message = mutableListOf<JsonMessage>()

    /**
     * 末尾に文字列を挿入します
     * @param text 文字列
     * @param hover カーソルを合わせた時に表示される文字列 default: null
     * @param click クリックイベント default: null
     * @return [JsonBuilder]
     */
    fun append(
        text: String,
        hover: String? = null,
        click: Click? = null
    ): JsonBuilder {
        message.add(JsonMessage.Text(text, hover, click))
        return this
    }

    /**
     * 末尾に改行を挿入します
     * @return [JsonBuilder]
     */
    fun appendln(): JsonBuilder {
        message.add(JsonMessage.NewLine)
        return this
    }

    /**
     * 末尾に文字列を挿入し、改行します
     * @param text 文字列
     * @param hover カーソルを合わせた時に表示される文字列 default: null
     * @param click クリックイベント default: null
     * @return [JsonBuilder]
     */
    fun appendln(
        text: String,
        hover: String? = null,
        click: Click? = null
    ): JsonBuilder {
        return append(text, hover, click).appendln()
    }

    /**
     * [TextComponent] に変換します
     */
    val toTextComponent
        get() = TextComponent().apply {
            message.forEach { eachMessage ->
                when (eachMessage) {
                    is JsonMessage.Text -> addExtra(
                        TextComponent(eachMessage.text.toColor).apply {
                            eachMessage.hover?.let {
                                hoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT, Text(it.toColor))
                            }
                            eachMessage.click?.let {
                                clickEvent = ClickEvent(it.event, it.content.toColor)
                            }
                        }
                    )
                    is JsonMessage.NewLine -> addExtra("\n")
                }
            }
        }

    /**
     * メッセージ
     */
    internal sealed class JsonMessage {
        class Text(
            val text: String,
            val hover: String?,
            val click: Click?
        ): JsonMessage()

        object NewLine: JsonMessage()
    }

    /**
     * クリックイベント
     */
    sealed class Click(
        val event: ClickEvent.Action,
        val content: String
    ) {
        /**
         * コマンドを実行します
         * @param command 実行するコマンド
         */
        class RunCommand(command: String): Click(ClickEvent.Action.RUN_COMMAND, command)

        /**
         * チャット入力欄を変更します
         * @param text 変更する文字
         */
        class TypeText(text: String): Click(ClickEvent.Action.SUGGEST_COMMAND, text)

        /**
         * URLを開きます
         * @param url URL
         */
        class OpenURL(url: String): Click(ClickEvent.Action.OPEN_URL, url)

        /**
         * クリップボードにコピーします
         * @param text コピーする文字
         */
        class Clipboard(text: String): Click(ClickEvent.Action.COPY_TO_CLIPBOARD, text)
    }
}