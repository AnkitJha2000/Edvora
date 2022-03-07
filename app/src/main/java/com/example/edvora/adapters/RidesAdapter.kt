package com.example.edvora.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.edvora.R
import com.example.edvora.models.RideResponse
import com.google.gson.Gson
import java.lang.Math.abs

class RidesAdapter() : RecyclerView.Adapter<RidesAdapter.RideViewHolder>(){
    inner class RideViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val rideId = itemView.findViewById<TextView>(R.id.item_ride_id)
        val mapImage = itemView.findViewById<ImageView>(R.id.item_map_img)
        val cityName = itemView.findViewById<TextView>(R.id.item_city_name)
        val stateName = itemView.findViewById<TextView>(R.id.item_state_name)
        val originStation = itemView.findViewById<TextView>(R.id.item_origin_station)
        val stationPath = itemView.findViewById<TextView>(R.id.item_station_path)
        val date = itemView.findViewById<TextView>(R.id.item_date)
        val distance: TextView = itemView.findViewById<TextView>(R.id.item_distance)
    }

    private var differCallback = object : DiffUtil.ItemCallback<RideResponse>() {
        override fun areItemsTheSame(oldItem: RideResponse, newItem: RideResponse): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: RideResponse, newItem: RideResponse): Boolean {
            return oldItem == newItem
        }
    }

    var differ = AsyncListDiffer(this,differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RideViewHolder {
        return RideViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.ride_recycler_item_cardview,parent,false))
    }

    override fun onBindViewHolder(holder: RideViewHolder, position: Int) {
        val ride = differ.currentList[position]
        holder.rideId.text = ride.id.toString()
        holder.cityName.text = ride.city
        holder.originStation.text = ride.origin_station_code.toString()
        holder.stateName.text = ride.state
        holder.date.text = ride.date

        var stationString = ride.station_path.joinToString()

        stationString = "[ $stationString ]"

        holder.stationPath.text = stationString
        Glide.with(holder.itemView.context).load(ride.map_url).into(holder.mapImage)

        holder.distance.text = ride.distance.toString()

        // remove after correction
//        if(userStation == -1)
//        {
//            holder.distance.text = "not found"
//            Log.d("ANKIT",userStation.toString())
//        }
//        else {
//            var tempDistance = 10000000000
//            for (dis in ride.station_path) {
//                if (abs(userStation - dis) < tempDistance) {
//                    tempDistance = abs(userStation - dis).toLong()
//                }
//            }
//            holder.distance.text = tempDistance.toString()
//            Log.d("ANKIT",tempDistance.toString())
//        }

    }

    override fun getItemCount(): Int = differ.currentList.size


}