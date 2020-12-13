package com.github.syari.ss.plugins.ma.shop

object Shop {
    private var list = mapOf<String, ShopData>()

    fun get(id: String) = list[id]

    fun replace(newList: Map<String, ShopData>) {
        list = newList
    }
}