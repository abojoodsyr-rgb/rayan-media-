package com.rayan.rayantube.data

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.core.content.ContextCompat

/**
 * Notifies the UI when a Rayantube download (started via DownloadHelper) finishes,
 * so the Downloads screen can refresh its list automatically.
 */
class DownloadReceiver(private val onDownloadComplete: (Long) -> Unit) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == DownloadManager.ACTION_DOWNLOAD_COMPLETE) {
            val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1L)
            if (id != -1L) onDownloadComplete(id)
        }
    }

    companion object {
        fun register(context: Context, onDownloadComplete: (Long) -> Unit): DownloadReceiver {
            val receiver = DownloadReceiver(onDownloadComplete)
            val filter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
            ContextCompat.registerReceiver(
                context, receiver, filter, ContextCompat.RECEIVER_NOT_EXPORTED
            )
            return receiver
        }

        fun unregister(context: Context, receiver: DownloadReceiver) {
            runCatching { context.unregisterReceiver(receiver) }
        }
    }
}
