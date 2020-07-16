package com.qwert2603.geotrack

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import kotlinx.coroutines.launch

class MainActivityViewModel : ViewModel() {
    val geoInfoDatabase = Room
        .databaseBuilder(App.APP_CONTEXT, GeoInfoDatabase::class.java, "geo_info.sqlite")
        .fallbackToDestructiveMigration()
        .build()

    val started = MutableLiveData(false)

    val accuracy = MutableLiveData("")

    private val locationManager =
        App.APP_CONTEXT.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    private val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            accuracy.value =
                "accuracy=${location.accuracy}\nbearing=${location.bearing}\nspeed=${location.speed}"

            viewModelScope.launch {
                val geoInfo = GeoInfo(
                    id = 0,
                    lat = location.latitude,
                    lon = location.longitude,
                    millis = System.currentTimeMillis(),
                    speed = location.speed
                )
                geoInfoDatabase.getInfoDao().insertGeoInfo(geoInfo)
            }
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) = Unit
        override fun onProviderEnabled(provider: String?) = Unit
        override fun onProviderDisabled(provider: String?) = Unit
    }

    @SuppressLint("MissingPermission")
    fun start() {
        started.value = true

        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            0L,
            0f,
            locationListener
        )

//        Executors.newSingleThreadExecutor().submit {
//            while (started.value!!) {
//                Thread.sleep(3000)
//
//                val geoInfo = GeoInfo(
//                    id = 0,
//                    lat = Random.nextDouble(),
//                    lon = Random.nextDouble(),
//                    millis = System.currentTimeMillis()
//                )
//                geoInfoDatabase.getInfoDao().insertGeoInfo(geoInfo)
//            }
//        }
    }

    fun stop() {
        started.value = false

        locationManager.removeUpdates(locationListener)
    }
}