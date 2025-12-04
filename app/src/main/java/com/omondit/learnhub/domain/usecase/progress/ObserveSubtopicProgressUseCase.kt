package com.omondit.learnhub.domain.usecase.progress

import com.omondit.learnhub.domain.model.SubtopicProgress
import com.omondit.learnhub.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveSubtopicProgressUseCase @Inject constructor(
    private val progressRepository: ProgressRepository
) {
    operator fun invoke(userId: String, subtopicIds: List<String>): Flow<List<SubtopicProgress>> {
        return progressRepository.observeSubtopicProgress(userId, subtopicIds)
    }
}
