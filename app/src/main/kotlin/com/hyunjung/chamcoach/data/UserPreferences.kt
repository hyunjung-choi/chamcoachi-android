/*
 * Copyright 2025 ChamCoach
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
