package com.example.animepedia.presentation.animeDetail

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import com.example.animepedia.domain.model.AnimeDetailModel

@OptIn(ExperimentalMaterial3Api::class)
class AnimeDetailUI {

    @Composable
    fun LayoutUI(
        animeTitle: String,
        anime: AnimeDetailModel?,
        loading: Boolean,
        isOnline: Boolean,
        onBack: () -> Unit
    ) {
        BackHandler { onBack() }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = animeTitle,
                            maxLines = 1,
                            style = MaterialTheme.typography.titleLarge
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->

            when {
                loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                anime != null -> {
                    Content(
                        anime = anime,
                        paddingValues = paddingValues,
                        isOnline = isOnline,
                    )
                }
            }
        }
    }

    // ---------------- CONTENT ----------------

    @Composable
    private fun Content(
        anime: AnimeDetailModel,
        paddingValues: PaddingValues,
        isOnline: Boolean,
    ) {
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            MediaSection(
                imageUrl = anime.imageUrl,
                trailerUrl = anime.trailerUrl,
                isOnline = isOnline,
            )

            Spacer(modifier = Modifier.height(16.dp))

            ContentSection(anime)
        }
    }

    // ---------------- MEDIA ----------------

    @Composable
    private fun MediaSection(
        imageUrl: String,
        trailerUrl: String?,
        isOnline: Boolean,
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            if (!trailerUrl.isNullOrBlank() && isOnline) {
                AnimeTrailerWebView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp),
                    embedUrl = trailerUrl
                )
            } else if(imageUrl.isNotEmpty()) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Anime Poster",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .clip(RoundedCornerShape(16.dp))
                )
            }
        }
    }

    // ---------------- SAFE VIDEO PLAYER ----------------

    @Composable
    private fun AnimeTrailerWebView(
        modifier: Modifier,
        embedUrl: String
    ) {
        AndroidView(
            modifier = modifier,
            factory = { context ->
                WebView(context).apply {

                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )

                    setupSettings()

                    loadHtml(embedUrl)
                }
            }
        )
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun WebView.setupSettings() {
        settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            mediaPlaybackRequiresUserGesture = false
            cacheMode = WebSettings.LOAD_DEFAULT
            useWideViewPort = true
            loadWithOverviewMode = true
            allowFileAccess = false
            allowContentAccess = false
        }

        webChromeClient = WebChromeClient()
        setBackgroundColor(0x00000000)
        setLayerType(WebView.LAYER_TYPE_HARDWARE, null)
    }

    private fun WebView.loadHtml(originalUrl: String) {
        val safeUrl = buildSafeYoutubeUrl(originalUrl)

        val html = """
            <!DOCTYPE html>
            <html>
            <head>
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <style>
                    html, body {
                        margin: 0;
                        padding: 0;
                        width: 100%;
                        height: 100%;
                        background-color: black;
                        overflow: hidden;
                    }
                    iframe {
                        position: absolute;
                        top: 0;
                        left: 0;
                        width: 100%;
                        height: 100%;
                        border: none;
                    }
                </style>
            </head>
            <body>
                <iframe
                    src="$safeUrl"
                    allow="autoplay; encrypted-media; picture-in-picture"
                    allowfullscreen>
                </iframe>
            </body>
            </html>
        """.trimIndent()

        loadDataWithBaseURL(
            "https://www.youtube-nocookie.com",
            html,
            "text/html",
            "utf-8",
            null
        )
    }

    private fun buildSafeYoutubeUrl(url: String): String {
        val base = url.substringBefore("?")
        return "$base?autoplay=1&mute=1&playsinline=1&controls=1"
    }

    // ---------------- DETAILS ----------------

    @Composable
    private fun ContentSection(anime: AnimeDetailModel) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {

            Text(
                text = anime.titleEnglish ?: anime.title,
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                InfoChip("⭐ ${anime.rating ?: "N/A"}")
                InfoChip("Episodes: ${anime.episodes ?: "N/A"}")
            }

            Spacer(modifier = Modifier.height(16.dp))

            SectionTitle("Synopsis")
            Text(
                text = anime.synopsis ?: "No synopsis available.",
                style = MaterialTheme.typography.bodyMedium
            )

            if (anime.genres.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                SectionTitle("Genres")
                FlowRow(anime.genres)
            }

            if (anime.studios.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                SectionTitle("Studios")
                FlowRow(anime.studios)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // ---------------- SMALL UI ----------------

    @Composable
    private fun InfoChip(text: String) {
        Surface(
            shape = RoundedCornerShape(50),
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
            Text(
                text = text,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                style = MaterialTheme.typography.labelMedium
            )
        }
    }

    @Composable
    private fun SectionTitle(title: String) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
    }

    @Composable
    private fun FlowRow(items: List<String>) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items.forEach { InfoChip(it) }
        }
    }
}
