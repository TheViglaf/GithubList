package com.example.githublist.gitrepolist

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githublist.data.models.GitHubListEntry
import com.example.githublist.repository.GitListRepositoryImpl
import com.example.githublist.utils.Constants.PAGE_SIZE
import com.example.githublist.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class GitListViewModel @Inject constructor(
    private val repository: GitListRepositoryImpl
) : ViewModel() {
    private var curPage = 0

    var gitRepoList = mutableStateOf<List<GitHubListEntry>>(listOf())
    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var endReached = mutableStateOf(false)

    init {
        loadGitListPaginated()
    }

    fun loadGitListPaginated() {
        viewModelScope.launch {
            isLoading.value = true
            val result = repository.getGitRepoList(page = 1, queryParams = "kotlin", sort = "stars")
            when (result) {
                is Resource.Sucess -> {
                    endReached.value = curPage * PAGE_SIZE >= result.data!!.total_count
                    val githubEntries = result.data.items.mapIndexed { index, item ->
                        val number = if (item.url.endsWith("/")) {
                            item.url.dropLast(1).takeLastWhile { it.isDigit() }
                        } else {
                            item.url.takeLastWhile { it.isDigit() }
                        }
                        val url =
                            "https://api.github.com/search/repositories?q=language:kotlin&sort=stars&page=${number}"
                        GitHubListEntry(item.name.capitalize(Locale.ROOT), url, number.toString())
                    }
                    curPage++

                    loadError.value = ""
                    isLoading.value = false
                    gitRepoList.value += githubEntries
                }
                is Resource.Error -> {
                    loadError.value = result.message!!
                    isLoading.value = false
                }
            }
        }
    }

}