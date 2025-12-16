package com.omondit.learnhub.domain.usecase.search

import com.omondit.learnhub.domain.repository.SearchRepository
import com.omondit.learnhub.domain.repository.SearchResult
import com.omondit.learnhub.domain.usecase.BaseUseCase
import javax.inject.Inject

class SearchUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) : BaseUseCase<SearchUseCase.Params, List<SearchResult>>() {

    data class Params(
        val query: String,
        val classId: String? = null
    )

    override suspend fun invoke(params: Params): Result<List<SearchResult>> {
        return searchRepository.search(params.query, params.classId)
    }
}
