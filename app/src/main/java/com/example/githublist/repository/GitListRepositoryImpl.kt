package com.example.githublist.repository

import com.example.githublist.data.remote.GitHubAPI
import com.example.githublist.data.remote.responses.Models
import com.example.githublist.utils.Resource
import dagger.hilt.android.scopes.ActivityScoped
import java.lang.Exception
import javax.inject.Inject

@ActivityScoped
class GitListRepositoryImpl @Inject constructor(
    private val api: GitHubAPI
) {

    suspend fun getGitRepoList(queryParams: String, sort: String, page: Int): Resource<Models>{
        val response = try {
            api.getRepositoriesByStar(queryParams, sort, page)
        } catch (e: Exception){
            return Resource.Error("Failed to get repos")
        }
        return Resource.Sucess(response)
    }
}