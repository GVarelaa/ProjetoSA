package com.example.elderwatch.utils

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.elderwatch.R
import com.google.firebase.Timestamp
import kotlin.math.pow
import kotlin.math.sqrt

class SensorService : Service(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private var sensorData: MutableList<HashMap<String, Any>> = mutableListOf()
    private var lastUpdate: Long = 0
    private val updateThreshold: Long = 30000 // Tempo em milissegundos (30 segundos)
    private val lowThreshold: Double = 2.5
    private val highThreshold: Double = 8.0
    override fun onCreate() {
        super.onCreate()

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)

        if (accelerometer != null){
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notification = Notification.Builder(this, "sensor_service")
                .setSmallIcon(R.drawable.ic_eye)
                .setColor(Color.parseColor("#FF0000"))
                .setContentTitle("Serviço de Sensorização Ativo")
                .setContentText("A coletar dados dos sensores.")
                .build()

            startForeground(1, notification)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()

        sensorManager.unregisterListener(this)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_LINEAR_ACCELERATION) {
            val x: Double = event.values[0].toDouble().pow(2.toDouble())
            val y: Double = event.values[1].toDouble().pow(2.toDouble())
            val z: Double = event.values[2].toDouble().pow(2.toDouble())
            val magnitude = sqrt(x+y+z)

            val data = HashMap<String, Any>()
            data["x"] = x
            data["y"] = y
            data["z"] = z
            data["magnitude"] = magnitude
            data["timestamp"] = Timestamp.now()

            sensorData.add(data)

            val currentTime = System.currentTimeMillis()

            if ((currentTime - lastUpdate) > updateThreshold) {
                lastUpdate = currentTime

                val fall = detectFall()

                if (fall){
                    DataSender.sendFall()

                    Log.d("FALL DETECTION", "True")
                }
                else Log.d("FALL DETECTION", "False")

                DataSender.sendSensorData(sensorData, fall)

                sensorData.clear()
            }
        }
    }

    private fun detectFall(): Boolean {
        for (i in 0 until sensorData.size) {
            if ((sensorData[i]["magnitude"] as Double) < lowThreshold) {
                for (j in i+1 until sensorData.size) {
                    if ((sensorData[j]["magnitude"] as Double) > highThreshold) return true
                }
            }
        }

        return false
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }
}