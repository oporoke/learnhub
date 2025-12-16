package com.omondit.learnhub.domain.usecase.social

import com.omondit.learnhub.domain.model.Achievement
import com.omondit.learnhub.domain.repository.SocialRepository
import com.omondit.learnhub.domain.usecase.BaseUseCase
import javax.inject.Inject

class GetAchievementsUseCase @Inject constructor(
    private val socialRepository: SocialRepository
) : BaseUseCase<String, List<Achievement>>() {

    override suspend fun invoke(params: String): Result<List<Achievement>> {
        return socialRepository.getAchievements(params)
    }
}
