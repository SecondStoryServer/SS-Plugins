@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.github.syari.ss.plugins.core.config.type.section

import com.github.syari.ss.plugins.core.config.CustomConfig
import com.github.syari.ss.plugins.core.config.type.ConfigSectionType

object ConfigIntSectionType : ConfigSectionType<Int> {
    override val typeName = "Int"

    override fun parse(
        config: CustomConfig,
        path: String,
        value: String
    ): Int? {
        return value.toIntOrNull() ?: config.typeMismatchError(path, typeName).run { null }
    }
}
