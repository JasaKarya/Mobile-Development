package com.jasakarya.di

import android.content.Context
import com.jasakarya.data.repository.Repository
import com.jasakarya.data.source.remote.ApiConfig

object Injection {
    fun provideRepository(context: Context) : Repository {
        val apiService = ApiConfig.getApiServices()
        return Repository(apiService, context)
    }
}