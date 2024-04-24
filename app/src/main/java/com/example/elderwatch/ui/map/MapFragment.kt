package com.example.elderwatch.ui.map

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.elderwatch.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task


class MapFragment : Fragment(), OnMapReadyCallback {

    companion object {
        fun newInstance() = MapFragment()
    }

    private lateinit var viewModel: MapViewModel
    private lateinit var map : GoogleMap
    private val FINE_PERMISSION_CODE: Int = 1
    var currentLocation: Location? = null
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private fun getLastLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission not granted, request it
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                FINE_PERMISSION_CODE
            )
            return
        }

        val task: Task<Location> = fusedLocationProviderClient.lastLocation
        task.addOnSuccessListener { location ->
            if (location != null) {
                currentLocation = location
                val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
                mapFragment.getMapAsync(this)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_map, container, false)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        getLastLocation()
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MapViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onMapReady(@NonNull googleMap: GoogleMap) {
        map = googleMap
        val loc = LatLng(currentLocation!!.latitude, currentLocation!!.longitude)
        //map.addMarker(MarkerOptions().position(sydney).title("Sydney"))
        map.moveCamera(CameraUpdateFactory.newLatLng(loc))
        val options = MarkerOptions().position(loc).title("My Location")
        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
        map.addMarker(options)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == FINE_PERMISSION_CODE){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation()
            }
            else{
                Toast.makeText(requireContext(), "Location permission is denied. Please allow the permission.", Toast.LENGTH_SHORT).show()
            }
        }
    }

}