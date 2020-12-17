package com.isaacudy.opentrivia.trivia.launcher

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.isaacudy.opentrivia.core.ui.ErrorViewParameters
import com.isaacudy.opentrivia.navigation.TriviaGameScreen
import com.isaacudy.opentrivia.navigation.TriviaLauncherScreen
import com.isaacudy.opentrivia.observe
import com.isaacudy.opentrivia.trivia.launcher.databinding.TriviaLauncherFragmentBinding
import com.isaacudy.opentrivia.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import nav.enro.annotations.NavigationDestination
import nav.enro.core.NavigationKey
import nav.enro.core.forward
import nav.enro.core.getNavigationHandle
import nav.enro.core.navigationHandle
import nav.enro.viewmodel.enroViewModels

@AndroidEntryPoint
@NavigationDestination(TriviaLauncherScreen::class)
class TriviaLauncherFragment : Fragment(R.layout.trivia_launcher_fragment) {

    private val binding by viewBinding<TriviaLauncherFragmentBinding>()
    private val viewModel by enroViewModels<TriviaLauncherViewModel>()

    private val navigation by navigationHandle<TriviaLauncherScreen>()

    private val adapter = TriviaCategoryAdapter {
        navigation.forward(TriviaGameScreen(it.id))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initialiseRecyclerView()
        initialiseErrorView()
        viewModel.observe(this, ::onStateUpdated)
    }

    private fun initialiseRecyclerView() {
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        binding.swipeRefreshView.setOnRefreshListener {
            viewModel.onRefreshed()
        }
        binding.swipeRefreshView.setColorSchemeResources(R.color.primary)
    }

    private fun initialiseErrorView() {
        binding.errorView.set(
            ErrorViewParameters(
                title = "Whoops",
                message = "It looks like something went wrong, please try again",
                button = "Retry",
                onButtonClicked = { viewModel.onRefreshed() }
            )
        )
    }

    private fun onStateUpdated(state: TriviaLauncherState) {

        binding.errorView.isVisible = state is TriviaLauncherState.Error

        binding.swipeRefreshView.isVisible = state is TriviaLauncherState.Loaded || state is TriviaLauncherState.Loading
        binding.swipeRefreshView.isRefreshing = state is TriviaLauncherState.Loading

        when (state) {
            TriviaLauncherState.None -> {
            }
            TriviaLauncherState.Loading -> {
            }
            TriviaLauncherState.Error -> {
            }
            is TriviaLauncherState.Loaded -> {
                adapter.submitList(state.categories)
            }
        }
    }
}