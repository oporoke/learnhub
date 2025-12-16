package com.omondit.learnhub.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent

@Composable
fun AsyncImageLoader(
    imageUrl: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    SubcomposeAsyncImage(
        model = imageUrl,
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = contentScale
    ) {
        when (painter.state) {
            is AsyncImagePainter.State.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
            is AsyncImagePainter.State.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "🖼️",
                            style = MaterialTheme.typography.displayMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Failed to load image",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
            else -> {
                SubcomposeAsyncImageContent()
            }
        }
    }
}
