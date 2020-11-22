package com.github.syari.ss.plugins.votifier.util

import java.nio.charset.StandardCharsets
import java.security.Key
import javax.crypto.spec.SecretKeySpec

object KeyCreator {
    /**
     * トークンから鍵を生成します
     * @param token トークン
     * @return [Key]
     */
    fun createKeyFrom(token: String): Key {
        return SecretKeySpec(token.toByteArray(StandardCharsets.UTF_8), "HmacSHA256")
    }
}