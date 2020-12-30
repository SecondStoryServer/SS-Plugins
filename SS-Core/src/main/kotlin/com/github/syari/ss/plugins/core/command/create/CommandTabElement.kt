package com.github.syari.ss.plugins.core.command.create

import org.bukkit.command.CommandSender

class CommandTabElement internal constructor(list: Iterable<String>): Collection<String> {
    companion object {
        /**
         * タブ補完の要素
         * @param element 要素
         * @return [CommandTabElement]
         */
        fun element(element: Iterable<String>?): CommandTabElement {
            return CommandTabElement(element ?: listOf())
        }

        /**
         * タブ補完の要素
         * @param element 要素
         * @return [CommandTabElement]
         */
        fun element(vararg element: String): CommandTabElement {
            return element(element.toList())
        }

        /**
         * タブ補完の要素
         * @param condition 条件
         * @param element 条件に一致した場合の要素
         * @param unlessElement 条件に一致しなかった場合の要素
         * @return [CommandTabElement]
         */
        fun elementIf(
            condition: Boolean, element: Iterable<String>?, unlessElement: Iterable<String>? = listOf()
        ): CommandTabElement {
            return element(if (condition) element else unlessElement)
        }

        /**
         * タブ補完の要素
         * @param condition 条件
         * @param element 条件に一致した場合の要素
         * @param unlessElement 条件に一致しなかった場合の要素
         * @return [CommandTabElement]
         */
        fun elementIf(
            condition: Boolean, vararg element: String, unlessElement: Iterable<String>? = listOf()
        ): CommandTabElement {
            return elementIf(condition, element.toList(), unlessElement)
        }

        /**
         * タブ補完の要素
         * @param sender CommandSender
         * @param element sender.isOpが真であった場合の要素
         * @param unlessElement sender.isOpが偽であった場合の要素
         * @return [CommandTabElement]
         */
        fun elementIfOp(
            sender: CommandSender, element: Iterable<String>?, unlessElement: Iterable<String>? = listOf()
        ): CommandTabElement {
            return elementIf(sender.isOp, element, unlessElement)
        }

        /**
         * タブ補完の要素
         * @param sender CommandSender
         * @param element sender.isOpが真であった場合の要素
         * @param unlessElement sender.isOpが偽であった場合の要素
         * @return [CommandTabElement]
         */
        fun elementIfOp(
            sender: CommandSender, vararg element: String, unlessElement: Iterable<String>? = listOf()
        ): CommandTabElement {
            return elementIfOp(sender, element.toList(), unlessElement)
        }
    }

    var element = list.toSet()
        private set

    override val size: Int
        get() = element.size

    /**
     * 要素を追加します
     * @param element 追加する要素
     */
    fun join(element: Iterable<String>): CommandTabElement {
        this.element = this.element.union(element)
        return this
    }

    /**
     * 要素を追加します
     * @param element 追加する要素
     */
    fun join(vararg element: String): CommandTabElement {
        return join(element.toList())
    }

    /**
     * [condition] が 真 だった場合に 要素を追加します
     * @param condition 条件
     * @param element 条件に一致した場合追加する要素
     */
    fun joinIf(
        condition: Boolean, element: Iterable<String>
    ): CommandTabElement {
        return if (condition) join(element) else this
    }

    /**
     * [condition] が 真 だった場合に 要素を追加します
     * @param condition 条件
     * @param element 条件に一致した場合追加する要素
     */
    fun joinIf(
        condition: Boolean, vararg element: String
    ): CommandTabElement {
        return joinIf(condition, element.toList())
    }

    /**
     * [sender] が OP だった場合に 要素を追加します
     * @param sender CommandSender
     * @param element sender.isOpが真だった場合追加する要素
     */
    fun joinIfOp(
        sender: CommandSender, element: Iterable<String>
    ): CommandTabElement {
        return joinIf(sender.isOp, element)
    }

    /**
     * [sender] が OP だった場合に 要素を追加します
     * @param sender CommandSender
     * @param element sender.isOpが真だった場合追加する要素
     */
    fun joinIfOp(
        sender: CommandSender, vararg element: String
    ): CommandTabElement {
        return joinIfOp(sender, element.toList())
    }

    /**
     * @param sender CommandSender
     * @param element sender.isOpが偽だった場合追加する要素
     */
    fun joinIfNotOp(
        sender: CommandSender, element: Iterable<String>
    ): CommandTabElement {
        return joinIf(!sender.isOp, element)
    }

    /**
     * @param sender CommandSender
     * @param element sender.isOpが偽だった場合追加する要素
     */
    fun joinIfNotOp(
        sender: CommandSender, vararg element: String
    ): CommandTabElement {
        return joinIfNotOp(sender, element.toList())
    }

    override fun contains(element: String): Boolean {
        return this.element.contains(element)
    }

    override fun containsAll(elements: Collection<String>): Boolean {
        return element.containsAll(elements)
    }

    override fun isEmpty(): Boolean {
        return element.isEmpty()
    }

    override fun iterator(): Iterator<String> {
        return element.iterator()
    }
}