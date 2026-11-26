package com.rayan.rayantube.data

import android.content.Context
import coil.ImageLoader
import coil.decode.VideoFrameDecoder

/**
 * A Coil ImageLoader that knows how to pull a preview frame out of a local
 * video file/Uri, used to show thumbnails on the Downloads screen.
 */
object ThumbnailImageLoader {
    @Volatile private var instance: ImageLoader? = null

    fun get(context: Context): ImageLoader {
        return instance ?: synchronized(this) {
            instance ?: ImageLoader.Builder(context.applicationContext)
                .components { add(VideoFrameDecoder.Factory()) }
                .build()
                .also { instance = it }
        }
    }
}
