package com.mustafa.myapplication.Home.HomeUi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mustafa.myapplication.Home.HomeNetwork.HomeRemoteDataSourceImpl
import com.mustafa.myapplication.Home.HomeViewModel.HomeViewModel
import com.mustafa.myapplication.Home.HomeViewModel.HomeViewModelFactory
import com.mustafa.myapplication.Home.Repo.HomeRepoImpl
import com.mustafa.myapplication.network.ApiClient
import com.mustafa.myapplication.ui.theme.MyApplicationTheme

class HomeFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Initialize ViewModel with proper dependency chain
        val api = ApiClient.api
        val remoteDataSource = HomeRemoteDataSourceImpl(api)
        val repo = HomeRepoImpl(remoteDataSource)
        val factory = HomeViewModelFactory(repo)
        viewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]

        return ComposeView(requireContext()).apply {
            // Dispose composition when the fragment view is destroyed
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )

            setContent {
                MyApplicationTheme {
                    HomeScreen(viewModel = viewModel)
                }
            }
        }
    }
}