package com.example.edvora.ui

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Display
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.edvora.R
import com.example.edvora.adapters.ViewPagerAdapter
import com.example.edvora.databinding.ActivityMainBinding
import com.example.edvora.repository.RidesRepository
import com.example.edvora.viewmodels.MainViewModel
import com.example.edvora.viewmodels.MainViewModelProviderFactory
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.textfield.TextInputLayout

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private lateinit var _binding : ActivityMainBinding
    lateinit var viewModel: MainViewModel
    private var numberOfUpcomingRides = ""
    private var numberOfPastRides = ""
    private lateinit var popUpWindow : Dialog
    private var popUpState = false


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        val view = _binding.root
        setContentView(view)

        val repository = RidesRepository()
        val viewModelFactory = MainViewModelProviderFactory(repository)
        viewModel = ViewModelProvider(this@MainActivity,viewModelFactory)[MainViewModel::class.java]

        popUpWindow = Dialog(this)

        _binding.apply {
            val adapter = ViewPagerAdapter(this@MainActivity)
            mainViewPager.adapter = adapter
            viewModel.getAllData()
            viewModel.numberOfPastRide.observe(this@MainActivity){
                it?.let{
                    numberOfPastRides = "($it)"
                    changeDataInTabLayout(mainTabLayout,mainViewPager)
                }
            }

            viewModel.numberOfUpcomingRide.observe(this@MainActivity){
                it?.let{
                    numberOfUpcomingRides = "($it)"
                    changeDataInTabLayout(mainTabLayout,mainViewPager)
                }
            }

            viewModel.user.observe(this@MainActivity) {
                it?.let {
                    tvUserName.text=it.data?.name
                    Glide.with(this@MainActivity).load(it.data?.url.toString()).into(btnUserImage)
                }
            }
            changeDataInTabLayout(mainTabLayout,mainViewPager)
            btnFilters.setOnClickListener {
                popUpState = !popUpState
                popUpState = if(popUpState) {
                    showPopWindow()
                    !popUpState
                } else {
                    closePopWindow()
                    !popUpState
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun showPopWindow() {
        popUpWindow.setContentView(R.layout.custom_filter_pop_up)

        val menuCity = popUpWindow.findViewById<TextInputLayout>(R.id.cityMenu)
        val menuState = popUpWindow.findViewById<TextInputLayout>(R.id.stateMenu)

        menuCity.hint = viewModel.selectedCity
        menuState.hint = viewModel.selectedState

        val autoCompleteViewCity = popUpWindow.findViewById<AutoCompleteTextView>(R.id.autoCompleteViewCity)
        val autoCompleteViewState = popUpWindow.findViewById<AutoCompleteTextView>(R.id.autoCompleteViewState)

        val display: Display = windowManager.defaultDisplay
        val width = display.width // deprecated

        var widthPopUp: Int
        var heightPopUp : Int
        _binding.apply {
            heightPopUp = mainTabLayout.height - width/2
            widthPopUp = rlheading.width
        }

        popUpWindow.window!!.attributes.x = widthPopUp
        popUpWindow.window!!.attributes.y = heightPopUp

        popUpWindow.show()

        viewModel.cityList.observe(this){
            autoCompleteViewCity.setAdapter(ArrayAdapter(this,R.layout.filter_dropdown_item_layout,it))
        }


        viewModel.stateList.observe(this){
            autoCompleteViewState.setAdapter(ArrayAdapter(this,R.layout.filter_dropdown_item_layout,it))
        }


        autoCompleteViewState.setOnItemClickListener { adapterView, view, i, l ->
            Log.d("filterList",adapterView.adapter.getItem(i).toString())
            viewModel.selectedState = adapterView.adapter.getItem(i).toString()
            viewModel.filterByState(adapterView.adapter.getItem(i).toString())
        }


        autoCompleteViewCity.setOnItemClickListener { adapterView, view, i, l ->
            viewModel.selectedCity = adapterView.adapter.getItem(i).toString()
            viewModel.filterByCity(adapterView.adapter.getItem(i).toString())
        }


    }
    private fun closePopWindow() {
        popUpWindow.dismiss()
    }

    private fun changeDataInTabLayout(mainTabLayout: TabLayout, mainViewPager: ViewPager2) {
        TabLayoutMediator(mainTabLayout, mainViewPager) { tab, position ->
            tab.text = when(position) {
                0 -> "Nearest"
                1 -> "Upcoming $numberOfUpcomingRides"
                else -> {"Past $numberOfPastRides"}
            }
        }.attach()
    }

}
