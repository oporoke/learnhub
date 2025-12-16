package com.omondit.learnhub.presentation.screens.search


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.omondit.learnhub.domain.repository.SearchResult
import com.omondit.learnhub.domain.repository.SearchResultType
import com.omondit.learnhub.presentation.util.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onNavigateBack: () -> Unit,
    onNavigateToContent: (String) -> Unit, // subtopicId
    viewModel: SearchViewModel = hiltViewModel()
) {
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val searchResults by viewModel.searchResults.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    TextField(
                        value = searchQuery,
                        onValueChange = { viewModel.onSearchQueryChanged(it) },
                        placeholder = { Text("Search topics, content...") },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = "Search")
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { viewModel.clearSearch() }) {
                                    Icon(Icons.Default.Clear, contentDescription = "Clear")
                                }
                            }
                        },
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            //focusedIndicatorColor = MaterialTheme.colorScheme.transparent,
                            //unfocusedIndicatorColor = MaterialTheme.colorScheme.transparent
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = searchResults) {
                is UiState.Idle -> {
                    EmptySearchState(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                is UiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                is UiState.Success -> {
                    SearchResultsList(
                        results = state.data,
                        onResultClick = { result ->
                            when (result.type) {
                                SearchResultType.SUBTOPIC, SearchResultType.CONTENT -> {
                                    result.subtopicId?.let { onNavigateToContent(it) }
                                }
                                SearchResultType.TOPIC -> {
                                    // Could navigate to topics screen
                                }
                            }
                        }
                    )
                }

                is UiState.Error -> {
                    NoResultsState(
                        message = state.message,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
private fun SearchResultsList(
    results: List<SearchResult>,
    onResultClick: (SearchResult) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(
                text = "${results.size} results found",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        items(results) { result ->
            SearchResultCard(
                result = result,
                onClick = { onResultClick(result) }
            )
        }
    }
}

@Composable
private fun SearchResultCard(
    result: SearchResult,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Type icon
            Surface(
                color = when (result.type) {
                    SearchResultType.TOPIC -> MaterialTheme.colorScheme.primaryContainer
                    SearchResultType.SUBTOPIC -> MaterialTheme.colorScheme.secondaryContainer
                    SearchResultType.CONTENT -> MaterialTheme.colorScheme.tertiaryContainer
                },
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = when (result.type) {
                        SearchResultType.TOPIC -> "📚"
                        SearchResultType.SUBTOPIC -> "📖"
                        SearchResultType.CONTENT -> "📝"
                    },
                    fontSize = 24.sp,
                    modifier = Modifier.padding(8.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = result.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = result.description,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = when (result.type) {
                        SearchResultType.TOPIC -> "Topic"
                        SearchResultType.SUBTOPIC -> "Subtopic"
                        SearchResultType.CONTENT -> "Content"
                    },
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun EmptySearchState(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "🔍",
            fontSize = 64.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Search for Topics & Content",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Type in the search bar to find learning materials",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun NoResultsState(
    message: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "❌",
            fontSize = 64.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = message,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}
