package com.github.syari.ss.plugins.core.config

import com.github.syari.ss.plugins.core.config.dataType.ConfigDataType
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin

interface CustomConfig {
    val plugin: JavaPlugin
    val config: YamlConfiguration

    /**
     * @param path コンフィグパス
     * @param typeName データ型の名前
     * @param notFoundError 存在しないデータの場合にエラーを出す default: true
     * @return [T]?
     */
    fun <T> getUnsafe(
        path: String, typeName: String, notFoundError: Boolean
    ): T? {
        if (config.contains(path)) {
            val getValue = config.get(path)
            try {
                @Suppress("UNCHECKED_CAST") return getValue as T
            } catch (ex: ClassCastException) {
                typeMismatchError(path, typeName)
            }
        } else if (notFoundError) {
            notFoundError(path)
        }
        return null
    }

    /**
     * @param path コンフィグパス
     * @param typeName データ型の名前
     * @param notFoundError 存在しないデータの場合にエラーを出す default: true
     * @return [List]<[T]>?
     */
    fun <T> getListUnsafe(
        path: String, typeName: String, notFoundError: Boolean = true
    ): List<T>? {
        return mutableListOf<T>().apply {
            if (config.isList(path)) {
                getUnsafe<List<*>>(path, "List<$typeName>", notFoundError)?.forEachIndexed { index, each ->
                    try {
                        @Suppress("UNCHECKED_CAST") add(each as T)
                    } catch (ex: ClassCastException) {
                        typeMismatchError("$path:$index", typeName)
                    }
                }
            } else {
                getUnsafe<T>(path, typeName, notFoundError)?.let {
                    add(it)
                }
            }
        }
    }

    /**
     * @param path コンフィグパス
     * @param type データタイプ
     * @param notFoundError 存在しないデータの場合にエラーを出す default: true
     */
    fun <T> get(
        path: String, type: ConfigDataType<T>, notFoundError: Boolean = true
    ): T? {
        return type.get(this, path, notFoundError)
    }

    /**
     * @param path コンフィグパス
     * @param type データタイプ
     * @param default デフォルト値
     * @param notFoundError 存在しないデータの場合にエラーを出す default: true
     */
    fun <T> get(
        path: String, type: ConfigDataType<T>, default: T, notFoundError: Boolean = true
    ): T {
        return get(path, type, notFoundError) ?: default
    }

    /**
     * コンフィグセクションを取得する
     * @param path コンフィグパス
     * @param notFoundError 存在しないデータの場合にエラーを出す default: true
     */
    fun section(
        path: String, notFoundError: Boolean = true
    ): Set<String>? {
        val section = config.getConfigurationSection(path)?.getKeys(false)
        return section.apply { if (section == null && notFoundError) notFoundError(path) }
    }

    /**
     * 存在するコンフィグパスかを取得する
     * @param path コンフィグパス
     */
    fun contains(path: String) = config.contains(path)

    /**
     * エラーを出力します
     * @param path コンフィグパス
     * @param message 本文
     */
    fun sendError(
        path: String, message: String
    )

    /**
     * ```
     * Format: "$thing が null です"
     * ```
     * @param path コンフィグパス
     * @param thing データ名
     */
    fun nullError(
        path: String, thing: String
    ) {
        sendError(path, "$thing が null です")
    }

    /**
     * ```
     * Format: "フォーマットを間違っています"
     * ```
     * @param path コンフィグパス
     */
    fun formatMismatchError(path: String) {
        sendError(path, "フォーマットを間違っています")
    }

    /**
     * ```
     * Format: "データタイプが $typeName ではありませんでした"
     * ```
     * @param path コンフィグパス
     * @param typeName データタイプ
     */
    fun typeMismatchError(
        path: String, typeName: String
    ) {
        sendError(path, "データタイプが $typeName ではありませんでした")
    }

    /**
     * ```
     * Format: "見つかりませんでした"
     * ```
     * @param path コンフィグパス
     */
    fun notFoundError(path: String) {
        sendError(path, "見つかりませんでした")
    }
}