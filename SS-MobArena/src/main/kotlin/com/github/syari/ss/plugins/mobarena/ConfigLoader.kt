package com.github.syari.ss.plugins.mobarena

import com.github.syari.spigot.api.config.configDirectory
import com.github.syari.spigot.api.config.type.ConfigDataType
import com.github.syari.spigot.api.config.type.ConfigSectionType
import com.github.syari.ss.plugins.core.code.IConfigLoader
import com.github.syari.ss.plugins.core.config.ConfigItemConverter
import com.github.syari.ss.plugins.core.item.itemStack
import com.github.syari.ss.plugins.core.message.Message.send
import com.github.syari.ss.plugins.dependency.crackshotplus.CrackShotPlusAPI
import com.github.syari.ss.plugins.dependency.mythicmobs.MythicMobsAPI
import com.github.syari.ss.plugins.mobarena.Main.Companion.plugin
import com.github.syari.ss.plugins.mobarena.arena.Area
import com.github.syari.ss.plugins.mobarena.arena.MobArena
import com.github.syari.ss.plugins.mobarena.kit.MobArenaKit
import com.github.syari.ss.plugins.mobarena.lobby.ServerSelector
import com.github.syari.ss.plugins.mobarena.shop.Shop
import com.github.syari.ss.plugins.mobarena.shop.ShopAction
import com.github.syari.ss.plugins.mobarena.shop.ShopElement
import com.github.syari.ss.plugins.mobarena.wave.MobArenaWave
import com.github.syari.ss.plugins.mobarena.wave.boss.MobArenaBoss
import com.github.syari.ss.plugins.mobarena.wave.boss.MobArenaMythicMobsBoss
import com.github.syari.ss.plugins.mobarena.wave.mob.MobArenaMob
import com.github.syari.ss.plugins.mobarena.wave.mob.MobArenaMythicMobsMob
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.inventory.ItemStack

object ConfigLoader : IConfigLoader {
    override fun load(sender: CommandSender) {
        loadArena(sender)
        loadKit(sender)
        loadShop(sender)
        ServerSelector.load(sender)
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun loadArena(sender: CommandSender) {
        val arenas = mutableListOf<MobArena>()
        plugin.configDirectory(sender, "MobArena") {
            val id = file.nameWithoutExtension
            val name = get("name", ConfigDataType.String, id)
            val lobby = get("lobby", Area.ConfigDataType) ?: return@configDirectory
            val play = get("play", Area.ConfigDataType) ?: return@configDirectory
            val spec = get("spec", Area.ConfigDataType) ?: return@configDirectory
            val spawn = get("spawn", ConfigDataType.Location) ?: return@configDirectory nullError("spawn", "Location")
            val kits = get("kit", ConfigDataType.StringList, listOf())
            val playerLimit = get("limit.player", ConfigDataType.Int, 5)
            val kitLimit = get("limit.kit", ConfigDataType.Int, 1, false)
            val entityLimit = get("limit.entity", ConfigDataType.Int, 30, false)
            val waveInterval = get("wave-interval", ConfigDataType.Long, 200, false)
            val arena = MobArena(id, name, kits, lobby, play, spec, spawn, waveInterval, playerLimit, kitLimit, entityLimit)
            val waves = section("wave", ConfigSectionType.Int)?.toMutableList()
            if (waves != null) {
                waves.sort()
                if (waves.first() == 1) {
                    var lastWave = 1
                    arena.waveList = buildList {
                        var stop = false
                        var mobAmount = 5
                        var mobs = listOf<MobArenaMob>()
                        var boss: MobArenaBoss? = null
                        var upgradeItem = listOf<ItemStack>()
                        waves.forEach { wave ->
                            if (wave != 1) add(
                                MobArenaWave(
                                    arena, lastWave until wave, mobAmount, stop, mobs, boss, upgradeItem
                                )
                            )
                            stop = get("wave.$wave.stop", ConfigDataType.Boolean, false, notFoundError = false)
                            mobAmount = get("wave.$wave.mob.amount", ConfigDataType.Int, mobAmount, false)
                            mobs = section("wave.$wave.mob.list", false)?.map {
                                val priority = get("wave.$wave.mob.list.$it", ConfigDataType.Int, 1)
                                MobArenaMythicMobsMob(it, priority)
                            } ?: mobs
                            boss = get("wave.$wave.boss", ConfigDataType.String, false)?.let {
                                MobArenaMythicMobsBoss(it)
                            }
                            upgradeItem = get("wave.$wave.upgrade", ConfigDataType.ItemStackList(itemConverter), listOf(), false)
                            lastWave = wave
                        }
                        add(MobArenaWave(arena, lastWave..lastWave, mobAmount, true, mobs, boss, upgradeItem))
                    }
                    arena.lastWave = lastWave
                } else {
                    sendError("", "Wave 1 is not defined")
                }
            } else {
                sendError("", "Wave is Empty")
            }
            arenas.add(arena)
        }
        MobArenaManager.arenas = arenas
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun loadKit(sender: CommandSender) {
        MobArenaKit.kits = buildMap {
            plugin.configDirectory(sender, "Kit") {
                val id = file.nameWithoutExtension
                val name = get("name", ConfigDataType.String, id)
                val icon = get("icon", ConfigDataType.ItemStack(itemConverter), itemStack(Material.STONE))
                val description = get("description", ConfigDataType.StringList).orEmpty()
                val difficulty = get("difficulty", ConfigDataType.Int, 1)
                val items = get("items", ConfigDataType.Inventory(itemConverter), mapOf())
                put(id, MobArenaKit(id, name, icon, description, difficulty, items))
            }
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun loadShop(sender: CommandSender) {
        Shop.list = buildMap {
            plugin.configDirectory(sender, "Shop") {
                section("")?.forEach { id ->
                    val name = get("$id.name", ConfigDataType.String, id)
                    val line = get("$id.line", ConfigDataType.Int, 3, false)
                    val list = mutableMapOf<Int, ShopAction>()
                    section("$id.list", ConfigSectionType.Int, false)?.forEach { slot ->
                        val elements = get("$id.list.$slot", ShopElement.ConfigDataType)
                        ShopAction.from(elements)?.let { list[slot] = it }
                    }
                    this@buildMap[id] = Shop(name, line, list)
                }
            }
        }
        sender.send("&b[MobArena] &a${Shop.list.size} &f個のショップを読み込みました")
    }

    private val itemTypeMap = mapOf<String, (String) -> ItemStack?>(
        "mc" to {
            val typeName = it.substringBefore(':').toUpperCase()
            val model = it.substringAfter(':', "")
            Material.getMaterial(typeName)?.let { material ->
                itemStack(material, customModelData = model.toIntOrNull())
            }
        },
        "mm" to {
            MythicMobsAPI.getItem(it)
        },
        "cs" to {
            CrackShotPlusAPI.getCrackShotItem(it)
        },
        "csp" to {
            CrackShotPlusAPI.getAttachment(it)
        }
    )

    private val itemConverter = ConfigItemConverter.FromName(itemTypeMap)
}
