package com.rayan.rayantube.data

import android.net.Uri

data class VideoItem(
    val id: Long,
    val title: String,
    val uri: Uri,
    val sizeBytes: Long,
    val durationMs: Long,
    val dateAddedSeconds: Long
) {
    val readableSize: String
        get() {
            val kb = sizeBytes / 1024.0
            val mb = kb / 1024.0
            return if (mb >= 1.0) "%.1f MB".format(mb) else "%.0f KB".format(kb)
        }

    val readableDuration: String
        get() {
            val totalSeconds = durationMs / 1000
            val minutes = totalSeconds / 60
            val seconds = totalSeconds % 60
            return "%02d:%02d".format(minutes, seconds)
        }
}
