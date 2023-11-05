package com.devYoussef.nb3elkhieradmin.utils

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.content.res.AssetFileDescriptor
import android.net.Uri
import android.os.Environment
import android.os.StatFs
import android.util.Log
import android.webkit.MimeTypeMap
import com.devYoussef.nb3elkhieradmin.ui.home.product.ProductsViewModel
import java.io.*
import java.util.*


internal fun getFilePathFromUri(
    context: Context,
    uri: Uri,
    productsViewModel: ProductsViewModel
): String? =
    if (uri.path?.contains("file://") == true) {
        uri.path
    } else {
        getFileFromContentUri(context, uri, productsViewModel).path

    }

@SuppressLint("SuspiciousIndentation")
private fun getFileFromContentUri(
    context: Context,
    contentUri: Uri,
    productsViewModel: ProductsViewModel
): File {

    val fileExtension = getFileExtension(context, contentUri) ?: ""
    val fileName = "temp_file.$fileExtension"
    val tempFile = File(context.cacheDir, fileName)
    tempFile.createNewFile()
    var oStream: FileOutputStream? = null
    var inputStream: InputStream? = null

    try {
        oStream = FileOutputStream(tempFile)
        inputStream = context.contentResolver.openInputStream(contentUri)

        inputStream?.let { copy(inputStream, oStream) }

        oStream.flush()
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        // Close streams
        inputStream?.close()
        oStream?.close()
    }
    productsViewModel.setFileName(fileName)
    Log.e("getFileFromContentUri: ", tempFile.toString())
    return tempFile
}

private fun getFileExtension(context: Context, uri: Uri): String? =
    if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
        MimeTypeMap.getSingleton().getExtensionFromMimeType(context.contentResolver.getType(uri))
    } else {
        uri.path?.let { MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(File(it)).toString()) }
    }

@Throws(IOException::class)
private fun copy(source: InputStream, target: OutputStream) {
    val buffer = ByteArray(8192)
    var length: Int
    while (source.read(buffer).also { length = it } > 0) {
        target.write(buffer, 0, length)
    }

//  length = source.read(buffer)
//    while (length != -1) {
//        target.write(buffer, 0, length)
//        length = source.read(buffer)
//    }

}

fun getAvailableInternalMemorySize(): Long {
    val path = Environment.getDataDirectory()
    val stat = StatFs(path.path)
    val blockSize: Long = stat.blockSizeLong
    val availableBlocks: Long = stat.availableBlocksLong
    return availableBlocks * blockSize
}

@SuppressLint("Recycle")
fun getFileSize(context: Context, uri: Uri): Long {
    val fileDescriptor: AssetFileDescriptor =
        context.contentResolver.openAssetFileDescriptor(uri, "r")!!
    return fileDescriptor.length
}