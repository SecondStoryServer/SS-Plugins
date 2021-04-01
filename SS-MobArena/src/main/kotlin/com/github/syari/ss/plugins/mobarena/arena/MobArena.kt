package com.github.syari.ss.plugins.mobarena.arena

import com.github.syari.spigot.api.scheduler.runTaskLater
import com.github.syari.spigot.api.string.toColor
import com.github.syari.spigot.api.uuid.UUIDEntity
import com.github.syari.ss.plugins.core.bossBar.CustomBossBar
import com.github.syari.ss.plugins.core.bossBar.CustomBossBar.Companion.bossBar
import com.github.syari.ss.plugins.core.inventory.CreateInventory.inventory
import com.github.syari.ss.plugins.core.item.ItemStackPlus.give
import com.github.syari.ss.plugins.core.message.Message.broadcast
import com.github.syari.ss.plugins.core.message.Message.send
import com.github.syari.ss.plugins.core.pluginMessage.PluginMessage
import com.github.syari.ss.plugins.core.scoreboard.CreateScoreBoard.board
import com.github.syari.ss.plugins.mobarena.Main.Companion.plugin
import com.github.syari.ss.plugins.mobarena.MobArenaManager.arenaPlayer
import com.github.syari.ss.plugins.mobarena.insertItem
import com.github.syari.ss.plugins.mobarena.kit.MobArenaKit
import com.github.syari.ss.plugins.mobarena.lobby.LobbyInventory
import com.github.syari.ss.plugins.mobarena.wave.MobArenaWave
import com.github.syari.ss.template.message.PluginMessageTemplateChatChannel
import org.bukkit.Location
import org.bukkit.attribute.Attribute
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitTask
import java.util.concurrent.ConcurrentSkipListSet

