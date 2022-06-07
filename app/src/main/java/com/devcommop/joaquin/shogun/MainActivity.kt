package com.devcommop.joaquin.shogun

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.devcommop.joaquin.shogun.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/*
StateFlow : Is a HotFlow means it will keep emitting even if their are no collectors
 */
class MainActivity : AppCompatActivity(){
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setOnClickListeners()
        subscribeToObservables()
    }

    private fun subscribeToObservables() {
        viewModel.liveData.observe(this){
            binding.btnLiveData.text = it
        }
        //To do for flow we must have coroutines as flows are part of coroutines
        lifecycleScope.launchWhenStarted {
            viewModel.stateFLow.collectLatest {
                binding.btnStateFlow.text = it
            }
            viewModel.sharedFlow.collectLatest {
                binding.btnSharedFlow.text = it
            }
        }
    }

    private fun setOnClickListeners() {
        binding.btnFlow.setOnClickListener{
            lifecycleScope.launch{
                viewModel.triggerFlow().collectLatest{
                    binding.btnFlow.text = it
                }
            }
        }
        binding.btnLiveData.setOnClickListener { viewModel.triggerLiveData() }
        binding.btnStateFlow.setOnClickListener { viewModel.triggerStateFlow() }
        binding.btnSharedFlow.setOnClickListener { viewModel.triggerSharedFlow() }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}