package com.example.edvora.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.edvora.R
import com.example.edvora.adapters.RidesAdapter
import com.example.edvora.databinding.FragmentPastRideBinding
import com.example.edvora.databinding.FragmentUpcomingRideBinding
import com.example.edvora.models.RideResponse
import com.example.edvora.ui.MainActivity
import com.example.edvora.utils.Resource
import com.example.edvora.viewmodels.MainViewModel
import java.lang.Math.abs

class UpcomingRideFragment : Fragment(R.layout.fragment_upcoming_ride) {
    private lateinit var viewModel: MainViewModel
    private lateinit var ridesAdapter: RidesAdapter
    private lateinit var _binding : FragmentUpcomingRideBinding
    private var userDistance : Int = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        _binding = FragmentUpcomingRideBinding.inflate(inflater, container, false)
        return _binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel

        setUp()

        viewModel.upcomingRides.observe(viewLifecycleOwner) { response ->

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

        viewModel.filterUpcomingRides.observe(viewLifecycleOwner) { response ->

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
        _binding.upcomingRideRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = ridesAdapter
        }
    }

    private fun hideProgressBar(){
        _binding.upcomingRideProgressBar.visibility = View.INVISIBLE
        // isLoading = false
    }

    private fun showProgressBar(){
        _binding.upcomingRideProgressBar.visibility = View.VISIBLE
        // isLoading = true
    }


}