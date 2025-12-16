package com.omondit.learnhub.domain.usecase.bookmark

import com.omondit.learnhub.domain.model.BookmarkType
import com.omondit.learnhub.domain.repository.BookmarkRepository
import javax.inject.Inject

class GetBookmarkedIdsUseCase @Inject constructor(
    private val bookmarkRepository: BookmarkRepository
) {
    suspend operator fun invoke(userId: String, itemType: BookmarkType, itemIds: List<String>): Set<String> {
        return bookmarkRepository.getBookmarkedIds(userId, itemType, itemIds)
    }
}
