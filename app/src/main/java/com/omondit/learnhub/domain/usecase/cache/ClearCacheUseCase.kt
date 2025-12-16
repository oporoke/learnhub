package com.omondit.learnhub.domain.usecase.cache

import com.omondit.learnhub.data.local.dao.ContentDao
import javax.inject.Inject

class ClearCacheUseCase @Inject constructor(
    private val contentDao: ContentDao
) {
    suspend operator fun invoke() {
        contentDao.clearAllClasses()
        contentDao.clearAllSubjects()
        contentDao.clearAllTopics()
        contentDao.clearAllSubtopics()
        contentDao.clearAllContent()
    }
}
