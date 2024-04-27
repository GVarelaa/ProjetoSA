package com.example.elderwatch

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.elderwatch.databinding.ActivityMainBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import android.Manifest
import com.example.elderwatch.utils.SensorListener
import com.example.elderwatch.utils.UserManager
import com.google.firebase.firestore.FirebaseFirestore


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestNotificationPermission()
        db = FirebaseFirestore.getInstance()
        val user = UserManager

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCM Token", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            val tokenField = hashMapOf(
                "token" to task.result
            )

            user.uid?.let {
                db.collection("users")
                    .document(it)
                    .update(tokenField as Map<String, Any>)
            }
        })

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        navView.setupWithNavController(navController)

        val sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)
        val sensorListener = SensorListener()

        sensorListener.setSensorManager(sensorManager)

        if (accelerometer != null){
            sensorManager.registerListener(sensorListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    private fun requestNotificationPermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val hasPermission = ContextCompat.checkSelfPermission(
                this,
                        Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if(!hasPermission) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    0
                )
            }
        }
    }
}