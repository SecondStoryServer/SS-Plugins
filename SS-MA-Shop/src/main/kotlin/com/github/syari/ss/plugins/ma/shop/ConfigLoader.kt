package com.github.syari.ss.plugins.ma.shop

import com.github.syari.ss.plugins.core.Main.Companion.console
import com.github.syari.ss.plugins.core.code.OnEnable
import com.github.syari.ss.plugins.core.config.CreateConfig.configDir
import com.github.syari.ss.plugins.core.config.dataType.ConfigDataType
import com.github.syari.ss.plugins.core.message.Message.send
import com.github.syari.ss.plugins.ma.shop.Main.Companion.plugin
import org.bukkit.command.CommandSender

object ConfigLoader: OnEnable {
    override fun onEnable() {
        loadConfig(console)
    }

    fun loadConfig(sender: CommandSender) {
        configDir(plugin, sender, "data") {
            val shopList = mutableMapOf<String, ShopData>()
            section("")?.forEach { id ->
                val name = get("$id.name", ConfigDataType.STRING, id)
                val line = get("$id.line", ConfigDataType.INT, 3, false)
                val list = mutableMapOf<Int, ShopBuyAction>()
                section("$id.list", false)?.forEach { rawSlot ->
                    val slot = rawSlot.toIntOrNull()
                    if (slot != null) {
                        val elements = get("$id.list.$rawSlot", ConfigDataType.STRINGLIST)?.map(ShopElement::from)
                        ShopBuyAction.from(elements)?.let { list[slot] = it }
                    } else {
                        nullError("$id.list.$rawSlot", "Int")
                    }
                }
                val data = ShopData(name, line, list)
                shopList[id] = data
            }
            sender.send("&b[MA_Shop] &a${shopList.size} &f個のショップを読み込みました")
            Shop.replace(shopList)
        }
    }
}