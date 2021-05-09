package com.github.syari.ss.wplugins.core.config

import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.plugin.Plugin
import java.io.File
import java.io.Reader

object CreateConfig {
    /**
     * コンフィグをロードします
     * @param output メッセージの出力先
     * @param fileName ファイル名 最後は必ず.yml
     * @param deleteIfEmpty 中身が存在しなければ消去する default: true
     * @return [CustomFileConfig]
     */
    fun Plugin.config(
        output: CommandSender,
        fileName: String,
        deleteIfEmpty: Boolean = true,
        default: Map<String, Any> = emptyMap()
    ): CustomFileConfig {
        var directory = dataFolder
        if (!directory.exists()) directory.mkdir()
        fileName.split("/".toRegex()).forEach { file ->
            if (file.endsWith(".yml")) {
                return CustomFileConfig(this, output, file, directory, deleteIfEmpty, default)
            } else {
                directory = File(directory, file)
                if (!directory.exists()) {
                    directory.mkdir()
                }
            }
        }
        throw IllegalArgumentException("ファイル名の最後が .yml ではありません ($fileName)")
    }

    /**
     * コンフィグをロードします
     * @param output メッセージの出力先
     * @param fileName ファイル名 最後は必ず.yml
     * @param deleteIfEmpty 中身が存在しなければ消去する default: true
     * @param run コンフィグに対して実行する処理
     * @return [CustomFileConfig]
     */
    fun Plugin.config(
        output: CommandSender,
        fileName: String,
        deleteIfEmpty: Boolean = true,
        default: Map<String, Any> = emptyMap(),
        run: CustomFileConfig.() -> Unit
    ): CustomFileConfig {
        return config(output, fileName, deleteIfEmpty, default).apply(run)
    }

    /**
     * コンフィグをロードします
     * @param output メッセージの出力先
     * @param uniqueName 識別名
     * @param reader コンフィグの内容
     * @return [CustomFileConfig]
     */
    fun Plugin.config(
        output: CommandSender,
        uniqueName: String,
        reader: Reader
    ): CustomReaderConfig {
        return CustomReaderConfig(this, output, uniqueName, reader)
    }

    /**
     * コンフィグをロードします
     * @param output メッセージの出力先
     * @param uniqueName 識別名
     * @param reader コンフィグの内容
     * @param run コンフィグに対して実行する処理
     * @return [CustomFileConfig]
     */
    fun Plugin.config(
        output: CommandSender,
        uniqueName: String,
        reader: Reader,
        run: CustomReaderConfig.() -> Unit
    ): CustomReaderConfig {
        return config(output, uniqueName, reader).apply(run)
    }

    /**
     * フォルダ内のコンフィグを全てロードします
     * @param output メッセージの出力先
     * @param directoryName フォルダ名
     * @param deleteIfEmpty 中身が存在しなければ消去する default: true
     * @return [Map]<[String], [CustomFileConfig]>
     */
    fun Plugin.configDir(
        output: CommandSender,
        directoryName: String,
        deleteIfEmpty: Boolean = true
    ): Map<String, CustomFileConfig> {
        var directory = dataFolder
        if (!directory.exists()) directory.mkdir()
        directoryName.split("/".toRegex()).forEach { subDirectory ->
            directory = File(directory, subDirectory)
            if (!directory.exists()) directory.mkdir()
        }
        return mutableMapOf<String, CustomFileConfig>().apply {
            directory.list()?.forEach { fileName ->
                if (fileName.endsWith(".yml")) {
                    this[fileName] = CustomFileConfig(this@configDir, output, fileName, directory, deleteIfEmpty)
                }
            }
        }
    }

    /**
     * フォルダ内のコンフィグを全てロードします
     * @param output メッセージの出力先
     * @param directoryName フォルダ名
     * @param deleteIfEmpty 中身が存在しなければ消去する default: true
     * @param run コンフィグに対して実行する処理
     * @return [Map]<[String], [CustomFileConfig]>
     */
    fun Plugin.configDir(
        output: CommandSender,
        directoryName: String,
        deleteIfEmpty: Boolean = true,
        run: CustomFileConfig.() -> Unit
    ) {
        configDir(output, directoryName, deleteIfEmpty).values.onEach { config ->
            run.invoke(config)
        }
    }

    /**
     * 存在するコンフィグファイルかを取得する
     * @param fileName 対象のファイル名 最後は必ず.yml
     * @return [Boolean]
     */
    fun Plugin.containsDataFile(
        fileName: String
    ): Boolean {
        var directory = dataFolder
        if (!directory.exists()) return false
        fileName.split("/".toRegex()).forEach { file ->
            if (file.endsWith(".yml")) {
                directory.list()?.forEach { directoryContent ->
                    if (file == directoryContent) {
                        return true
                    }
                }
            } else {
                directory = File(directory, file)
                if (!directory.exists()) {
                    return false
                }
            }
        }
        return false
    }
}
