package com.github.syari.ss.plugins.core.scoreboard

import org.bukkit.entity.Player

interface CustomScoreBoardEdit {
    fun space() = line("")

    fun line(text: String)

    fun line(text: Player.() -> String)
}