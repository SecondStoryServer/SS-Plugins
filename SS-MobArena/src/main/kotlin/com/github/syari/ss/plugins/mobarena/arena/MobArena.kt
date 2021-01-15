package com.github.syari.ss.plugins.mobarena.arena

import com.github.syari.ss.plugins.core.bossBar.CustomBossBar
import com.github.syari.ss.plugins.core.bossBar.CustomBossBar.Companion.bossBar
import com.github.syari.ss.plugins.core.inventory.CreateInventory.inventory
import com.github.syari.ss.plugins.core.item.ItemStackPlus.give
import com.github.syari.ss.plugins.core.message.Message.broadcast
import com.github.syari.ss.plugins.core.message.Message.send
import com.github.syari.ss.plugins.core.scheduler.CreateScheduler.runLater
import com.github.syari.ss.plugins.core.scheduler.CreateScheduler.runRepeatTimes
import com.github.syari.ss.plugins.core.scheduler.CustomTask
import com.github.syari.ss.plugins.core.scoreboard.CreateScoreBoard.board
import com.github.syari.ss.plugins.mobarena.Main.Companion.plugin
import com.github.syari.ss.plugins.mobarena.MobArenaManager.arenaPlayer
import com.github.syari.ss.plugins.mobarena.MobArenaManager.inMobArena
import com.github.syari.ss.plugins.mobarena.kit.MobArenaKit
import com.github.syari.ss.plugins.mobarena.wave.MobArenaWave
import com.github.syari.ss.plugins.playerdatastore.PlayerData
import org.bukkit.Location
import org.bukkit.attribute.Attribute
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import java.util.UUID

