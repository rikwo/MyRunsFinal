package com.example.ricky_kwong_myruns2

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng

class MapViewModel : ViewModel() {
    val routeData = MutableLiveData<List<LatLng>>()

    fun startTracking(inputType: String, activityType: String) {

    }

    fun stopTracking() {

    }

    fun saveRunEntry(startTime: Long, endTime: Long, distance: Float) {

    }
}