package com.example.ricky_kwong_myruns2

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MapActivity : AppCompatActivity(), OnMapReadyCallback, LocationListener {

    private lateinit var mMap: GoogleMap
    private var mapCentered = false
    private lateinit var markerOptions: MarkerOptions
    private lateinit var polylineOptions: PolylineOptions
    private lateinit var polyline: Polyline
    private lateinit var database: RunDatabase
    private lateinit var databaseDao: RunDao
    private lateinit var runRepository: RunRepository
    private lateinit var viewModelFactory: RunViewModelFactory
    private lateinit var runViewModel: RunViewModel

    private var startTime: Long = 0
    private var distance: Float = 0.0f
    private var previousLocation: Location? = null

    private lateinit var locationManager: LocationManager

    private lateinit var infoType: TextView
    private lateinit var infoAvgSpeed: TextView
    private lateinit var infoCurSpeed: TextView
    private lateinit var infoClimb: TextView
    private lateinit var infoCalorie: TextView
    private lateinit var infoDistance: TextView

    private var previousTime: Long = 0
    private var totalElapsedTime: Long = 0
    private var totalDistance: Float = 0f
    private var lastLocation: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        database = RunDatabase.getInstance(this)
        databaseDao = database.runDao
        runRepository = RunRepository(databaseDao)

        infoType = findViewById(R.id.textType)
        infoAvgSpeed = findViewById(R.id.textAvgSpeed)
        infoCurSpeed = findViewById(R.id.textCurSpeed)
        infoClimb = findViewById(R.id.textClimb)
        infoCalorie = findViewById(R.id.textCalorie)
        infoDistance = findViewById(R.id.textDistance)

        val sharedPreferences: SharedPreferences = this.getSharedPreferences("settings", Context.MODE_PRIVATE)
        val units: Int = sharedPreferences.getInt("unit", -1)

        //function from XD locations lecture
        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment)
            as SupportMapFragment
        mapFragment.getMapAsync(this)

        startTime = System.currentTimeMillis()

        val saveBtn: Button = findViewById(R.id.mapSave)
        saveBtn.setOnClickListener {
            saveData(units)
            val serviceIntent = Intent(this, LocationTrackingService::class.java)
            stopService(serviceIntent)
            finish()
        }

        val cancelBtn: Button = findViewById(R.id.mapCancel)
        cancelBtn.setOnClickListener {
            val serviceIntent = Intent(this, LocationTrackingService::class.java)
            stopService(serviceIntent)
            finish()
        }
    }

    private fun calorieBurn(): Int {
        val distanceKM = distance / 1000
        val caloriesBurnt = distanceKM * 0.621371F

        return caloriesBurnt.toInt()
    }


    private fun saveData(units: Int) {
        viewModelFactory = RunViewModelFactory(runRepository)
        runViewModel = ViewModelProvider(this, viewModelFactory).get(RunViewModel::class.java)
        val endTime = System.currentTimeMillis()
        val elapsedTime = endTime - startTime
        var convertedDistance: Float = 0F
        //time implementation from https://www.baeldung.com/kotlin/current-date-time
        val current = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime.now()
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        val sdf = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss")
        val dateTimeString: String = current.format(sdf)

        when (units) {
            0, -1 -> {
                convertedDistance = distance/1000
            }
            else -> {
                val kmDistance = distance/1000
                convertedDistance = Util.kmtoMiles(kmDistance)
            }
        }

        val run = Run(
            entryType = "GPS",
            activityType = "Run",
            dateTime = dateTimeString,
            duration = elapsedTime / 60000f,
            distance = convertedDistance,
            calories = calorieBurn(),
            heartRate = 0,
            comment = "good work!"
        )

        runViewModel.insert(run)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        markerOptions = MarkerOptions()
        polylineOptions = PolylineOptions().width(5f).color(Color.RED)
        polyline = mMap.addPolyline(polylineOptions)

        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL

        checkPermission()
    }

    //implementation from XD lecture
    private fun initLocationManager() {
        try {
            locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) return
            val location =locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (location != null)
                onLocationChanged(location)
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0f, this)
        } catch(e: SecurityException){}
    }
    //implementation from XD Lecture
    override fun onLocationChanged(location: Location) {
        val lat = location.latitude
        val lng = location.longitude
        val latLng = LatLng(lat, lng)

        markerOptions.position(latLng)
        mMap.addMarker(markerOptions)

        polylineOptions.add(latLng)
        polyline.points = polylineOptions.points

        if (!mapCentered) {
            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17f)
            mMap.animateCamera(cameraUpdate)
            markerOptions.position(latLng)

        }

        if (previousLocation != null) {
            val newDistance = location.distanceTo(previousLocation!!)
            distance += newDistance
        }
        previousLocation = location

        if (previousTime == 0L) {
            previousTime = System.currentTimeMillis()
        }

        updateInformationOverlay(location)
    }

    private fun updateInformationOverlay(location: Location) {
        var currentSpeed = 0F
        val climb = 0.0
        val calorie = calorieBurn()
        lateinit var unitsPerTime: String
        lateinit var unitsText: String
        val sharedPreferences: SharedPreferences = this.getSharedPreferences("settings", Context.MODE_PRIVATE)
        val units: Int = sharedPreferences.getInt("unit", -1)

        when (units) {
            0, -1 -> {
                unitsText = "kilometres"
                unitsPerTime = "km/h"
            }
            else -> {
                unitsText = "miles"
                unitsPerTime = "m/h"
            }
        }

        when (units) {
            0, -1 -> {
                currentSpeed = location.speed * 3.6F
            }
            else -> {
                currentSpeed = location.speed * 2.237F
            }
        }


        var convertedDistance: Float = 0F

        when (units) {
            0, -1 -> {
                convertedDistance = distance/1000
            }
            else -> {
                val kmDistance = distance/1000
                convertedDistance = Util.kmtoMiles(kmDistance)
            }
        }

        infoType.text = "Type: GPS"

        infoAvgSpeed.text = "Avg speed: ${calculateAverageSpeed()} $unitsPerTime"

        // Set other information based on your calculations
        infoCurSpeed.text = "Cur speed: $currentSpeed $unitsPerTime"
        infoClimb.text = "Climb: $climb $unitsText"
        infoCalorie.text = "Calorie: $calorie"
        infoDistance.text = "Distance: $convertedDistance $unitsText"
    }

    private fun calculateAverageSpeed(): Float {

        val currentTime = System.currentTimeMillis()
        val elapsedTimeSinceLastUpdate = currentTime - previousTime

        val sharedPreferences: SharedPreferences = this.getSharedPreferences("settings", Context.MODE_PRIVATE)
        val units: Int = sharedPreferences.getInt("unit", -1)

        if (previousLocation != null) {
            totalDistance = distance
            totalElapsedTime += elapsedTimeSinceLastUpdate
        }

        val averageMetersPerSecond: Float = if (totalElapsedTime > 0) {
            totalDistance / (totalElapsedTime / 1000f) } else {
                0f
        }

        previousTime = currentTime
        lastLocation = Location("dummy")

        var averageSpeed: Float = 0F

        when (units) {
            0, -1 -> {
                averageSpeed = averageMetersPerSecond * 3.6F
            }
            else -> {
                averageSpeed = averageMetersPerSecond * 2.237F
            }
        }

        return averageSpeed
    }

    //implementation from XD lecture
    override fun onDestroy() {
        super.onDestroy()
        if (locationManager != null)
            locationManager.removeUpdates(this)
        val serviceIntent = Intent(this, LocationTrackingService::class.java)
        stopService(serviceIntent)
    }
    //implementation from XD lecture
    fun checkPermission() {
        if (Build.VERSION.SDK_INT < 23) return
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 0)
        else initLocationManager()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) initLocationManager()
        }
    }
}