package com.fromu.fromu.utils

import android.content.Context
import android.graphics.Bitmap
import java.io.File
import java.io.FileOutputStream
import java.util.*

class ImageUtils {
    companion object {


        /**
         * 비트맵을 file로 만들어주는 메소드
         *
         * @param context
         * @param bitmap
         */
        fun bitmapToFile(context: Context, bitmap: Bitmap?): File {
            val uuid = UUID.randomUUID().toString().replace("-", "")
            val fileName = "$uuid.jpg"
            val file = File(context.cacheDir, fileName)
            file.createNewFile()
            val out = FileOutputStream(file)
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.close()

            return file
        }
    }
}