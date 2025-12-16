package com.omondit.learnhub.domain.usecase.bookmark

import com.omondit.learnhub.domain.model.BookmarkType
import com.omondit.learnhub.domain.repository.BookmarkRepository
import javax.inject.Inject

class IsBookmarkedUseCase @Inject constructor(
    private val bookmarkRepository: BookmarkRepository
) {
    suspend operator fun invoke(userId: String, itemType: BookmarkType, itemId: String): Boolean {
        return bookmarkRepository.isBookmarked(userId, itemType, itemId)
    }
}
