package com.github.syari.ss.plugins.core.config.type

import com.github.syari.ss.plugins.core.config.CustomConfig
import com.github.syari.ss.plugins.core.config.CustomFileConfig
import com.github.syari.ss.plugins.core.config.type.data.ConfigBooleanDataType
import com.github.syari.ss.plugins.core.config.type.data.ConfigDateDataType
import com.github.syari.ss.plugins.core.config.type.data.ConfigFloatDataType
import com.github.syari.ss.plugins.core.config.type.data.ConfigIntDataType
import com.github.syari.ss.plugins.core.config.type.data.ConfigInventoryDataType
import com.github.syari.ss.plugins.core.config.type.data.ConfigItemConverter
import com.github.syari.ss.plugins.core.config.type.data.ConfigItemsDataType
import com.github.syari.ss.plugins.core.config.type.data.ConfigLocationDataType
import com.github.syari.ss.plugins.core.config.type.data.ConfigLongDataType
import com.github.syari.ss.plugins.core.config.type.data.ConfigMaterialDataType
import com.github.syari.ss.plugins.core.config.type.data.ConfigMySQLDataType
import com.github.syari.ss.plugins.core.config.type.data.ConfigNumberDataType
import com.github.syari.ss.plugins.core.config.type.data.ConfigParticleDataType
import com.github.syari.ss.plugins.core.config.type.data.ConfigPotionDataType
import com.github.syari.ss.plugins.core.config.type.data.ConfigSoundDataType
import com.github.syari.ss.plugins.core.config.type.data.ConfigStringDataType
import com.github.syari.ss.plugins.core.config.type.data.ConfigStringListDataType
import com.github.syari.ss.plugins.core.config.type.data.ConfigVector3DDataType
import com.github.syari.ss.plugins.core.config.type.data.ConfigWorldDataType

/**
 * コンフィグデータタイプ
 * @param T データ型
 */
interface ConfigDataType<T> {
    /**
     * データ型の名前
     */
    val typeName: String

    /**
     * @param config [CustomConfig]
     * @param path コンフィグパス
     * @param notFoundError 存在しないデータの場合にエラーを出す
     */
    fun get(
        config: CustomConfig,
        path: String,
        notFoundError: Boolean
    ): T?

    /**
     * @param config [CustomConfig]
     * @param path コンフィグパス
     * @param notFoundError 存在しないデータの場合にエラーを出す
     * @param default デフォルト値
     */
    fun get(
        config: CustomConfig,
        path: String,
        notFoundError: Boolean,
        default: T
    ): T {
        return get(config, path, notFoundError) ?: default
    }

    interface WithSet<T> : ConfigDataType<T> {
        /**
         * @param config [CustomFileConfig]
         * @param path コンフィグパス
         * @param value 設定する値
         */
        fun set(
            config: CustomFileConfig,
            path: String,
            value: T?
        )
    }

    companion object {
        val NUMBER = ConfigNumberDataType
        val INT = ConfigIntDataType
        val LONG = ConfigLongDataType
        val FLOAT = ConfigFloatDataType
        val STRING = ConfigStringDataType
        val STRINGLIST = ConfigStringListDataType
        val BOOLEAN = ConfigBooleanDataType
        val DATE = ConfigDateDataType
        val LOCATION = ConfigLocationDataType
        val VECTOR3D = ConfigVector3DDataType
        val VECTOR5D = ConfigVector3DDataType
        val WORLD = ConfigWorldDataType
        val MATERIAL = ConfigMaterialDataType
        fun ITEMS(itemConverter: ConfigItemConverter) = ConfigItemsDataType(itemConverter)
        fun INVENTORY(itemConverter: ConfigItemConverter) = ConfigInventoryDataType(itemConverter)
        val PARTICLE = ConfigParticleDataType
        val POTION = ConfigPotionDataType
        val SOUND = ConfigSoundDataType
        val MYSQL = ConfigMySQLDataType
    }
}
