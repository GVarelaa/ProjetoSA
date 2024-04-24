package com.example.elderwatch

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import java.lang.Math.pow
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
            /*AccelerometerData.valueX = event.values[0]
            AccelerometerData.valueY = event.values[1]
            AccelerometerData.valueZ = event.values[2]
            AccelerometerData.accuracy = event.accuracy

            //Log.d("Accelerometer", "[SENSOR] - X ${AccelerometerData.valueX}, Y ${AccelerometerData.valueY}, Z ${AccelerometerData.valueZ}")
            val a: Double = AccelerometerData.valueX.toDouble().pow(2.toDouble())
            val b: Double = AccelerometerData.valueY.toDouble().pow(2.toDouble())
            val c: Double = (AccelerometerData.valueZ-9.8).toDouble().pow(2.toDouble())
            Log.d("Accelerometer", "${sqrt(a+b+c)}")*/

            val a: Double = event.values[0].toDouble().pow(2.toDouble())
            val b: Double = event.values[1].toDouble().pow(2.toDouble())
            val c: Double = event.values[2].toDouble().pow(2.toDouble())
            val magnitude = sqrt(a+b+c)

            sensorData.add(magnitude)
            val currentTime = System.currentTimeMillis()

            if ((currentTime - lastUpdate) > updateThreshold && event != null) {
                lastUpdate = currentTime

                Log.d("Deteção", "A processar queda.....")

                for (i in 0 until sensorData.size) {
                    if (sensorData[i] < lowThreshold) {
                        for (j in i+1 until sensorData.size) {
                            if (sensorData[j] > highThreshold) Log.d("QUEDA", "QUEDAAAA")
                        }
                    }
                }

                sensorData.clear()

                Log.d("NÃO QUEDA", "NÃOOOO QUEDAAAA")
            }
        }

        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            GyroscopeData.valueX = event.values[0]
            GyroscopeData.valueY = event.values[1]
            GyroscopeData.valueZ = event.values[2]
            GyroscopeData.accuracy = event.accuracy

            //Log.d("Gyroscope", "[SENSOR] - X ${GyroscopeData.valueX}, Y ${GyroscopeData.valueY}, Z ${GyroscopeData.valueZ}")
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }
}