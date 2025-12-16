package com.omondit.learnhub.domain.usecase.bookmark

import com.omondit.learnhub.domain.model.Bookmark
import com.omondit.learnhub.domain.repository.BookmarkRepository
import com.omondit.learnhub.domain.usecase.BaseUseCase
import javax.inject.Inject

class GetBookmarksUseCase @Inject constructor(
    private val bookmarkRepository: BookmarkRepository
) : BaseUseCase<String, List<Bookmark>>() {

    override suspend fun invoke(params: String): Result<List<Bookmark>> {
        return bookmarkRepository.getBookmarks(params)
    }
}