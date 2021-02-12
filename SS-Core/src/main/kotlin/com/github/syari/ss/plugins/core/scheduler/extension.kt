package com.github.syari.ss.plugins.core.scheduler

import com.github.syari.spigot.api.scheduler.runTask
import com.github.syari.spigot.api.scheduler.runTaskLater
import org.bukkit.plugin.java.JavaPlugin

typealias TaskId = Int

/**
 * @param listWithDelay キーを待機時間としたマップ
 * @param action 待機後に実行する処理
 * @return [Set]<[TaskId]>
 */
@OptIn(ExperimentalStdlibApi::class)
fun <T> JavaPlugin.runTaskLaterList(
    listWithDelay: Map<Long, Set<T>>,
    action: (T) -> Unit
): Set<TaskId> {
    return buildSet {
        listWithDelay.forEach { (delay, value) ->
            runTaskLater(delay, true) {
                runTask(false) {
                    value.forEach {
                        action(it)
                    }
                }
                remove(taskId)
            }.taskId.let(::add)
        }
    }
}