class MobArena(
    val id: String,
    val name: String,
    val kits: List<String>,
    val lobbyArea: Area,
    val playArea: Area,
    val specArea: Area,
    val mobSpawn: Location,
    private val waveInterval: Long,
    private val playerLimit: Int,
    private val kitLimit: Int,
    private val defaultEntityLimit: Int
) {
    var players = mutableListOf<MobArenaPlayer>()
    var status = MobArenaStatus.StandBy
    var mobs = ConcurrentSkipListSet<UUIDEntity>()
    var wave = 0
    var firstMemberSize = 0

    var waveList = listOf<MobArenaWave>()
    var lastWave = 0
    var entityLimit = defaultEntityLimit

    private val board = plugin.board("&a&lMobArena", 1) {
        val arenaPlayer = getPlayer(it)
        """
            &e&m------------------------
            &a&lウェーブ &7≫ &e$wave
            
            &a&l生存者数 &7≫ &e${livingPlayers.count()}
            
            &a&lキット &7≫ &e${if (arenaPlayer != null && arenaPlayer.play) arenaPlayer.kit?.name ?: "&c未設定" else "&b観戦者"}
            &e&m------------------------
        """.trimIndent()
    }

    private val chatChannel = "mob_arena/$id"

    private fun setChatChannel(player: Player) {
        PluginMessage.send(
            PluginMessageTemplateChatChannel(
                PluginMessageTemplateChatChannel.UpdateTask.AddPlayer,
                chatChannel,
                listOf(player.name)
            )
        )
        PluginMessage.send(
            PluginMessageTemplateChatChannel(
                PluginMessageTemplateChatChannel.UpdateTask.SetSpeaker,
                chatChannel,
                listOf(player.name)
            )
        )
    }

    private fun unsetChatChannel(player: Player) {
        PluginMessage.send(
            PluginMessageTemplateChatChannel(
                PluginMessageTemplateChatChannel.UpdateTask.RemovePlayer,
                chatChannel,
                listOf(player.name)
            )
        )
        PluginMessage.send(
            PluginMessageTemplateChatChannel(
                PluginMessageTemplateChatChannel.UpdateTask.UnSetSpeaker,
                chatChannel,
                listOf(player.name)
            )
        )
    }

    fun getPlayer(player: Player) = players.firstOrNull { it.player == player }

    val livingPlayers get() = players.filter { it.play }

    fun availableKit(kit: MobArenaKit) = kits.contains(kit.id) && players.count { it.kit == kit } < kitLimit

    fun loadKit(arenaPlayer: MobArenaPlayer, kit: MobArenaKit) {
        arenaPlayer.kit = kit
        kit.load(arenaPlayer.player)
        board.updatePlayer(arenaPlayer.player)
    }

    fun announce(msg: String) {
        players.forEach {
            it.player.send(msg)
        }
    }

    private fun updateAllBoard() {
        board.updatePlayer(*players.map { it.player }.toTypedArray())
    }

    private fun reloadProgress() {
        if (status == MobArenaStatus.NowPlay) {
            bar?.title = if (lastWave < wave) "&7>> &e&lAll Clear &7<<" else "&e&lWave &e&l$wave &7/ &e&l$lastWave"
            bar?.progress = wave.toDouble() / lastWave
            players.forEach { bar?.addPlayer(it.player) }
        } else {
            bar?.clearPlayer()
        }
    }

    fun checkReady(player: Player) {
        val count = players.count { it.play && it.ready.not() }
        if (count == 0) {
            start()
        } else {
            announce("&b[MobArena] &a${player.displayName}&fが準備完了しました &f残り${count}人です")
        }
    }

    var nextWaveTask: BukkitTask? = null
    var bar: CustomBossBar? = null
    var publicChest = initChest()

    private fun initChest() = inventory("&0&l共有チェスト", 2) {
        onClick = {
            val player = it.whoClicked as? Player
            if (player != null) {
                val lore = it.insertItem?.lore
                if (lore != null && lore.contains("&c受け渡し不可".toColor())) {
                    it.isCancelled = true
                }
            }
        }
        cancel = false
    }

    private fun firstJoin() {
        broadcast("&b[MobArena] &a$name &fが始まります")
        status = MobArenaStatus.WaitReady
        bar = bossBar("&f&l全員が準備完了をしたらゲームを開始します", BarColor.GREEN, BarStyle.SOLID)
    }

    fun join(player: Player) {
        if (status == MobArenaStatus.NowPlay) {
            return player.send("&b[MobArena] &c既にゲームが始まっています")
        }
        val arenaPlayer = player.arenaPlayer
        if (arenaPlayer != null) {
            if (arenaPlayer.play) {
                return player.send("&b[MobArena] &c既にモブアリーナに参加しています")
            } else {
                arenaPlayer.arena.leave(player)
            }
        } else {
            player.inventory.clear()
        }
        if (playerLimit <= players.size) {
            return player.send("&b[MobArena] &c制限人数に達しています")
        }
        if (players.isEmpty()) {
            firstJoin()
        }
        players.add(MobArenaPlayer(this, player, true))
        player.closeInventory()
        player.teleport(lobbyArea.spawn)
        bar?.addPlayer(player)
        board.addPlayer(player)
        setChatChannel(player)
        updateAllBoard()
    }

    fun spec(player: Player) {
        val arenaPlayer = player.arenaPlayer
        if (arenaPlayer != null) {
            if (arenaPlayer.play) {
                arenaPlayer.arena.leave(player)
            } else {
                return player.send("&b[MobArena] &c既にモブアリーナに参加しています")
            }
        } else {
            player.inventory.clear()
        }
        player.closeInventory()
        players.add(MobArenaPlayer(this, player, false))
        player.teleport(specArea.spawn)
        board.addPlayer(player)
    }

    fun leave(player: Player) {
        val arenaPlayer = getPlayer(player)
        if (arenaPlayer != null) {
            players.remove(arenaPlayer)
        } else {
            return player.send("&b[MobArena] &cモブアリーナに参加していません")
        }
        if (livingPlayers.isEmpty() && status != MobArenaStatus.StandBy) {
            end(false)
        }
        player.closeInventory()
        LobbyInventory.applyToPlayer(player)
        player.teleport(player.world.spawnLocation)
        bar?.removePlayer(player)
        board.removePlayer(player)
        unsetChatChannel(player)
        updateAllBoard()
    }

    fun start() {
        bar?.delete()
        bar = bossBar("&e&lWave", BarColor.BLUE, BarStyle.SOLID)
        status = MobArenaStatus.NowPlay
        players.forEach {
            if (it.play) {
                val player = it.player
                player.teleport(playArea.spawn)
                player.activePotionEffects.clear()
                player.health = player.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.value
                player.foodLevel = 20
                firstMemberSize++
            }
        }
        reloadProgress()
        checkEntityCount()
    }

    fun end(force: Boolean) {
        when (status) {
            MobArenaStatus.NowPlay -> {
                nextWaveTask?.cancel()
                mobs.toList().forEach {
                    it.entity?.remove()
                }
                mobs.clear()
            }
            MobArenaStatus.WaitReady -> {
                nextWaveTask?.cancel()
                bar?.delete()
            }
            MobArenaStatus.StandBy -> return
        }
        if (force) {
            announce("&b[MobArena] &f強制終了しました")
            players.toList().forEach {
                leave(it.player)
            }
            players.clear()
        } else {
            broadcast("&b[MobArena] &a$name&fのゲームが終わりました")
            updateAllBoard()
        }
        wave = 0
        firstMemberSize = 0
        nextWaveTask = null
        publicChest = initChest()
        status = MobArenaStatus.StandBy
        reloadProgress()
    }

    fun onDeath(player: Player) {
        plugin.runTaskLater(3) {
            leave(player)
        }
    }

    private fun clearGame() {
        broadcast(
            """
            &b[MobArena] &fモブアリーナ&a$name&fがクリアされました！！
            &fクリア者: &a${livingPlayers.joinToString(", ") { it.player.displayName }}
            """.trimIndent()
        )
        plugin.runTaskLater(10 * 20) {
            livingPlayers.forEach {
                leave(it.player)
            }
            status = MobArenaStatus.StandBy
            players.clear()
        }
    }

    private fun giveItem(waveData: MobArenaWave) {
        players.forEach {
            if (it.play) {
                it.player.give(waveData.upgrade)
            }
        }
    }

    fun nextWave() {
        if (status != MobArenaStatus.NowPlay) return
        wave++
        val waveData = waveList.firstOrNull { wave in it.waveRange }
        if (wave < lastWave + 1 && waveData != null) {
            announce("&b[MobArena] &a${wave}ウェーブ&fに突入します")
            giveItem(waveData)
            entityLimit = if (waveData.stop) {
                1
            } else {
                defaultEntityLimit
            }
            waveData.spawn()
        } else {
            clearGame()
            wave = lastWave
        }
        reloadProgress()
        updateAllBoard()
        nextWaveTask = null
    }

    var checkEntityTask: BukkitTask? = null
    var checkDeadEntityTask: BukkitTask? = null

    private fun checkEntityCount() {
        if (status != MobArenaStatus.NowPlay) return
        if (mobs.size < entityLimit) {
            if (nextWaveTask == null) {
                nextWaveTask = plugin.runTaskLater(waveInterval) {
                    nextWave()
                    checkEntityTask?.cancel()
                    checkEntityTask = plugin.runTaskLater(waveInterval) {
                        checkEntityCount()
                    }
                }
            }
        }
        checkDeadEntityTask?.cancel()
        checkDeadEntityTask = plugin.runTaskLater(5 * 20) {
            mobs.removeIf { it.entity?.isDead != false }
            checkEntityCount()
        }
    }

    fun onKillEntity(entity: LivingEntity) {
        mobs.remove(UUIDEntity.from(entity))
        checkEntityCount()
    }
}
