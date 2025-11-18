package com.mustafa.myapplication
import SearchViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
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
import com.mustafa.myapplication.Search.SearchUI.SearchScreen
import com.mustafa.myapplication.Search.SearchViewModel.SearchViewModelFactory
import com.mustafa.myapplication.network.ApiClient
import com.mustafa.myapplication.ui.theme.MyApplicationTheme
import com.mustafa.myapplication.ui.theme.Routes

class MainActivity : ComponentActivity() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize ViewModels
        val api = ApiClient.api
        val remoteDataSource= HomeRemoteDataSourceImpl(api)
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

    val isSearchScreen : Boolean = currentRoute==  Routes.Search::class.qualifiedName
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {

            if (!isSearchScreen){
                TopAppBar(
                    title = { Text("Popular Movies") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
            }else{
                TopAppBar(
                    title = { Text("Search Movies") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
            }
            }
            ,
        bottomBar = {
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
                            currentDestination == item.route
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
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Routes.Home,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable<Routes.Home> {
                HomeScreen(viewModel = homeViewModel)
            }

            composable<Routes.Search> {
                val searchDataSource = remember { SearchRemoteDaraSourceImpl() }
                val searchRepo = remember { SearchRepoImpl(searchDataSource) }
                val searchViewModel : SearchViewModel = viewModel(factory = SearchViewModelFactory(searchRepo))

                SearchScreen(searchViewModel ,navController)

            }
            composable<Routes.Details>{
                navBackStackEntry ->
                val details : Routes.Details = navBackStackEntry.toRoute()
                val detailsDataSource = remember { DetailsRemoteDataSourcelmpl() }
                val detailsRepo = remember { DetailsRepoImpl(detailsDataSource) }
                val detailsViewModel : DetailsViewModel = viewModel(factory = DetailsViewModelFactory(detailsRepo))

                DetailsScreen(movieId = details.movieId , viewModel = detailsViewModel)
            }
        }
    }
}


data class BottomNavItem(
    val title: String,
    val icon: ImageVector,
    val route: Routes
)
val bottomNavItems = listOf(
    BottomNavItem("Home", Icons.Default.Home, Routes.Home),
    BottomNavItem("Search", Icons.Default.Search, Routes.Search),
)