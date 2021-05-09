package com.github.syari.ss.wplugins.core.command

import com.github.syari.ss.wplugins.core.Main.Companion.plugin
import net.md_5.bungee.api.connection.ProxiedPlayer

open class CommandArgument internal constructor(
    private val array: Array<out String>
) {
    /**
     * 指定した要素を取得します
     * @param index 取得する位置
     * @return String
     * @exception IndexOutOfBoundsException 範囲外の要素を指定
     */
    operator fun get(index: Int) = array[index]

    /**
     * 指定した要素を取得します。存在しなければ null を返します
     * @param index 取得する位置
     * @return [String]?
     */
    fun getOrNull(index: Int) = array.getOrNull(index)

    /**
     * getOrNull(index)の戻り値を小文字に変換します
     * ```
     * when(args.whenIndex(0)){
     *      ...
     * }
     * ```
     * @param index 取得する位置
     * @return String?
     */
    fun whenIndex(index: Int) = getOrNull(index)?.toLowerCase()

    /**
     * 指定したオプションフラグの値を取得します
     * @return [String]?
     */
    fun getFlag(target: String): String? {
        val lowerTarget = target.toLowerCase()
        array.forEachIndexed { index, element ->
            if (lowerTarget == element.toLowerCase()) {
                return getOrNull(index + 1)?.toLowerCase()
            }
        }
        return null
    }

    /**
     * 引数の数
     */
    val size = array.size

    /**
     * 引数の数が0であれば真
     */
    val isEmpty = array.isEmpty()

    /**
     * 引数の数が0でなければ真
     */
    val isNotEmpty = array.isNotEmpty()

    /**
     * 引数を List として取得します
     */
    val toList by lazy { array.toList() }

    /**
     * 引数を結合します
     * @param separator 区切り文字 default: ", "
     */
    fun joinToString(separator: CharSequence = ", ") = array.joinToString(separator)

    /**
     * 指定範囲のみ取得します
     * @param first 範囲の始まり
     * @return [List]<[String]>
     */
    fun slice(first: Int) = slice(first until size)

    /**
     * 指定範囲のみ取得します
     * @param range 範囲
     * @return [List]<[String]>
     */
    fun slice(range: IntRange) = array.slice(range)

    /**
     * 指定した要素をプレイヤーに変換します
     * @param index 取得する位置
     * @param equalName 名前が完全一致した場合のみ取得する
     * @return [ProxiedPlayer]?
     */
    open fun getPlayer(
        index: Int,
        equalName: Boolean
    ): ProxiedPlayer? {
        return getOrNull(index)?.let { rawPlayer ->
            val player = plugin.proxy.getPlayer(rawPlayer)
            return if (player != null && (!equalName || player.name.equals(rawPlayer, ignoreCase = true))) {
                player
            } else {
                null
            }
        }
    }
}
