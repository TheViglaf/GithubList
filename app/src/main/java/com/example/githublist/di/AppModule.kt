package com.example.githublist.di

import com.example.githublist.data.remote.GitHubAPI
import com.example.githublist.repository.GitListRepositoryImpl
import com.example.githublist.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providesGitListRepository(
        api: GitHubAPI
    ) = GitListRepositoryImpl(api)

    @Singleton
    @Provides
    fun provideGitListAPI(): GitHubAPI{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(GitHubAPI::class.java)
    }
}