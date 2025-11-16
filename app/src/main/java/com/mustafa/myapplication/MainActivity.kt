package com.mustafa.myapplication

import SearchViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mustafa.myapplication.Details.DetailsNetwork.DetailsRemoteDataSourcelmpl
import com.mustafa.myapplication.Details.DetailsRepo.DetailsRepoImpl
import com.mustafa.myapplication.Details.DetailsUI.DetailsScreen
import com.mustafa.myapplication.Details.DetailsViewModel.DetailsViewModel
import com.mustafa.myapplication.Details.DetailsViewModel.DetailsViewModelFactory
import com.mustafa.myapplication.Home.HomeNetwork.HomeRemoteDataSourceImpl
import com.mustafa.myapplication.Home.HomeUi.HomeScreen
import com.mustafa.myapplication.Home.HomeViewModel.HomeViewModel
import com.mustafa.myapplication.Home.HomeViewModel.HomeViewModelFactory
import com.mustafa.myapplication.Home.Repo.HomeRepoImpl
import com.mustafa.myapplication.Search.SearchNetwork.SearchRemoteDaraSourceImpl
import com.mustafa.myapplication.Search.SearchRepo.SearchRepoImpl
import com.mustafa.myapplication.Search.SearchScreen
import com.mustafa.myapplication.Search.SearchViewModel.SearchViewModelFactory
import com.mustafa.myapplication.network.ApiClient
import com.mustafa.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize ViewModels
        val api = ApiClient.api
        val remoteDataSource = HomeRemoteDataSourceImpl(api)
        val repo = HomeRepoImpl(remoteDataSource)
        val factory = HomeViewModelFactory(repo)
        homeViewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]

        setContent {
            MyApplicationTheme {
                MainScreen(homeViewModel = homeViewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(homeViewModel: HomeViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val isSearchScreen: Boolean = currentRoute == Screen.Search.route
    val isDetailsScreen: Boolean = currentRoute?.startsWith("details/") == true

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            when {
                isDetailsScreen -> {
                    TopAppBar(
                        title = { Text("Movie Details") },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    )
                }
                isSearchScreen -> {
                    TopAppBar(
                        title = { Text("Search Movies") },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    )
                }
                else -> {
                    TopAppBar(
                        title = { Text("Popular Movies") },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    )
                }
            }
        },
        bottomBar = {
            if (!isDetailsScreen) {
                NavigationBar {
                    val currentDestination = navBackStackEntry?.destination

                    bottomNavItems.forEach { item ->
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.title
                                )
                            },
                            label = { Text(item.title) },
                            selected = currentDestination?.hierarchy?.any {
                                it.route == item.route
                            } == true,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    viewModel = homeViewModel,
                    onMovieClick = { movieId ->
                        navController.navigate("details/$movieId")
                    }
                )
            }

            composable(Screen.Search.route) {
                val searchDataSource = remember { SearchRemoteDaraSourceImpl() }
                val searchRepo = remember { SearchRepoImpl(searchDataSource) }
                val searchViewModel: SearchViewModel = viewModel(
                    factory = SearchViewModelFactory(searchRepo)
                )

                SearchScreen(
                    viewModel = searchViewModel,
                    onMovieClick = { movieId ->
                        navController.navigate("details/$movieId")
                    }
                )
            }

            composable(
                route = "details/{movieId}",
                arguments = listOf(navArgument("movieId") { type = NavType.IntType })
            ) { backStackEntry ->
                val movieId = backStackEntry.arguments?.getInt("movieId") ?: 0

                val detailsDataSource = remember { DetailsRemoteDataSourcelmpl() }
                val detailsRepo = remember { DetailsRepoImpl(detailsDataSource) }
                val detailsViewModel: DetailsViewModel = viewModel(
                    factory = DetailsViewModelFactory(detailsRepo)
                )

                DetailsScreen(
                    movieId = movieId,
                    viewModel = detailsViewModel
                )
            }
        }
    }
}

// Navigation items
sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screen("home", "Home", Icons.Filled.Home)
    object Search : Screen("search", "Search", Icons.Filled.Search)
    object Details : Screen("details/{movieId}", "Details", Icons.Filled.Info)
}

val bottomNavItems = listOf(
    Screen.Home,
    Screen.Search
)