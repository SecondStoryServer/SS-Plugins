@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.github.syari.ss.plugins.core.command.create

enum class ErrorMessage(val message: String) {
    OnlyPlayer("コンソールから実行できないコマンドです"),
    NotEnterPlayer("プレイヤーを入力してください"),
    NotFoundPlayer("プレイヤーが見つかりませんでした"),
    NotExistName("存在しない名前です"),
    AlreadyExistName("既に存在する名前です"),
    NotEnterName("名前を入力してください"),
    NotExistId("存在しないIDです"),
    AlreadyExistId("既に存在するIDです"),
    NotEnterId("IDを入力してください"),
    NotExist("存在しません"),
    AlreadyExist("既に存在しています"),
    NotEnter("入力してください")
}
