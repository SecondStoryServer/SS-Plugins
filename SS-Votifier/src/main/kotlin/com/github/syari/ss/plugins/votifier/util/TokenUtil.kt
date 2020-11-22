package com.github.syari.ss.plugins.votifier.util

import java.math.BigInteger
import java.security.SecureRandom

object TokenUtil {
    private val RANDOM = SecureRandom()

    /**
     * 新しいトークンを取得します
     * @return [String]
     */
    fun newToken(): String {
        return BigInteger(130, RANDOM).toString(32)
    }
}