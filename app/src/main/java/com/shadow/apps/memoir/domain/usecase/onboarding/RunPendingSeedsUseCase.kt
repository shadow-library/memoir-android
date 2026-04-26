package com.shadow.apps.memoir.domain.usecase.onboarding

import com.shadow.apps.memoir.domain.repository.AuthRepository
import com.shadow.apps.memoir.domain.repository.ProfileRepository
import com.shadow.apps.memoir.domain.seed.SeedContext
import com.shadow.apps.memoir.domain.seed.SeedRegistry
import javax.inject.Inject

class RunPendingSeedsUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository,
    private val seedRegistry: SeedRegistry,
) {
    suspend operator fun invoke(context: SeedContext = SeedContext()): Result<Unit> = runCatching {
        val uid = authRepository.currentUserId() ?: error("Not signed in")
        val applied = profileRepository.getAppliedSeeds(uid)
        seedRegistry.all
            .filterNot { it.id in applied }
            .filter { context.hasContext || !it.requiresContext }
            .forEach { seed ->
                seed.apply(context)
                profileRepository.markSeedApplied(uid, seed.id)
            }
    }
}
