package io.kelly.util

import android.util.Base64
import java.nio.charset.StandardCharsets
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.spec.PKCS8EncodedKeySpec
import javax.crypto.Cipher

/**
 * 扩展函数：直接解密
 */
fun String.decryptRSA(privateKeyPem: String): String? {
    return try {
        val key = RSADecoder.parsePrivateKey(privateKeyPem)
        RSADecoder.decrypt(this, key)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

internal object RSADecoder {
    private const val RSA_ALGORITHM = "RSA"
    private const val TRANSFORMATION = "RSA/ECB/PKCS1Padding"


    fun decrypt(encryptedBase64: String?, privateKey: PrivateKey): String {
        if (encryptedBase64.isNullOrEmpty()) throw IllegalArgumentException("Data is empty")

        val encryptedBytes = Base64.decode(encryptedBase64, Base64.DEFAULT)
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.DECRYPT_MODE, privateKey)

        // 注意：这里没有处理分段解密。如果数据过长，会抛出 IllegalBlockSizeException
        val decryptedBytes = cipher.doFinal(encryptedBytes)

        return String(decryptedBytes, StandardCharsets.UTF_8)
    }


    fun parsePrivateKey(privateKeyPem: String): PrivateKey {
        val cleanPem = privateKeyPem
            .replace("-----BEGIN PRIVATE KEY-----", "")
            .replace("-----END PRIVATE KEY-----", "")
            .replace("\\s".toRegex(), "")

        val keyBytes = Base64.decode(cleanPem, Base64.DEFAULT)
        val keySpec = PKCS8EncodedKeySpec(keyBytes)
        val keyFactory = KeyFactory.getInstance(RSA_ALGORITHM)
        return keyFactory.generatePrivate(keySpec)
    }
}