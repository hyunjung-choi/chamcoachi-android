package com.hyunjung.chamcoach.data

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val DATASTORE_NAME = "user_prefs"

val Context.userDataStore by preferencesDataStore(name = DATASTORE_NAME)

object UserPreferences {

    private fun bookmarkKeyFor(setId: String) = intPreferencesKey("bookmark_index_$setId")

    fun observeBookmarkIndex(context: Context, setId: String): Flow<Int> {
        val key = bookmarkKeyFor(setId)
        return context.userDataStore.data.map { prefs: Preferences ->
            prefs[key] ?: 0
        }
    }

    suspend fun saveBookmarkIndex(context: Context, setId: String, index: Int) {
        val key = bookmarkKeyFor(setId)
        context.userDataStore.edit { prefs ->
            prefs[key] = index
        }
    }
}