class MobArena(
    val id: String,
    private val name: String,
    val kits: List<String>,
    val lobby: Area,
    val play: Area,
    val spec: Area,
    val mobSpawn: Location,
    private val waveInterval: Long,
    private val playerLimit: Int,
    private val kitLimit: Int
) {
    var players = mutableListOf<MobArenaPlayer>()
    var status = MobArenaStatus.StandBy
    var mobs = mutableListOf<UUID>()
    var wave = 0
    var firstMemberSize = 0

    var waveList = listOf<MobArenaWave>()
    var lastWave = 0
    var waitAllKill = false

    private val board = plugin.board("&a&lMobArena", 1) {
        val arenaPlayer = getPlayer(it)
        """
            &e&m------------------------
            &a&lウェーブ &7≫ &e$wave
            
            &a&l残り人数 &7≫ &e${livingPlayers.count()}人
            
            &a&lキット &7≫ &e${if (arenaPlayer != null && arenaPlayer.play) arenaPlayer.kit?.name ?: "&c未設定" else "&b&l観戦者"}
            &e&m------------------------
        """.trimIndent()
    }

    fun getPlayer(player: Player) = players.firstOrNull { it.player == player }

    val livingPlayers get() = players.filter { it.play }

    private val isEmptyLivingPlayers get() = livingPlayers.isEmpty()

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

    private fun checkReady(): Int {
        val count = players.count { f -> f.play && !f.ready }
        if (count == 0) {
            if (allowStart) {
                start()
            }
        }
        return count
    }

    fun checkReady(p: Player) {
        val count = checkReady()
        announce("&b[MobArena] &a${p.displayName}&fが準備完了しました &f残り${count}人です")
    }

    var bar: CustomBossBar? = null
    var mainTask: CustomTask? = null
    var allowStart = false
    var publicChest = inventory("&0&l共有チェスト", 2) {}

    private fun firstJoin() {
        broadcast("もぶありーなはじまるよ！！！！！！！！！！！")
        allowStart = false
        status = MobArenaStatus.WaitReady
        mainTask = plugin.runRepeatTimes(20, 90) {
            bar?.progress = repeatRemain.toDouble() / 90
        }?.onEndRepeat {
            allowStart = true
            if (checkReady() != 0) {
                announce("&b[MobArena] &f全員が準備完了をしたらゲームを開始します")
            }
        }
    }

    fun join(p: Player) {
        if (status == MobArenaStatus.NowPlay) {
            return p.send("&b[MobArena] &c既にゲームが始まっています /ma-debug s $id で観戦しましょう")
        }
        val m = p.arenaPlayer
        if (m != null) {
            if (m.play) {
                return p.send("&b[MobArena] &c既にモブアリーナに参加しています")
            } else {
                m.arena.leave(p)
            }
        }
        if (playerLimit <= players.size) {
            return p.send("&b[MobArena] &c制限人数に達しています /ma-debug s $id で観戦しましょう")
        }
        if (players.isEmpty()) {
            firstJoin()
        }
        players.add(MobArenaPlayer(this, p, true))
        p.closeInventory()
        PlayerData.saveStoreData(p)
        p.inventory.clear()
        p.teleport(lobby.spawn)
        board.addPlayer(p)
        updateAllBoard()
    }

    fun spec(p: Player) {
        p.closeInventory()
        PlayerData.saveStoreData(p)
        p.inventory.clear()
        players.add(MobArenaPlayer(this, p, false))
        p.teleport(spec.spawn)
        board.addPlayer(p)
    }

    fun leave(p: Player) {
        if (!p.inMobArena) {
            return p.send("&b[MobArena] &cモブアリーナに参加していません")
        }
        val m = getPlayer(p)
        if (m != null) {
            players.remove(m)
        }
        if (isEmptyLivingPlayers && status != MobArenaStatus.StandBy) {
            end(false)
        }
        p.closeInventory()
        p.inventory.clear()
        PlayerData.loadStoreData(p)
        board.removePlayer(p)
        updateAllBoard()
    }

    fun start() {
        bar?.delete()
        bar = bossBar("&e&lWave", BarColor.BLUE, BarStyle.SOLID)
        status = MobArenaStatus.NowPlay
        bar?.delete()
        mainTask?.cancel()
        players.forEach { m ->
            if (m.play) {
                val p = m.player
                p.teleport(play.spawn)
                p.activePotionEffects.clear()
                p.health = p.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.value
                p.foodLevel = 20
                firstMemberSize++
            }
        }
        reloadProgress()
        mainTask = plugin.runLater(10 * 20) {
            nextWave()
        }
    }

    fun end(force: Boolean) {
        when (status) {
            MobArenaStatus.NowPlay -> {
                nextWaveTask?.cancel()
                mobs.toList().forEach {
                    plugin.server.getEntity(it)?.remove()
                }
                mobs.clear()
            }
            MobArenaStatus.WaitReady -> {
                mainTask?.cancel()
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
            broadcast("&b[MobArena] &a$name&fのゲームが終わりました &a/ma-debug j $id &fで始めましょう")
            updateAllBoard()
        }
        wave = 0
        firstMemberSize = 0
        publicChest = inventory("&0&l共有チェスト", 2) {}
        status = MobArenaStatus.StandBy
        reloadProgress()
    }

    fun onDeath(p: Player) {
        plugin.runLater(3) {
            leave(p)
        }
    }

    private fun clearGame() {
        broadcast(
            """
            &b[MobArena] &fモブアリーナ&a$name&fがクリアされました！！
            &fクリア者: &a${livingPlayers.joinToString(", ") { it.player.displayName }}
            """.trimIndent()
        )
        plugin.runLater(10 * 20) {
            livingPlayers.forEach {
                leave(it.player)
            }
            status = MobArenaStatus.StandBy
            players.clear()
        }
    }

    var nextWaveTask: CustomTask? = null

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
            val stop = waveData.stop
            waitAllKill = stop
            if (stop.not()) {
                giveItem(waveData)
                mainTask = plugin.runLater(5 * 20) {
                    nextWaveTask = plugin.runLater(waveInterval) {
                        nextWave()
                    }
                }
            }
            waveData.spawn()
        } else {
            clearGame()
            wave = lastWave
        }
        reloadProgress()
        updateAllBoard()
    }

    var checkDisTask: CustomTask? = null

    private fun checkDis() {
        if (status != MobArenaStatus.NowPlay) return
        if (mobs.isEmpty() && waitAllKill) {
            val waveData = waveList.firstOrNull { w -> wave in w.waveRange } ?: return
            giveItem(waveData)
            nextWave()
        } else {
            checkDisTask?.cancel()
            checkDisTask = plugin.runLater(40 * 20) {
                mobs.removeIf { uuid ->
                    plugin.server.getEntity(uuid) == null
                }
                checkDis()
            }
        }
    }

    fun onKillEntity(e: LivingEntity) {
        mobs.remove(e.uniqueId)
        checkDis()
    }
}
