package com.example.edvora.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.edvora.adapters.RidesAdapter
import com.example.edvora.databinding.FragmentNearestRideBinding
import com.example.edvora.models.RideResponse
import com.example.edvora.ui.MainActivity
import com.example.edvora.utils.Resource
import com.example.edvora.viewmodels.MainViewModel
import kotlin.math.abs

class NearestRideFragment : Fragment() {
    private lateinit var viewModel: MainViewModel
    private lateinit var ridesAdapter: RidesAdapter
    private lateinit var _binding : FragmentNearestRideBinding
    private var userDistance : Int = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        _binding = FragmentNearestRideBinding.inflate(inflater, container, false)
        return _binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel

        setUp()

        viewModel.distanceSortedRides.observe(requireActivity()) { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    ridesAdapter.differ.submitList(response.data!!.ride)
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let {
                        Log.d("error", it)
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        }
        viewModel.filterDistanceSortedRides.observe(requireActivity()) { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    ridesAdapter.differ.submitList(response.data!!.ride)
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let {
                        Log.d("error", it)
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        }
    }


    private fun setUp() {
        ridesAdapter = RidesAdapter()
        _binding.nearestRideRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = ridesAdapter
        }
    }

    private fun hideProgressBar(){
        _binding.nearestRideProgressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar(){
        _binding.nearestRideProgressBar.visibility = View.VISIBLE
    }

}
