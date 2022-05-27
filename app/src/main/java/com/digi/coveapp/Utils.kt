package com.digi.coveapp

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.IOException


class Utils {
    fun encodeImgToString(c: Context, photoUri: Uri?): String? {
        var bitmap: Bitmap? = null
        try {
            val baos = ByteArrayOutputStream()
            bitmap = MediaStore.Images.Media.getBitmap(c.contentResolver, photoUri)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos)
            val imageBytes: ByteArray = baos.toByteArray()
            return Base64.encodeToString(imageBytes, Base64.DEFAULT)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    //decode base64 string to image
    fun decodeImgString(imageString: String?): Bitmap? {
        val imageBytes: ByteArray = Base64.decode(imageString, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }

    fun stringToBitMap(encodedString: String?): Bitmap? {
        return try {
            val encodeByte: ByteArray = Base64.decode(encodedString, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
        } catch (e: Exception) {
            e.message
            null
        }
    }
}