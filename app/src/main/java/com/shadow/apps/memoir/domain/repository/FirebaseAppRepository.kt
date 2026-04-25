package com.shadow.apps.memoir.domain.repository

import com.shadow.apps.memoir.domain.model.FirebaseCredentials

/*
 * Firebase app repository
 *
 * Dynamic Firebase app initialization contract for BYOF setup.
 */
interface FirebaseAppRepository {
    fun initialise(credentials: FirebaseCredentials)
    fun isInitialised(): Boolean
}
