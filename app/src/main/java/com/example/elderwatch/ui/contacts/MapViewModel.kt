package com.example.elderwatch.ui.contacts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class MapViewModel : ViewModel() {
    private val _location = MutableLiveData<LatLng>()
    val location: LiveData<LatLng> = _location

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
                val locationData = documentSnapshot.data?.get("location") as? Map<String, Double>
                locationData?.let {
                    val lat = it["latitude"] ?: 0.0
                    val lng = it["longitude"] ?: 0.0
                    _location.postValue(LatLng(lat, lng))
                }
            }
        }
    }
}