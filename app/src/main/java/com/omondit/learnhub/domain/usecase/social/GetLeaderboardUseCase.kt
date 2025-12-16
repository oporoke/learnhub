package com.omondit.learnhub.domain.usecase.social


import com.omondit.learnhub.domain.model.LeaderboardEntry
import com.omondit.learnhub.domain.model.LeaderboardType
import com.omondit.learnhub.domain.repository.SocialRepository
import com.omondit.learnhub.domain.usecase.BaseUseCase
import javax.inject.Inject

class GetLeaderboardUseCase @Inject constructor(
    private val socialRepository: SocialRepository
) : BaseUseCase<GetLeaderboardUseCase.Params, List<LeaderboardEntry>>() {

    data class Params(
        val type: LeaderboardType,
        val limit: Int = 10
    )

    override suspend fun invoke(params: Params): Result<List<LeaderboardEntry>> {
        return socialRepository.getLeaderboard(params.type, params.limit)
    }
}
