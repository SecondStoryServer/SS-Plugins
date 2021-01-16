@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.github.syari.ss.plugins.core.code

import org.bukkit.ChatColor

object StringEditor {
    /**
     * 文字を色付きにします
     */
    val String.toColor get(): String = ChatColor.translateAlternateColorCodes('&', this)

    /**
     * 文字から色を無くします
     */
    val String.toUncolor get() = ChatColor.stripColor(toColor) ?: this

    /**
     * 文字を色付きにします
     */
    val Iterable<String>.toColor get() = map { it.toColor }

    /**
     * 文字から色を無くします
     */
    val Iterable<String>.toUncolor get() = map { it.toUncolor }

    /**
     * 文字を色付きにします
     */
    val Array<String>.toColor get() = map { it.toColor }

    /**
     * 文字から色を無くします
     */
    val Array<String>.toUncolor get() = map { it.toUncolor }
}
