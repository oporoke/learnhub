package com.omondit.learnhub.domain.usecase.bookmark

import com.omondit.learnhub.domain.model.BookmarkType
import com.omondit.learnhub.domain.repository.BookmarkRepository
import com.omondit.learnhub.domain.usecase.BaseUseCase
import javax.inject.Inject

class ToggleBookmarkUseCase @Inject constructor(
    private val bookmarkRepository: BookmarkRepository
) : BaseUseCase<ToggleBookmarkUseCase.Params, Boolean>() {

    data class Params(
        val userId: String,
        val itemType: BookmarkType,
        val itemId: String,
        val itemTitle: String,
        val itemDescription: String
    )

    override suspend fun invoke(params: Params): Result<Boolean> {
        return bookmarkRepository.toggleBookmark(
            userId = params.userId,
            itemType = params.itemType,
            itemId = params.itemId,
            itemTitle = params.itemTitle,
            itemDescription = params.itemDescription
        )
    }
}
