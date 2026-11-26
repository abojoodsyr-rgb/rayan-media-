package com.rayan.rayantube.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.rayan.rayantube.R
import com.rayan.rayantube.data.DownloadReceiver
import com.rayan.rayantube.data.ThumbnailImageLoader
import com.rayan.rayantube.data.VideoItem
import com.rayan.rayantube.data.VideoRepository

@Composable
fun DownloadsScreen(onOpenVideo: (VideoItem) -> Unit) {
    val context = LocalContext.current
    var videos by remember { mutableStateOf(VideoRepository.listDownloadedVideos(context)) }

    DisposableEffect(Unit) {
        val receiver = DownloadReceiver.register(context) {
            videos = VideoRepository.listDownloadedVideos(context)
        }
        onDispose { DownloadReceiver.unregister(context, receiver) }
    }

    if (videos.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = stringResource(R.string.empty_downloads),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(videos, key = { it.id }) { video ->
            VideoRow(video = video, onClick = { onOpenVideo(video) })
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
private fun VideoRow(video: VideoItem, onClick: () -> Unit) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(12.dp))
            ) {
                AsyncImage(
                    model = video.uri,
                    imageLoader = ThumbnailImageLoader.get(context),
                    contentDescription = video.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Icon(
                    imageVector = Icons.Filled.PlayCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.surface,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = video.title,
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1
                )
                Text(
                    text = "${video.readableDuration} · ${video.readableSize}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
