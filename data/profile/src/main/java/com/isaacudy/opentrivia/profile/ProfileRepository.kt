package com.isaacudy.opentrivia.profile

import javax.inject.Inject

class ProfileRepository @Inject constructor() {
    suspend fun getCurrentUser(): UserEntity? {
        return null
    }
}