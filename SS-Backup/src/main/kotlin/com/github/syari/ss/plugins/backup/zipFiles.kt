package com.github.syari.ss.plugins.backup

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

fun zipFiles(files: Array<File>, output: File) {
    ZipOutputStream(FileOutputStream(output)).use {
        it.addFiles(files)
    }
}

private fun ZipOutputStream.addFiles(files: Array<File>, parent: String = "") {
    files.forEach { file ->
        val path = parent + "/" + file.name
        if (file.isFile) {
            FileInputStream(file).use { inputStream ->
                putNextEntry(ZipEntry(path))
                val buffer = ByteArray(1024)
                var len: Int
                while (inputStream.read(buffer).also { len = it } > 0) {
                    write(buffer, 0, len)
                }
            }
        } else {
            file.listFiles()?.let {
                addFiles(it, path)
            }
        }
    }
}