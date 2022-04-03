package com.example.githublist.data.remote

import com.example.githublist.data.remote.responses.Models
import retrofit2.http.GET
import retrofit2.http.Query

interface GitHubAPI {
    @GET("search/repositories")
    suspend fun getRepositoriesByStar(
        @Query("q") queryParams: String,
        @Query("sort") sort: String = "stars",
        @Query("page") page: Int
    ): Models
}
