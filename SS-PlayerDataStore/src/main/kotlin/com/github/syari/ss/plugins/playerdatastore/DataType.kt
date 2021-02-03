package com.github.syari.ss.plugins.playerdatastore

abstract class DataType(val playerData: PlayerData) {
    abstract val isEnable: Boolean

    var isLoaded = false

    abstract fun save()
}
