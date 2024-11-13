package com.example.whiteboard.domain

interface ILocalRepository {
    suspend fun isSignedIn(): Boolean?
    suspend fun saveSignIn(signInStatus: Boolean)
}