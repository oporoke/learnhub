package com.omondit.learnhub.presentation.components

import android.content.Context
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.barteksc.pdfviewer.PDFView
import com.github.barteksc.pdfviewer.listener.OnErrorListener
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.URL

@Composable
fun PdfViewerComposable(
    pdfUrl: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    var isLoading by remember { mutableStateOf(true) }
    var hasError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var currentPage by remember { mutableIntStateOf(0) }
    var totalPages by remember { mutableIntStateOf(0) }
    var pdfFile by remember { mutableStateOf<File?>(null) }

    LaunchedEffect(pdfUrl) {
        isLoading = true
        hasError = false

        try {
            val file = downloadPdf(context, pdfUrl)
            pdfFile = file
            isLoading = false
        } catch (e: Exception) {
            hasError = true
            errorMessage = e.message ?: "Failed to load PDF"
            isLoading = false
        }
    }

    // Cleanup PDF file when component is disposed
    DisposableEffect(pdfFile) {
        onDispose {
            pdfFile?.let { file ->
                try {
                    if (file.exists()) {
                        file.delete()
                    }
                } catch (e: Exception) {
                    // Log error but don't crash
                }
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(500.dp)
    ) {
        when {
            isLoading -> {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Loading PDF...",
                        fontSize = 14.sp
                    )
                }
            }

            hasError -> {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "📄",
                        fontSize = 48.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Failed to load PDF",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = errorMessage,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            pdfFile != null -> {
                Column {
                    // PDF Viewer
                    AndroidView(
                        factory = { ctx ->
                            PDFView(ctx, null).apply {
                                layoutParams = FrameLayout.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT
                                )
                            }
                        },
                        update = { pdfView ->
                            pdfFile?.let { file ->
                                pdfView.fromFile(file)
                                    .enableSwipe(true)
                                    .swipeHorizontal(false)
                                    .enableDoubletap(true)
                                    .defaultPage(currentPage)
                                    .onLoad(OnLoadCompleteListener { nbPages ->
                                        totalPages = nbPages
                                    })
                                    .onPageChange(OnPageChangeListener { page, pageCount ->
                                        currentPage = page
                                    })
                                    .onError(OnErrorListener { throwable ->
                                        hasError = true
                                        errorMessage = throwable.message ?: "PDF rendering error"
                                    })
                                    .enableAnnotationRendering(true)
                                    .spacing(10)
                                    .load()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    )

                    // Page indicator
                    if (totalPages > 0) {
                        Surface(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Page ${currentPage + 1} of $totalPages",
                                modifier = Modifier.padding(8.dp),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}

private suspend fun downloadPdf(context: Context, urlString: String): File =
    withContext(Dispatchers.IO) {
        val fileName = "temp_${System.currentTimeMillis()}.pdf"
        val file = File(context.cacheDir, fileName)

        val url = URL(urlString)
        url.openStream().use { input ->
            FileOutputStream(file).use { output ->
                input.copyTo(output)
            }
        }

        file
    }