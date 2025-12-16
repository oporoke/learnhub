package com.omondit.learnhub.presentation.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omondit.learnhub.domain.repository.SearchResult
import com.omondit.learnhub.domain.usecase.search.SearchUseCase
import com.omondit.learnhub.presentation.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchUseCase: SearchUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchResults = MutableStateFlow<UiState<List<SearchResult>>>(UiState.Idle)
    val searchResults: StateFlow<UiState<List<SearchResult>>> = _searchResults.asStateFlow()

    init {
        // Debounce search queries
        viewModelScope.launch {
            _searchQuery
                .debounce(300) // Wait 300ms after user stops typing
                .distinctUntilChanged()
                .collect { query ->
                    if (query.isBlank()) {
                        _searchResults.value = UiState.Idle
                    } else {
                        performSearch(query)
                    }
                }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    private fun performSearch(query: String) {
        viewModelScope.launch {
            _searchResults.value = UiState.Loading

            val result = searchUseCase(SearchUseCase.Params(query = query))

            result.fold(
                onSuccess = { results ->
                    _searchResults.value = if (results.isEmpty()) {
                        UiState.Error("No results found for \"$query\"")
                    } else {
                        UiState.Success(results)
                    }
                },
                onFailure = { exception ->
                    _searchResults.value = UiState.Error(
                        exception.message ?: "Search failed"
                    )
                }
            )
        }
    }

    fun clearSearch() {
        _searchQuery.value = ""
        _searchResults.value = UiState.Idle
    }
}
