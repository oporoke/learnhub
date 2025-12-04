package com.omondit.learnhub.domain.usecase.progress

import com.omondit.learnhub.domain.model.TopicProgress
import com.omondit.learnhub.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveTopicProgressUseCase @Inject constructor(
    private val progressRepository: ProgressRepository
) {
    operator fun invoke(userId: String, topicIds: List<String>): Flow<List<TopicProgress>> {
        return progressRepository.observeTopicProgress(userId, topicIds)
    }
}