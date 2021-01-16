@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.github.syari.ss.plugins.core.persistentData

import org.bukkit.NamespacedKey
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.java.JavaPlugin

/**
 * [PersistentDataContainer] 拡張クラス
 */
class CustomPersistentData internal constructor(
    private val plugin: JavaPlugin,
    private val persistentDataContainer: PersistentDataContainer
) {
    private val String.asNamespacedKey get() = NamespacedKey(plugin, this)

    /**
     * 存在するか取得します
     * @param key キー (小文字英数字、ピリオド、アンダースコア、ハイフン)
     * @param type データタイプ
     * @return [Boolean]
     */
    fun <T, Z> has(
        key: String,
        type: PersistentDataType<T, Z>
    ): Boolean {
        return persistentDataContainer.has(key.asNamespacedKey, type)
    }

    /**
     * データを取得します
     * @param key キー (小文字英数字、ピリオド、アンダースコア、ハイフン)
     * @param type データタイプ
     * @return [Z]?
     */
    fun <T, Z> get(
        key: String,
        type: PersistentDataType<T, Z>
    ): Z? {
        return if (has(key, type)) {
            persistentDataContainer.get(key.asNamespacedKey, type)
        } else {
            null
        }
    }

    /**
     * データを取得します
     * @param key キー (小文字英数字、ピリオド、アンダースコア、ハイフン)
     * @param type データタイプ
     * @param default デフォルト値
     * @return [Z]
     */
    fun <T, Z> get(
        key: String,
        type: PersistentDataType<T, Z>,
        default: Z
    ): Z {
        return get(key, type) ?: default
    }

    /**
     * データを設定します
     * @param key キー (小文字英数字、ピリオド、アンダースコア、ハイフン)
     * @param type データタイプ
     * @param value 値
     */
    fun <T, Z> set(
        key: String,
        type: PersistentDataType<T, Z>,
        value: Z?
    ) {
        if (value != null) {
            persistentDataContainer.set(key.asNamespacedKey, type, value)
        } else {
            remove(key)
        }
    }

    /**
     * データを削除します
     * @param key キー (小文字英数字、ピリオド、アンダースコア、ハイフン)
     */
    fun remove(key: String) {
        persistentDataContainer.remove(key.asNamespacedKey)
    }
}
