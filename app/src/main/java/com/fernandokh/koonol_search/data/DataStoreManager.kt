package com.fernandokh.koonol_search.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


private val Context.dataStore by preferencesDataStore(name = "settings")

class DataStoreManager(private val context: Context) {

    private val historySearchKey = stringPreferencesKey("history_search_key")

    suspend fun saveHistoryList(list: List<String>) {
        val jsonString = Gson().toJson(list)
        context.dataStore.edit { preferences ->
            preferences[historySearchKey] = jsonString
        }
    }


    fun getHistoryList(): Flow<List<String>> {
        return context.dataStore.data.map { preferences ->
            val jsonString = preferences[historySearchKey] ?: "[]"
            try {
                val type = object : TypeToken<List<String>>() {}.type
                Gson().fromJson<List<String>>(jsonString, type) ?: emptyList()
            } catch (e: Exception) {
                emptyList()
            }
        }
    }
}