package com.example.elderwatch.ui.contacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.elderwatch.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MapFragment : Fragment(), OnMapReadyCallback {
    private lateinit var map: GoogleMap
    private val viewModel: MapViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val centerButton = view.findViewById<Button>(R.id.btnCenter)
        centerButton.setOnClickListener {
            centerMap()
        }

        val contactId = arguments?.getString("contactId")
            ?: throw IllegalStateException("Contact ID is required")
        viewModel.fetchCoordinatesById(contactId)
        observeViewModel()

        val backButton = view.findViewById<Button>(R.id.backButton)
        backButton.setOnClickListener {
            findNavController().navigate(R.id.action_mapFragment_to_contactsFragment)
        }
    }

    private fun observeViewModel() {
        viewModel.location.observe(viewLifecycleOwner) { location ->
            if (::map.isInitialized) {
                updateMap(location)
                viewModel.lastUpdate.observe(viewLifecycleOwner) { lastUpdate ->
                    val textView = view?.findViewById<TextView>(R.id.lastUpdateTextView)
                    textView?.text = "Última Atualização: $lastUpdate"
                }
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.mapType = GoogleMap.MAP_TYPE_HYBRID

        viewModel.location.value?.let { updateMap(it) }
    }

    private fun updateMap(location: LatLng) {
        if (::map.isInitialized) {
            map.clear()
            map.addMarker(MarkerOptions().position(location))

            val cameraPosition = CameraPosition.Builder()
                .target(location)
                .zoom(19f)
                .build()

            map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        }
    }

    private fun centerMap() {
        if (::map.isInitialized) {
            viewModel.location.value?.let {
                updateMap(it)
            }
        }
    }
}