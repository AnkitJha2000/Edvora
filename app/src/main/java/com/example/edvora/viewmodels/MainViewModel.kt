package com.example.edvora.viewmodels

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.edvora.models.RideResponse
import com.example.edvora.models.RidesResponse
import com.example.edvora.models.UserResponse
import com.example.edvora.repository.RidesRepository
import com.example.edvora.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.abs

class MainViewModel(private val repository : RidesRepository) : ViewModel () {

    val rides : MutableLiveData<Resource<RidesResponse>> = MutableLiveData()

    val distanceSortedRides: MutableLiveData<Resource<RidesResponse>> = MutableLiveData()

    val upcomingRides : MutableLiveData<Resource<RidesResponse>> = MutableLiveData()

    val pastRides : MutableLiveData<Resource<RidesResponse>> = MutableLiveData()

    val filterDistanceSortedRides: MutableLiveData<Resource<RidesResponse>> = MutableLiveData()

    val filterUpcomingRides : MutableLiveData<Resource<RidesResponse>> = MutableLiveData()

    val filterPastRides : MutableLiveData<Resource<RidesResponse>> = MutableLiveData()

    val user : MutableLiveData<Resource<UserResponse>> = MutableLiveData()

    val numberOfUpcomingRide : MutableLiveData<String> = MutableLiveData()

    val numberOfPastRide : MutableLiveData<String> = MutableLiveData()

    val cityList : MutableLiveData<Array<String>> = MutableLiveData()

    val stateList : MutableLiveData<Array<String>> = MutableLiveData()

    var selectedState:String = "State"

    var selectedCity:String = "State"

    fun getAllData() = viewModelScope.launch {
        user.postValue(Resource.Loading())
        repository.getUser().let {
            if(it.isSuccessful){
                user.postValue(Resource.Success(it.body()!!))

                val stationCode = it.body()!!.station_code

                rides.postValue(Resource.Loading())
                repository.getRides().let{ ridesResponse->
                    if(ridesResponse.isSuccessful) {
                        rides.postValue(calculateDistance(ridesResponse, stationCode))
                        filterByDistance(ridesResponse.body()!!.ride)
                        filterByTime(ridesResponse.body()!!.ride)
                        getCityList(ridesResponse.body()!!.ride)
                        getStateList(ridesResponse.body()!!.ride)
                    }
                }
            }
            else{
                Log.d("ANKIT","ERROR")
                Log.d("errorCode",it.code().toString())
            }
        }
    }

    private fun calculateDistance(ridesResponse: Response<RidesResponse>, station_code: Int): Resource<RidesResponse> {
        ridesResponse.body()!!.ride.forEach { dis ->
            var tempDistance = 100000000
            dis.station_path.forEach {  id->
                if (abs(station_code - id) < tempDistance) {
                    tempDistance = abs(station_code - id)
                }
                dis.distance = tempDistance
            }
        }
        return Resource.Success(ridesResponse.body()!!)
    }

    private fun getCityList(rideList : List<RideResponse>){
        val tempCityList = sortedSetOf<String>()
        for(ride in rideList)
        {
            tempCityList.add(ride.city)
        }
        cityList.postValue(tempCityList.toTypedArray())
    }

