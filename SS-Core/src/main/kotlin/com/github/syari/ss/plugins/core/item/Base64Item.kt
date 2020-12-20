package com.github.syari.ss.plugins.core.item

import com.github.syari.ss.plugins.core.nms.UseNMS
import net.minecraft.server.v1_16_R3.NBTCompressedStreamTools
import net.minecraft.server.v1_16_R3.NBTTagCompound
import net.minecraft.server.v1_16_R3.NBTTagList
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack
import org.bukkit.inventory.ItemStack
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.DataInput
import java.io.DataInputStream
import java.io.DataOutput
import java.io.DataOutputStream
import java.io.IOException
import java.math.BigInteger

@UseNMS
object Base64Item {
    /**
     * アイテムを Base64 に変換します
     * @param item 対象アイテム
     * @return [String]
     */
    fun toBase64(item: ItemStack): String? {
        val outputStream = ByteArrayOutputStream()
        val nbtTagCompound = NBTTagCompound().apply {
            CraftItemStack.asNMSCopy(item).save(this)
            NBTTagList().add(this)
        }

        try {
            NBTCompressedStreamTools.a(nbtTagCompound, DataOutputStream(outputStream) as DataOutput)
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }

        return BigInteger(1, outputStream.toByteArray()).toString(32)
    }

    /**
     * Base64 をアイテムに変換します
     * @param base64 Base64 データ
     * @return [ItemStack]?
     */
    fun fromBase64(base64: String): ItemStack? {
        val inputStream = ByteArrayInputStream(BigInteger(base64, 32).toByteArray())

        val nbtTagCompound = try {
            NBTCompressedStreamTools.a(DataInputStream(inputStream) as DataInput)
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }

        val nmsItem = net.minecraft.server.v1_16_R3.ItemStack.a(nbtTagCompound)
        return CraftItemStack.asBukkitCopy(nmsItem)
    }
}