package com.github.syari.ss.plugins.rpg.core

import org.bukkit.ChatColor
import org.bukkit.Color

enum class ElementType(
    val color: Color,
    val chatColor: ChatColor,
    val displayName: String,
    val icon: String
) {
    Fire(
        Color.RED,
        ChatColor.RED,
        "火",
        "\uD83D\uDD25" // 🔥
    ),
    Water(
        Color.AQUA,
        ChatColor.AQUA,
        "水",
        "\uD83D\uDCA7" // 💧
    ),
    Wood(
        Color.LIME,
        ChatColor.GREEN,
        "木",
        "\uD83C\uDF31" // 🌱
    ),
    Holy(
        Color.YELLOW,
        ChatColor.YELLOW,
        "聖",
        "\uD83C\uDF1F" // 🌟
    ),
    Dark(
        Color.PURPLE,
        ChatColor.LIGHT_PURPLE,
        "闇",
        "\uD83D\uDC80" // 💀
    ),
    None(
        Color.GRAY,
        ChatColor.GRAY,
        "無",
        "\uD83E\uDDB4" // 🦴
    );
}
