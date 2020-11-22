package com.github.syari.ss.plugins.votifier.net.protocol.v1

import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.spec.RSAKeyGenParameterSpec
import javax.crypto.Cipher

object RSA {
    /**
     * RSA暗号を解読します
     * @param data データ
     * @param key 秘密鍵
     * @return 解読データ
     */
    @Throws(Exception::class)
    fun decrypt(
        data: ByteArray,
        key: PrivateKey
    ): ByteArray {
        val cipher = Cipher.getInstance("RSA")
        cipher.init(Cipher.DECRYPT_MODE, key)
        return cipher.doFinal(data)
    }

    /**
     * 鍵を生成します
     * @param bits ビット数
     * @return [KeyPair]
     */
    @Throws(Exception::class)
    fun generate(bits: Int): KeyPair {
        val keygen = KeyPairGenerator.getInstance("RSA")
        val spec = RSAKeyGenParameterSpec(
            bits, RSAKeyGenParameterSpec.F4
        )
        keygen.initialize(spec)
        return keygen.generateKeyPair()
    }
}