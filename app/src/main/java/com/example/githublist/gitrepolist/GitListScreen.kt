package com.example.githublist.gitrepolist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import coil.request.ImageRequest
import com.example.githublist.R
import com.example.githublist.data.models.GitHubListEntry
import com.google.accompanist.coil.CoilImage

@Composable
fun GitListScreen(
    navController: NavController,
    viewModel: GitListViewModel = hiltNavGraphViewModel()
) {
    Surface(
        color = MaterialTheme.colors.background,
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            Spacer(modifier = Modifier.height(20.dp))
            Image(
                painterResource(id = R.drawable.ic_github_logo),
                contentDescription = "Github",
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            )

        }
        Spacer(modifier = Modifier.height(16.dp))
        GitList(navController = navController)
    }
}

@Composable
fun GitList(
    navController: NavController,
    viewModel: GitListViewModel = hiltNavGraphViewModel()
) {
    val gitList by remember { viewModel.gitRepoList }
    val endReached by remember { viewModel.endReached }
    val loadError by remember { viewModel.loadError }
    val isLoading by remember { viewModel.isLoading }
    val isSearching by remember { viewModel.isLoading }

    LazyColumn(contentPadding = PaddingValues(16.dp)) {
        val itemCount = if (gitList.size % 2 == 0) {
            gitList.size / 2
        } else {
            gitList.size / 2 + 1
        }
        items(itemCount) {
            if (it >= itemCount - 1 && !endReached && !isLoading && !isSearching) {
                viewModel.loadGitListPaginated()
            }
            PokedexRow(rowIndex = it, entries = gitList, navController = navController)
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        if (isLoading) {
            CircularProgressIndicator(color = MaterialTheme.colors.primary)
        }
        if (loadError.isNotEmpty()) {
            RetrySection(error = loadError) {
                viewModel.loadGitListPaginated()

            }
        }


    }

}

@Composable
fun PokedexEntry(
    entry: GitHubListEntry,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: GitListViewModel = hiltNavGraphViewModel()
) {
    val defaultDominantColor = MaterialTheme.colors.surface
    var dominantColor by remember {
        mutableStateOf(defaultDominantColor)

    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .shadow(5.dp, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .aspectRatio(1f)
            .background(
                Brush.verticalGradient(
                    listOf(
                        dominantColor,
                        defaultDominantColor
                    )
                )
            )
    ) {
        Column {
            CoilImage(
                request = ImageRequest.Builder(LocalContext.current)
                    .data(entry.imageURL)
                    .target {
                    }
                    .build(),
                contentDescription = entry.repoName,
                fadeIn = true,
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.scale(0.5f)
                )
            }
            Text(
                text = entry.repoName,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

    }
}

@Composable
fun PokedexRow(
    rowIndex: Int,
    entries: List<GitHubListEntry>,
    navController: NavController
) {
    Column {
        Row {
            PokedexEntry(
                entry = entries[rowIndex * 2],
                navController = navController,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            if (entries.size >= rowIndex * 2 + 2) {
                PokedexEntry(
                    entry = entries[rowIndex * 2 + 1],
                    navController = navController,
                    modifier = Modifier.weight(1f)
                )
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun RetrySection(
    error: String,
    onRetry: () -> Unit
) {
    Column {
        Text(error, color = Color.Red, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { onRetry },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Retry")

        }

    }
}