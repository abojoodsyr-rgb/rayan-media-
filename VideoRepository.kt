package com.rayan.rayantube.data

import android.content.ContentUris
import android.content.Context
import android.os.Build
import android.provider.MediaStore

/**
 * Reads back whatever Rayantube has already saved locally (via DownloadHelper)
 * so the Downloads screen and in-app player can list and play them.
 * This only reads local files the app itself created - no network scraping involved.
 */
object VideoRepository {

    private const val APP_FOLDER = "Rayantube"

    fun listDownloadedVideos(context: Context): List<VideoItem> {
        val items = mutableListOf<VideoItem>()

        val collection = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.SIZE,
            MediaStore.Video.Media.DURATION,
            MediaStore.Video.Media.DATE_ADDED,
            MediaStore.Video.Media.RELATIVE_PATH
        )

        val selection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            "${MediaStore.Video.Media.RELATIVE_PATH} LIKE ?"
        } else null
        val selectionArgs = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            arrayOf("%$APP_FOLDER%")
        } else null

        val sortOrder = "${MediaStore.Video.Media.DATE_ADDED} DESC"

        context.contentResolver.query(
            collection, projection, selection, selectionArgs, sortOrder
        )?.use { cursor ->
            val idCol = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
            val nameCol = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
            val sizeCol = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)
            val durationCol = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)
            val dateCol = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_ADDED)
            val pathCol = cursor.getColumnIndex(MediaStore.Video.Media.RELATIVE_PATH)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idCol)

                // Pre-Android 10 fallback filter, since RELATIVE_PATH selection isn't available
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                    val path = if (pathCol >= 0) cursor.getString(pathCol) ?: "" else ""
                    if (!path.contains(APP_FOLDER)) continue
                }

                val uri = ContentUris.withAppendedId(collection, id)
                items.add(
                    VideoItem(
                        id = id,
                        title = cursor.getString(nameCol) ?: "video",
                        uri = uri,
                        sizeBytes = cursor.getLong(sizeCol),
                        durationMs = cursor.getLong(durationCol),
                        dateAddedSeconds = cursor.getLong(dateCol)
                    )
                )
            }
        }

        return items
    }
}
