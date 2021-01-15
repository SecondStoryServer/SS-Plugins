package com.github.syari.ss.plugins.core.config.type

import com.github.syari.ss.plugins.core.config.CustomConfig
import com.github.syari.ss.plugins.core.config.type.section.ConfigIntSectionType

/**
 * コンフィグセクションタイプ
 * @param T データ型
 */
interface ConfigSectionType<T> {
    /**
     * データ型の名前
     */
    val typeName: String

    /**
     * @param value セクション名
     */
    fun parse(
        config: CustomConfig,
        path: String,
        value: String
    ): T?

    companion object {
        val INT = ConfigIntSectionType
    }
}
