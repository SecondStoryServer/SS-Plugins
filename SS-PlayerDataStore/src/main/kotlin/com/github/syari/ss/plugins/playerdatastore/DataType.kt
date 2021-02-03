package com.github.syari.ss.plugins.playerdatastore

abstract class DataType(val playerData: PlayerData) {
    abstract val isEnable: Boolean

    var isLoaded = false

    fun unload() {
        if (isLoaded) {
            isLoaded = false
            save()
        }
    }

    abstract fun save()
}
