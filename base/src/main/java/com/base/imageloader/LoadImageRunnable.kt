package com.base.imageloader

import android.content.Context
import android.graphics.Bitmap
import com.base.utils.BitmapUtil
import java.io.File
import java.net.HttpURLConnection
import java.net.URL

/**
 * Created by vophamtuananh on 1/12/18.
 */
class LoadImageRunnable(context: Context, loadInformationKeeper: LoadInformationKeeper,
                        loadCallback: (loadInformationKeeper: LoadInformationKeeper, bitmap: Bitmap?) -> Unit) : Runnable {

    companion object {
        private val mSyncObject = Any()

        private var mFileCacher: FileCacher? = null
    }

    private val mLoadCallback = loadCallback

    private val mLoadInformationKeeper = loadInformationKeeper

    init {
        if (mFileCacher == null) {
            synchronized(mSyncObject) {
                if (mFileCacher == null) {
                    mFileCacher = FileCacher(context)
                }
            }
        }
    }

    override fun run() {
        val bitmap = getBitmap(mLoadInformationKeeper)

        mLoadCallback(mLoadInformationKeeper, bitmap)
    }

    private fun getBitmap(loadInformationKeeper: LoadInformationKeeper): Bitmap? {
        val file = mFileCacher!!.getFile(loadInformationKeeper.url!!) ?: return null

        var bitmap: Bitmap? = null
        if (file.exists())
            bitmap = decodeFile(file, loadInformationKeeper)

        return if (bitmap != null) bitmap else fetchBitmap(file, loadInformationKeeper)

    }

    private fun fetchBitmap(file: File, loadInformationKeeper: LoadInformationKeeper): Bitmap? {
        try {
            val imageUrl = URL(loadInformationKeeper.url?.replace(" ", "%20"))

            val conn = imageUrl.openConnection() as HttpURLConnection
            conn.connectTimeout = 30000
            conn.readTimeout = 30000
            conn.instanceFollowRedirects = true

            val size = conn.contentLength.toLong()
            val inputStream = conn.inputStream
            mFileCacher!!.saveFile(inputStream, file, size)
            conn.disconnect()
            if (file.exists())
                return decodeFile(file, loadInformationKeeper)
        } catch (ex: Exception) {
            ex.printStackTrace()
            if (file.exists())
                file.delete()
        }

        return null
    }

    private fun decodeFile(file: File, loadInformationKeeper: LoadInformationKeeper): Bitmap {
        val bitmap = BitmapUtil.getBitmapFromCachedFile(file, loadInformationKeeper.config)
        return when (loadInformationKeeper.scaleType) {
            LoadInformationKeeper.ScaleType.CROP -> crop(bitmap, loadInformationKeeper.width, loadInformationKeeper.height)
            LoadInformationKeeper.ScaleType.CENTER_INSIDE -> centerInside(bitmap, loadInformationKeeper.width, loadInformationKeeper.height)
            LoadInformationKeeper.ScaleType.SCALE_FULL_WIDTH -> scaleFullWidth(bitmap, loadInformationKeeper.width)
            LoadInformationKeeper.ScaleType.SCALE_FULL_HEIGHT -> scaleFullHeight(bitmap, loadInformationKeeper.height)
            else -> bitmap
        }

    }

    private fun crop(source: Bitmap, showWidth: Int, showHeight: Int): Bitmap {
        var bitmap = source
        val bitmapWidth = bitmap.width
        val bitmapHeight = bitmap.height

        val scaleWidth = showWidth / bitmapWidth.toFloat()
        val scaleHeight = showHeight / bitmapHeight.toFloat()

        val scale = if (scaleWidth > scaleHeight) scaleWidth else scaleHeight

        val destWidth = (bitmapWidth * scale).toInt()
        val destHeight = (bitmapHeight * scale).toInt()
        bitmap = Bitmap.createScaledBitmap(bitmap, destWidth, destHeight, false)
        if (scaleWidth < scaleHeight) {
            bitmap = Bitmap.createBitmap(bitmap, bitmap.width / 2 - showWidth / 2, 0, showWidth, destHeight)
        } else if (scaleWidth >= scaleHeight) {
            if (scaleWidth > scaleHeight)
                bitmap = Bitmap.createBitmap(bitmap, 0, bitmap.height / 2 - showHeight / 2, destWidth, showHeight)
            else if (scaleWidth == scaleHeight)
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, destWidth, destHeight)
        }
        return bitmap
    }

    private fun centerInside(source: Bitmap, showWidth: Int, showHeight: Int): Bitmap {
        var bitmap = source
        val bitmapWidth = bitmap.width
        val bitmapHeight = bitmap.height

        val scaleWidth = showWidth / bitmapWidth.toFloat()
        val scaleHeight = showHeight / bitmapHeight.toFloat()

        val scale = if (scaleWidth < scaleHeight) scaleWidth else scaleHeight
        val destWidth = (bitmapWidth * scale).toInt()
        val destHeight = (bitmapHeight * scale).toInt()
        bitmap = Bitmap.createScaledBitmap(bitmap, destWidth, destHeight, false)
        return bitmap
    }

    private fun scaleFullWidth(source: Bitmap, showWidth: Int): Bitmap {
        var bitmap = source
        val bitmapWidth = bitmap.width
        val bitmapHeight = bitmap.height

        val scale = showWidth / bitmapWidth.toFloat()

        if (scale == 1f) {
            return bitmap
        } else {
            val destWidth = (bitmapWidth * scale).toInt()
            val destHeight = (bitmapHeight * scale).toInt()
            bitmap = Bitmap.createScaledBitmap(bitmap, destWidth, destHeight, false)
        }

        return bitmap
    }

    private fun scaleFullHeight(source: Bitmap, showHeight: Int): Bitmap {
        var bitmap = source
        val bitmapWidth = bitmap.width
        val bitmapHeight = bitmap.height

        val scale = showHeight / bitmapHeight.toFloat()

        if (scale == 1f) {
            return bitmap
        } else {
            val destWidth = (bitmapWidth * scale).toInt()
            val destHeight = (bitmapHeight * scale).toInt()
            bitmap = Bitmap.createScaledBitmap(bitmap, destWidth, destHeight, false)
        }

        return bitmap
    }
}