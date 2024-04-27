package com.example.elderwatch.utils

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import kotlin.math.pow
import kotlin.math.sqrt

class SensorListener : SensorEventListener {
    companion object{
        private const val TAG: String = "AccelerometerSensorListener"
    }

    private lateinit var sensorManager: SensorManager
    private val sensorData: MutableList<Double> = mutableListOf()
    private var lastUpdate: Long = 0
    private val updateThreshold: Long = 10000 // Tempo em milissegundos
    private val lowThreshold: Double = 2.5
    private val highThreshold: Double = 8.0

    fun setSensorManager(sensorMan: SensorManager){
        sensorManager = sensorMan
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            val a: Double = event.values[0].toDouble().pow(2.toDouble())
            val b: Double = event.values[1].toDouble().pow(2.toDouble())
            val c: Double = event.values[2].toDouble().pow(2.toDouble())
            val magnitude = sqrt(a+b+c)

            sensorData.add(magnitude)

            val currentTime = System.currentTimeMillis()

            if ((currentTime - lastUpdate) > updateThreshold) {
                lastUpdate = currentTime

                val fall = detectFall()

                if (fall){
                    Log.d("FALL DETECTION", "True")

                }
                else {
                    Log.d("FALL DETECTION", "False")
                }
            }
        }
    }

    private fun detectFall(): Boolean {
        for (i in 0 until sensorData.size) {
            if (sensorData[i] < lowThreshold) {
                for (j in i+1 until sensorData.size) {
                    if (sensorData[j] > highThreshold) {
                        sensorData.clear()
                        return true
                    }
                }
            }
        }

        sensorData.clear()
        return false
    }
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }
}