package com.github.syari.ss.plugins.rpg.core

import org.bukkit.ChatColor
import org.bukkit.Color

/**
 * 属性
 * @param color 色
 * @param chatColor 文字色
 * @param displayName 表示名
 * @param icon アイコン
 */
enum class ElementType(
    val color: Color,
    val chatColor: ChatColor,
    val displayName: String,
    val icon: String
) {
    /**
     * 火属性 🔥
     */
    Fire(
        Color.RED,
        ChatColor.RED,
        "火",
        "\uD83D\uDD25"
    ),

    /**
     * 水属性 💧
     */
    Water(
        Color.AQUA,
        ChatColor.AQUA,
        "水",
        "\uD83D\uDCA7"
    ),

    /**
     * 木属性 🌱
     */
    Wood(
        Color.LIME,
        ChatColor.GREEN,
        "木",
        "\uD83C\uDF31"
    ),

    /**
     * 聖属性 🌟
     */
    Holy(
        Color.YELLOW,
        ChatColor.YELLOW,
        "聖",
        "\uD83C\uDF1F"
    ),

    /**
     * 闇属性 💀
     */
    Dark(
        Color.PURPLE,
        ChatColor.LIGHT_PURPLE,
        "闇",
        "\uD83D\uDC80"
    ),

    /**
     * 無属性 🦴
     */
    None(
        Color.GRAY,
        ChatColor.GRAY,
        "無",
        "\uD83E\uDDB4"
    );
}
