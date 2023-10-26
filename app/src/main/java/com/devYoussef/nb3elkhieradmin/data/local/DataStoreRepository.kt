package com.devYoussef.nb3elkhieradmin.data.local

import androidx.core.content.ContentProviderCompat.requireContext
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first

class DataStoreRepository(private val dataStore: DataStore<Preferences>) {

    suspend fun getToken(key: String): String? {
        val dataStoreKey: Preferences.Key<String> = stringPreferencesKey(key)
        val preference = dataStore.data.first()
        return preference[dataStoreKey]
    }

    suspend fun saveToken(key: String, value: String) {
        val dataStoreKey: Preferences.Key<String> = stringPreferencesKey(key)
        dataStore.edit { preferences ->
            preferences[dataStoreKey] = value
        }
    }


}



