package com.example.elderwatch

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log

class SensorListener : SensorEventListener {
    companion object{
        private const val TAG: String = "AccelerometerSensorListener"
    }

    private lateinit var sensorManager: SensorManager

    fun setSensorManager(sensorMan: SensorManager){
        sensorManager = sensorMan
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            AccelerometerData.valueX = event.values[0]
            AccelerometerData.valueY = event.values[1]
            AccelerometerData.valueZ = event.values[2]
            AccelerometerData.accuracy = event.accuracy

            Log.d("Accelerometer", "[SENSOR] - X=${AccelerometerData.valueX}, Y=${AccelerometerData.valueY}, Z=${AccelerometerData.valueZ}")
        }

        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            GyroscopeData.valueX = event.values[0]
            GyroscopeData.valueY = event.values[1]
            GyroscopeData.valueZ = event.values[2]
            GyroscopeData.accuracy = event.accuracy

            Log.d("Gyroscope", "[SENSOR] - X=${GyroscopeData.valueX}, Y=${GyroscopeData.valueY}, Z=${GyroscopeData.valueZ}")
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }
}