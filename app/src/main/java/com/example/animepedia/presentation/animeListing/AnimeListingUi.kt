package com.example.animepedia.presentation.animeListing

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.example.animepedia.domain.model.AnimeModel
import kotlinx.coroutines.flow.collectLatest
import com.example.animepedia.R
import androidx.compose.ui.res.painterResource

@OptIn(ExperimentalMaterial3Api::class)
class AnimeListingUI {

    @Composable
    fun LayoutUI(
        animeList: List<AnimeModel>,
        totalCount: Int,
        loading: Boolean,
        onLoadNextPage: () -> Unit,
        onSearch: (String) -> Unit,
        onAnimeClick: (Int, String) -> Unit
    ) {
        val listState = rememberLazyListState()
        var searchQuery by remember { mutableStateOf("") }

        val isSearching = searchQuery.isNotBlank()

        LaunchedEffect(listState, animeList, isSearching) {
            if (isSearching) return@LaunchedEffect

            snapshotFlow {
                listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
            }.collectLatest { lastVisibleIndex ->
                if (lastVisibleIndex == animeList.lastIndex - 5) {
                    onLoadNextPage()
                }
            }
        }

        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            topBar = { AnimeTopBar() }
        ) { paddingValues ->

            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {

                Divider(color = MaterialTheme.colorScheme.surfaceVariant)

                SearchBar(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    onSearch = {
                        searchQuery = it
                        onSearch(it)
                    }
                )

                AnimeList(
                    animeList = animeList,
                    listState = listState,
                    isSearching = isSearching,
                    loading = loading,
                    totalCount = totalCount,
                    onLoadNextPage = onLoadNextPage,
                    onAnimeClick = onAnimeClick
                )
            }
        }
    }

    @Composable
    private fun AnimeTopBar() {
        TopAppBar(
            title = {
                Text(
                    text = "Top Rated Animes",
                    style = MaterialTheme.typography.titleLarge
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        )
    }

    @Composable
    private fun SearchBar(
        modifier: Modifier = Modifier,
        onSearch: (String) -> Unit
    ) {
        var query by remember { mutableStateOf("") }

        OutlinedTextField(
            value = query,
            onValueChange = {
                query = it
                onSearch(it)
            },
            modifier = modifier.fillMaxWidth(),
            placeholder = { Text("Search anime...") },
            singleLine = true,
            shape = RoundedCornerShape(14.dp)
        )
    }

    @Composable
    private fun AnimeList(
        animeList: List<AnimeModel>,
        listState: LazyListState,
        isSearching: Boolean,
        loading: Boolean,
        totalCount: Int,
        onLoadNextPage: () -> Unit,
        onAnimeClick: (Int, String) -> Unit
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface),
            contentPadding = PaddingValues(bottom = 96.dp)
        ) {

            itemsIndexed(animeList) { _, anime ->
                AnimeItem(
                    anime = anime,
                    onClick = {
                        onAnimeClick(anime.id, anime.title)
                    }
                )
            }

            if (isSearching) {
                item {
                    LoadMoreButton(
                        visibleCount = animeList.size,
                        totalCount = totalCount,
                        onClick = onLoadNextPage
                    )
                }
            }

            if (loading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }

    @Composable
    private fun LoadMoreButton(
        visibleCount: Int,
        totalCount: Int,
        onClick: () -> Unit
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(50)
        ) {
            Text(
                text = "Load more · Showing $visibleCount of $totalCount"
            )
        }
    }

    @Composable
    fun AnimeItem(
        anime: AnimeModel,
        onClick: () -> Unit
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp)
                .clickable { onClick() },
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow
            )
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(110.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    SubcomposeAsyncImage(
                        model = anime.imageUrl.takeIf { it.isNotBlank() }
                            ?: R.drawable.ic_anime_placeholder,
                        contentDescription = anime.title,
                        modifier = Modifier.matchParentSize(),
                        contentScale = ContentScale.Crop
                    ) {
                        when (painter.state) {

                            is AsyncImagePainter.State.Loading -> {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(modifier = Modifier.size(26.dp))
                                }
                            }

                            is AsyncImagePainter.State.Error -> {
                                Image(
                                    painter = painterResource(R.drawable.ic_anime_placeholder),
                                    contentDescription = "Placeholder",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(20.dp),
                                    contentScale = ContentScale.Fit
                                )
                            }

                            else -> SubcomposeAsyncImageContent()
                        }
                    }
                }



                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = anime.title,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 2
                    )

                    Text(
                        text = "Episodes: ${anime.episodes ?: "N/A"}",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Text(
                        text = "⭐ ${anime.rating ?: "N/A"}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}
