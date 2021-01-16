@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.github.syari.ss.plugins.core.message

import com.github.syari.ss.plugins.core.code.StringEditor.toColor
import com.github.syari.ss.plugins.core.item.CustomItemStack
import com.github.syari.ss.plugins.core.item.NBTItem.nbtTag
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.ItemTag
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.chat.hover.content.Content
import net.md_5.bungee.api.chat.hover.content.Item
import org.bukkit.inventory.ItemStack

class JsonBuilder {
    companion object {
        inline fun buildJson(action: JsonBuilder.() -> Unit) = JsonBuilder().apply(action).toTextComponent
    }

    private val messages = mutableListOf<JsonMessage>()

    /**
     * 末尾に文字列を挿入します
     * @param text 文字列
     * @param hover ホバーイベント default: null
     * @param click クリックイベント default: null
     * @return [JsonBuilder]
     */
    fun append(
        text: String,
        hover: Hover? = null,
        click: Click? = null
    ): JsonBuilder {
        messages.add(JsonMessage.Text(text, hover, click))
        return this
    }

    /**
     * 末尾に改行を挿入します
     * @return [JsonBuilder]
     */
    fun appendln(): JsonBuilder {
        messages.add(JsonMessage.NewLine)
        return this
    }

    /**
     * 末尾に文字列を挿入し、改行します
     * @param text 文字列
     * @param hover ホバーイベント default: null
     * @param click クリックイベント default: null
     * @return [JsonBuilder]
     */
    fun appendln(
        text: String,
        hover: Hover? = null,
        click: Click? = null
    ): JsonBuilder {
        return append(text, hover, click).appendln()
    }

    /**
     * [TextComponent] に変換します
     */
    val toTextComponent
        get() = TextComponent().apply {
            messages.forEach { message ->
                when (message) {
                    is JsonMessage.Text -> addExtra(
                        TextComponent(message.text.toColor).apply {
                            message.hover?.let {
                                hoverEvent = HoverEvent(it.event, it.content)
                            }
                            message.click?.let {
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
            val hover: Hover?,
            val click: Click?
        ) : JsonMessage()

        object NewLine : JsonMessage()
    }

    /**
     * ホバーイベント
     */
    sealed class Hover(
        val event: HoverEvent.Action,
        val content: Content
    ) {
        /**
         * 文字列を表示します
         * @param text 表示する文字列
         */
        class Text(text: String) : Hover(HoverEvent.Action.SHOW_TEXT, net.md_5.bungee.api.chat.hover.content.Text(text.toColor))

        /**
         * アイテムを表示します
         * @param item 表示するアイテム
         */
        class Item(item: net.md_5.bungee.api.chat.hover.content.Item) : Hover(HoverEvent.Action.SHOW_ITEM, item) {
            constructor(item: ItemStack) : this(Item(item.type.key.key, 1, ItemTag.ofNbt(item.nbtTag)))
            constructor(item: CustomItemStack) : this(item.toOneItemStack)
        }
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
        class RunCommand(command: String) : Click(ClickEvent.Action.RUN_COMMAND, command)

        /**
         * チャット入力欄を変更します
         * @param text 変更する文字
         */
        class TypeText(text: String) : Click(ClickEvent.Action.SUGGEST_COMMAND, text)

        /**
         * URLを開きます
         * @param url URL
         */
        class OpenURL(url: String) : Click(ClickEvent.Action.OPEN_URL, url)

        /**
         * クリップボードにコピーします
         * @param text コピーする文字
         */
        class Clipboard(text: String) : Click(ClickEvent.Action.COPY_TO_CLIPBOARD, text)
    }
}
