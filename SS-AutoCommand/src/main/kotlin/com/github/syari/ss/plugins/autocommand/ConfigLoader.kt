package com.github.syari.ss.plugins.autocommand

import com.github.syari.ss.plugins.autocommand.Main.Companion.plugin
import com.github.syari.ss.plugins.core.Main.Companion.console
import com.github.syari.ss.plugins.core.code.OnEnable
import com.github.syari.ss.plugins.core.command.RunCommand
import com.github.syari.ss.plugins.core.command.RunCommand.runCommandFromConsole
import com.github.syari.ss.plugins.core.config.CreateConfig.config
import com.github.syari.ss.plugins.core.config.dataType.ConfigDataType
import com.github.syari.ss.plugins.core.message.Message.send
import com.github.syari.ss.plugins.core.time.TimeScheduler.clearTimeScheduler
import com.github.syari.ss.plugins.core.time.TimeScheduler.getFormatTime
import com.github.syari.ss.plugins.core.time.TimeScheduler.scheduleEveryDayAt
import com.github.syari.ss.plugins.core.time.TimeScheduler.scheduleEveryWeekAt
import org.bukkit.command.CommandSender
import java.time.DayOfWeek

object ConfigLoader : OnEnable {
    override fun onEnable() {
        load(console)
    }

    fun load(sender: CommandSender) {
        fun String.toTime() = split(":").let { it.getOrNull(0)?.toIntOrNull() to it.getOrNull(1)?.toIntOrNull() }

        plugin.clearTimeScheduler()
        plugin.config(sender, "config.yml") {
            sender.send("&b[AutoCommand] &f自動コマンド一覧")
            section("every", false)?.forEach {
                val commandList = get("every.$it", ConfigDataType.STRINGLIST) ?: return@forEach
                val (hour, minute) = it.toTime()
                if (hour != null && minute != null) {
                    plugin.scheduleEveryDayAt(hour, minute) {
                        commandList.forEach(RunCommand::runCommandFromConsole)
                    }
                    sender.send("&7- &a${getFormatTime(hour, minute)} &7×${commandList.size}")
                } else {
                    formatMismatchError("every.$it")
                }
            }
            DayOfWeek.values().forEach { day ->
                val dayName = day.name.toLowerCase()
                section(dayName, false)?.forEach nextTime@{
                    val commandList = get("$dayName.$it", ConfigDataType.STRINGLIST) ?: return@nextTime
                    val (hour, minute) = it.toTime()
                    if (hour != null && minute != null) {
                        plugin.scheduleEveryWeekAt(day, hour, minute) {
                            commandList.forEach(RunCommand::runCommandFromConsole)
                        }
                        sender.send("&7- &a$dayName ${getFormatTime(hour, minute)} &7×${commandList.size}")
                    } else {
                        formatMismatchError("$dayName.$it")
                    }
                }
            }
        }
    }
}
