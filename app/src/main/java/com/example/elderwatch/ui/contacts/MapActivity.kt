package com.example.elderwatch.ui.contacts

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.elderwatch.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var map : GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_map)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val centerButton = findViewById<Button>(R.id.btnCenter)

        centerButton.setOnClickListener {
            centerMap()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.mapType = GoogleMap.MAP_TYPE_NORMAL

        val latitude = intent.getDoubleExtra("latitude", 0.0)
        val longitude = intent.getDoubleExtra("longitude", 0.0)
        val location = LatLng(latitude, longitude)

        map.addMarker(MarkerOptions().position(location).title("Marker in Contact Location"))

        val cameraPosition = CameraPosition.Builder()
            .target(location)
            .zoom(17f)
            .build()
        map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    private fun centerMap() {
        val latitude = intent.getDoubleExtra("latitude", 0.0)
        val longitude = intent.getDoubleExtra("longitude", 0.0)
        val location = LatLng(latitude, longitude)

        val cameraPosition = CameraPosition.Builder()
            .target(location)
            .zoom(17f)
            .build()
        map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

}