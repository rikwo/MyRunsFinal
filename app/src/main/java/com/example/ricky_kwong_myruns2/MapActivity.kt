package com.example.ricky_kwong_myruns2

import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions

class MapActivity : AppCompatActivity(), OnMapReadyCallback, LocationListener {

    private lateinit var mMap: GoogleMap
    private var mapCentered = false
    private lateinit var markerOptions: MarkerOptions


    private var startTime: Long = 0
    private var distance: Float = 0.0f
    private var previousLocation: Location? = null

    private lateinit var locationManager: LocationManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        //function from XD locations lecture
        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment)
            as SupportMapFragment
        mapFragment.getMapAsync(this)

        startTime = System.currentTimeMillis()

        val saveBtn: Button = findViewById(R.id.mapSave)
        saveBtn.setOnClickListener {
            saveData()
            finish()
        }

        val cancelBtn: Button = findViewById(R.id.mapCancel)
        cancelBtn.setOnClickListener {
            finish()
        }
    }

    private fun saveData() {
        val endTime = System.currentTimeMillis()
        val elapsedTime = endTime - startTime
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

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

        if (previousLocation != null) {
            val newDistance = location.distanceTo(previousLocation!!)
            distance += newDistance
        }
        previousLocation = location
    }
    //implementation from XD lecture
    override fun onDestroy() {
        super.onDestroy()
        if (locationManager != null)
            locationManager.removeUpdates(this)
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