    private fun getStateList(rideList: List<RideResponse>) {
        val tempStateList = sortedSetOf<String>()
        for(ride in rideList)
        {
            tempStateList.add(ride.state)
        }
        stateList.postValue(tempStateList.toTypedArray())
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun filterByState(stateName: String) {
        filterUpcomingRides.postValue(Resource.Loading())
        filterPastRides.postValue(Resource.Loading())
        filterDistanceSortedRides.postValue(Resource.Loading())
        if(stateName == ""){
            getAllData()
        }
        val currentDRides = distanceSortedRides.value!!.data!!.ride
        val currentDistanceRides: MutableList<RideResponse> = LinkedList(mutableListOf())
        currentDRides.forEach { currentDistanceRides.add(it) }
        currentDistanceRides.removeIf{it.state != stateName}
        Log.d("filter",currentDRides.toString())

        val currentURides = upcomingRides.value!!.data!!.ride
        val currentUpcomingRides: MutableList<RideResponse> = LinkedList(mutableListOf())
        currentURides.forEach { currentUpcomingRides.add(it) }
        currentUpcomingRides.removeIf{it.state != stateName}

        val currentPRides = upcomingRides.value!!.data!!.ride
        val currentPastRides: MutableList<RideResponse> = LinkedList(mutableListOf())
        currentPRides.forEach { currentPastRides.add(it) }
        currentPastRides.removeIf{it.state != stateName}

        filterDistanceSortedRides.postValue(Resource.Success(RidesResponse(currentDistanceRides)))
        filterUpcomingRides.postValue(Resource.Success(RidesResponse(currentUpcomingRides)))
        filterPastRides.postValue(Resource.Success(RidesResponse(currentPastRides)))
        numberOfUpcomingRide.postValue(currentUpcomingRides.size.toString())
        numberOfPastRide.postValue(currentPastRides.size.toString())
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun filterByCity(cityName: String) {
        filterUpcomingRides.postValue(Resource.Loading())
        filterPastRides.postValue(Resource.Loading())
        filterDistanceSortedRides.postValue(Resource.Loading())
        if(cityName == "")
        {
            getAllData()
        }
        val currentDRides = distanceSortedRides.value!!.data!!.ride
        val currentDistanceRides: MutableList<RideResponse> = LinkedList(mutableListOf())
        currentDRides.forEach { currentDistanceRides.add(it) }
        currentDistanceRides.removeIf{it.city != cityName}
        Log.d("filter",currentDRides.toString())

        val currentURides = upcomingRides.value!!.data!!.ride
        val currentUpcomingRides: MutableList<RideResponse> = LinkedList(mutableListOf())
        currentURides.forEach { currentUpcomingRides.add(it) }
        currentUpcomingRides.removeIf{it.city != cityName}

        val currentPRides = upcomingRides.value!!.data!!.ride
        val currentPastRides: MutableList<RideResponse> = LinkedList(mutableListOf())
        currentPRides.forEach { currentPastRides.add(it) }
        currentPastRides.removeIf{it.city != cityName}

        filterDistanceSortedRides.postValue(Resource.Success(RidesResponse(currentDistanceRides)))
        filterUpcomingRides.postValue(Resource.Success(RidesResponse(currentUpcomingRides)))
        filterPastRides.postValue(Resource.Success(RidesResponse(currentPastRides)))
        numberOfUpcomingRide.postValue(currentUpcomingRides.size.toString())
        numberOfPastRide.postValue(currentPastRides.size.toString())
    }

    private fun filterByDistance(rideList : List<RideResponse>) {
        distanceSortedRides.postValue(Resource.Loading())
        distanceSortedRides.postValue(Resource.Success(RidesResponse(rideList.sortedBy { it.distance } as MutableList<RideResponse>)))
    }

    @SuppressLint("SimpleDateFormat", "NewApi")
    private fun filterByTime(rideList : List<RideResponse>) {
        upcomingRides.postValue(Resource.Loading())
        pastRides.postValue(Resource.Loading())
        // val sdf = SimpleDateFormat("mm/dd/yyyy hh:mm a")
        val currentDateAndTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("mm/dd/yyyy hh:mm a"))
        val upcomingList = mutableListOf<RideResponse>()
        val pastList = mutableListOf<RideResponse>()
        rideList.forEach { rides->
            // val ridesTimeStamp = sdf.parse().time
            rides.let {
                if(rides.date>currentDateAndTime){
                    upcomingList.add(rides)
                }
                else{
                    pastList.add(rides)
                }
            }
        }

        upcomingRides.postValue(Resource.Success(RidesResponse(upcomingList)))
        pastRides.postValue(Resource.Success(RidesResponse(pastList)))

        numberOfUpcomingRide.postValue(upcomingList.size.toString())
        numberOfPastRide.postValue(pastList.size.toString())
        //timeSortedRides.postValue(Resource.Success(RidesResponse(rideList.sortedBy { it.date })))
    }


}