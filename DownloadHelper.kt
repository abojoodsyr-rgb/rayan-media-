package com.rayan.rayantube.data

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import java.net.URLConnection

/**
 * IMPORTANT SCOPE NOTE
 * --------------------
 * This helper downloads a DIRECT file URL (a link that points straight at a
 * video/media file, e.g. one ending in .mp4/.webm/.mkv, or a link from your own
 * cloud storage / server that returns a video file).
 *
 * It deliberately does NOT parse or scrape pages from YouTube, Instagram,
 * TikTok, Facebook, etc. to extract their hosted video streams - those
 * platforms' terms of service prohibit that, regardless of who is asking or
 * why. Wire this app up to sources you actually have the rights to.
 */
object DownloadHelper {

    private const val SUBFOLDER = "Rayantube"

    sealed class DownloadResult {
        data class Success(val downloadId: Long) : DownloadResult()
        data class Error(val message: String) : DownloadResult()
    }

    fun enqueueDirectUrlDownload(context: Context, urlString: String): DownloadResult {
        val trimmed = urlString.trim()

        if (!trimmed.startsWith("http://") && !trimmed.startsWith("https://")) {
            return DownloadResult.Error("الرابط لازم يبدأ بـ http:// أو https://")
        }

        val uri = try {
            Uri.parse(trimmed)
        } catch (e: Exception) {
            return DownloadResult.Error("رابط غير صالح")
        }

        val fileName = guessFileName(trimmed)

        return try {
            val request = DownloadManager.Request(uri)
                .setTitle(fileName)
                .setDescription("Rayantube download")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_DOWNLOADS,
                    "$SUBFOLDER/$fileName"
                )
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

            val manager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val id = manager.enqueue(request)
            DownloadResult.Success(id)
        } catch (e: Exception) {
            DownloadResult.Error(e.message ?: "تعذّر بدء التنزيل")
        }
    }

    private fun guessFileName(url: String): String {
        val lastSegment = url.substringAfterLast('/').substringBefore('?')
        if (lastSegment.isNotBlank() && lastSegment.contains('.')) {
            return lastSegment
        }
        val guessedExt = URLConnection.guessContentTypeFromName(url)?.substringAfter('/') ?: "mp4"
        return "rayantube_${System.currentTimeMillis()}.$guessedExt"
    }
}
