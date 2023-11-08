package com.ralphdugue.arcadephito.profile.ui

import com.ralphdugue.arcadephito.auth.domain.AuthRepository
import com.ralphdugue.arcadephito.di.modules.IoDispatcher
import com.ralphdugue.phitoarch.mvi.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
): BaseViewModel<ProfileEvent, ProfileState, ProfileEffect>(ioDispatcher) {

    override fun createEffect(throwable: Throwable): ProfileEffect =
        ProfileEffect(throwable.localizedMessage ?: "Unknown error")

    override fun createInitialState(): ProfileState = ProfileState()

    override suspend fun handleEvent(event: ProfileEvent): ProfileState = when(event) {
        LoadProfile -> {
            val result = authRepository.getCurrentUser()
            when {
                result.isSuccess -> {
                    state.value.copy(
                        userProfile = result.getOrNull()!!,
                        isSignedIn = true,
                        isLoading = false
                    )
                }
                else -> {
                   throw result.exceptionOrNull()!!
                }
            }
        }
    }

}