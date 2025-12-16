package com.omondit.learnhub.presentation.components

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

@OptIn(UnstableApi::class)
@Composable
fun VideoPlayerComposable(
    videoUrl: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    var isLoading by remember { mutableStateOf(true) }
    var hasError by remember { mutableStateOf(false) }

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            val mediaItem = MediaItem.fromUri(videoUrl)
            setMediaItem(mediaItem)
            prepare()
            playWhenReady = false // Don't auto-play

            addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    when (playbackState) {
                        Player.STATE_READY -> {
                            isLoading = false
                        }
                        Player.STATE_BUFFERING -> {
                            isLoading = true
                        }
                        Player.STATE_ENDED -> {
                            isLoading = false
                        }
                        Player.STATE_IDLE -> {
                            isLoading = false
                        }
                    }
                }

                override fun onPlayerError(error: androidx.media3.common.PlaybackException) {
                    hasError = true
                    isLoading = false
                }
            })
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(250.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        if (hasError) {
            // Error state
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "🎥",
                    style = MaterialTheme.typography.displayMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Failed to load video",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        } else {
            // Video player
            AndroidView(
                factory = { ctx ->
                    PlayerView(ctx).apply {
                        player = exoPlayer
                        layoutParams = FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        useController = true
                        controllerAutoShow = false
                    }
                },
                modifier = Modifier.fillMaxSize()
            )

            // Loading indicator
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(48.dp)
                )
            }
        }
    }
}
