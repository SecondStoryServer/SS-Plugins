@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.github.syari.ss.plugins.core.persistentData.customType

import org.bukkit.persistence.PersistentDataAdapterContext
import org.bukkit.persistence.PersistentDataType

object PersistentDataTypeBoolean : PersistentDataType<Byte, Boolean> {
    override fun getPrimitiveType(): Class<Byte> {
        return Byte::class.java
    }

    override fun getComplexType(): Class<Boolean> {
        return Boolean::class.java
    }

    override fun toPrimitive(
        complex: Boolean,
        context: PersistentDataAdapterContext
    ): Byte {
        return if (complex) 1 else 0
    }

    override fun fromPrimitive(
        primitive: Byte,
        context: PersistentDataAdapterContext
    ): Boolean {
        return primitive == 1.toByte()
    }
}
