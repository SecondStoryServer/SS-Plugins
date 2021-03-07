package com.github.syari.ss.plugins.event.acrobatsniper

import com.github.syari.spigot.api.uuid.UUIDPlayer

object EntryList {
    private val list = mutableSetOf<UUIDPlayer>()

    val nameList
        get() = list.mapNotNull { it.offlinePlayer.name }

    var isEnable = false

    fun contains(player: UUIDPlayer): Boolean {
        return list.contains(player)
    }

    fun add(player: UUIDPlayer) {
        list.add(player)
    }

    fun remove(player: UUIDPlayer) {
        list.remove(player)
    }

    fun clear() {
        list.clear()
    }
}
