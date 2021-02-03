package com.github.syari.ss.plugins.playerdatastore

abstract class LoadableDataType(playerData: PlayerData) : DataType(playerData) {
    abstract fun load()
}
