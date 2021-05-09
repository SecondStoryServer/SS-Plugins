package com.github.syari.ss.wplugins.votifier.net.protocol.v1

import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.security.KeyFactory
import java.security.KeyPair
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.Base64

object RSAIO {
    /**
     * 鍵をディレクトリに保存します
     * @param directory ディレクトリ
     * @param keyPair 鍵
     * @exception [Exception]
     */
    @Throws(Exception::class)
    fun save(
        directory: File,
        keyPair: KeyPair
    ) {
        val publicKey = keyPair.public
        val publicSpec = X509EncodedKeySpec(publicKey.encoded)
        FileOutputStream("$directory/public.key").use { publicOut ->
            publicOut.write(Base64.getEncoder().encode(publicSpec.encoded))
        }
        val privateKey = keyPair.private
        val privateSpec = PKCS8EncodedKeySpec(privateKey.encoded)
        FileOutputStream("$directory/private.key").use { privateOut ->
            privateOut.write(Base64.getEncoder().encode(privateSpec.encoded))
        }
    }

    /**
     * Base64ファイルを読み込みます
     * @param directory ディレクトリ
     * @param name ファイル名
     * @return [ByteArray]
     * @exception [IOException]
     */
    @Throws(IOException::class)
    private fun readBase64File(
        directory: File,
        name: String
    ): ByteArray {
        val f = File(directory, name)
        val contents = Files.readAllBytes(f.toPath())
        var strContents = String(contents)
        strContents = strContents.trim { it <= ' ' }
        return try {
            Base64.getDecoder().decode(strContents)
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException(
                "Base64 decoding exception: This is probably due to a corrupted file, " + "but in case it isn't, here is a b64 representation of what we read: " + String(
                    Base64.getEncoder().encode(contents), StandardCharsets.UTF_8
                ),
                e
            )
        }
    }

    /**
     * 鍵を読み込みます
     * @param directory ディレクトリ
     * @return [KeyPair]
     * @exception Exception
     */
    @Throws(Exception::class)
    fun load(directory: File): KeyPair {
        val keyFactory = KeyFactory.getInstance("RSA")
        val encodedPublicKey = readBase64File(directory, "public.key")
        val publicKeySpec = X509EncodedKeySpec(encodedPublicKey)
        val publicKey = keyFactory.generatePublic(publicKeySpec)
        val encodedPrivateKey = readBase64File(directory, "private.key")
        val privateKeySpec = PKCS8EncodedKeySpec(encodedPrivateKey)
        val privateKey = keyFactory.generatePrivate(privateKeySpec)
        return KeyPair(publicKey, privateKey)
    }
}
