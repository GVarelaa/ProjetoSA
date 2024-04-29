package com.example.elderwatch.ui.contacts

import android.util.Log
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.elderwatch.R
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class MapViewModel : ViewModel() {
    private val _location = MutableLiveData<LatLng>()
    val location: LiveData<LatLng> = _location

    private val _lastUpdate = MutableLiveData<String>()
    val lastUpdate: LiveData<String> = _lastUpdate

    private val firestoreDb = FirebaseFirestore.getInstance()

    fun fetchCoordinatesById(userId: String) {
        viewModelScope.launch {
            val documentSnapshot = withContext(Dispatchers.IO) {
                firestoreDb.collection("users")
                    .document(userId)
                    .get()
                    .await()
            }
            if (documentSnapshot.exists()) {
                val locationData = documentSnapshot.data?.get("location") as? Map<String, Any>
                locationData?.let {
                    val lat = it["latitude"] as? Double ?: 0.0
                    val lng = it["longitude"] as? Double ?: 0.0
                    val timestamp = it["timestamp"] as? Timestamp ?: Timestamp.now()

                    _location.postValue(LatLng(lat, lng))
                    _lastUpdate.postValue(formatLastUpdate(timestamp))
                }
            }
        }
    }

    private fun formatLastUpdate(timestamp: Timestamp): String {
        val date = timestamp.toDate()
        val dateFormat = SimpleDateFormat("dd/MM 'às' HH:mm", Locale.getDefault())

        return dateFormat.format(date)
    }
}