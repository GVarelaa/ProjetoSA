package com.example.elderwatch.utils

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.pow
import kotlin.math.sqrt

class SensorListener : SensorEventListener {
    companion object{
        private const val TAG: String = "AccelerometerSensorListener"
    }

    private lateinit var sensorManager: SensorManager
    private val sensorData: MutableList<HashMap<String, Any>> = mutableListOf()
    private var lastUpdate: Long = 0
    private val updateThreshold: Long = 30000 // Tempo em milissegundos (30 segundos)
    private val lowThreshold: Double = 2.5
    private val highThreshold: Double = 8.0

    fun setSensorManager(sensorMan: SensorManager){
        sensorManager = sensorMan
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
            data["timestamp"] = DateTimeFormatter.ISO_INSTANT.format(Instant.now())

            sensorData.add(data)

            val currentTime = System.currentTimeMillis()

            if ((currentTime - lastUpdate) > updateThreshold) {
                lastUpdate = currentTime

                val fall = detectFall()

                if (fall){
                    DataSender.notifyContacts()

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