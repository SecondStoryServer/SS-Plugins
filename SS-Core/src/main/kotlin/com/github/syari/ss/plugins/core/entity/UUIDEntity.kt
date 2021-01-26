package com.github.syari.ss.plugins.core.entity

import org.bukkit.Bukkit.getEntity
import org.bukkit.entity.Entity
import java.util.UUID

data class UUIDEntity(private val uniqueId: UUID) : Comparable<UUIDEntity> {
    constructor(entity: Entity) : this(entity.uniqueId)

    /**
     *  エンティティに変換します
     */
    val entity get(): Entity? = getEntity(uniqueId)

    /**
     * サーバーに存在するか取得します
     */
    val isDead get() = entity == null

    /**
     * [UUID.compareTo]
     */
    override fun compareTo(other: UUIDEntity) = uniqueId.compareTo(other.uniqueId)

    /**
     * UUIDを文字列として取得します
     */
    override fun toString() = uniqueId.toString()
}
