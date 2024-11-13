package com.example.whiteboard.data


import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.whiteboard.domain.ILocalRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

const val DATASTORE_NAME = "USER_INFO"


val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = DATASTORE_NAME)

class LocalRepository(private val context: Context) : ILocalRepository {

    companion object {
        val IS_LOGGED_IN = booleanPreferencesKey("IS_LOGGED_IN")
    }

    override suspend fun isSignedIn(): Boolean? = context.datastore.data.map { preference ->
        preference[IS_LOGGED_IN]
    }.first()


    override suspend fun saveSignIn(signInStatus: Boolean) {
        context.datastore.edit { preference ->
            preference[IS_LOGGED_IN] = signInStatus
        }

    }

}