package com.example.githublist.remote.responses

data class Models(
    val incomplete_results: Boolean,
    val items: List<Item>,
    val total_count: Int
)