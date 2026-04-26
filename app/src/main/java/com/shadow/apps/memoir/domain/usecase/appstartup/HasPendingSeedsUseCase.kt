package com.shadow.apps.memoir.domain.usecase.appstartup

import com.shadow.apps.memoir.domain.repository.AuthRepository
import com.shadow.apps.memoir.domain.repository.ProfileRepository
import com.shadow.apps.memoir.domain.seed.SeedRegistry
import javax.inject.Inject

class HasPendingSeedsUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository,
    private val seedRegistry: SeedRegistry,
) {
    suspend operator fun invoke(): Boolean {
        val uid = authRepository.currentUserId() ?: return false
        val applied = runCatching { profileRepository.getAppliedSeeds(uid) }.getOrElse { return false }
        return seedRegistry.all.any { it.id !in applied && !it.requiresContext }
    }
}
