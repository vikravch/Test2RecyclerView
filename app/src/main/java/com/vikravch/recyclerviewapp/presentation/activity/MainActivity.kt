package com.vikravch.recyclerviewapp.presentation.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.vikravch.recyclerviewapp.R
import com.vikravch.recyclerviewapp.databinding.ActivityMainBinding
import com.vikravch.recyclerviewapp.presentation.adapter.EndlessScrollListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        binding.recyclerView.adapter = viewModel.adapter

        binding.recyclerView.addOnScrollListener(object : EndlessScrollListener() {
            override fun getCount() = viewModel.adapter.itemCount
            override fun loadItems() = viewModel.loadItems()
        })
    }

    override fun onStart() {
        super.onStart()
        viewModel.initAdapter()
        lifecycleScope.launch {
            viewModel.uiEvent.collect{ event ->
                when (event) {
                    is MainActivityViewModel.UiEvent.LoadedInitError -> {
                        Snackbar.make(binding.root, event.message, Snackbar.LENGTH_LONG)
                            .setAction(R.string.load_again) {
                                viewModel.initAdapter()
                            }.show()
                    }

                    is MainActivityViewModel.UiEvent.LoadedMoreError -> {
                        Snackbar.make(binding.root, event.message, Snackbar.LENGTH_LONG)
                            .setAction(R.string.load_again) {
                                viewModel.loadItems()
                            }.show()
                    }
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        lifecycleScope.launch {
            viewModel.uiEvent.collect {}
        }
    }
}