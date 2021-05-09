package com.github.syari.ss.wplugins.core.command

class CommandTabElement internal constructor(list: Iterable<String>) : Collection<String> {
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
    }

    var element = list.toSet()
        private set

    override val size: Int
        get() = element.size

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
