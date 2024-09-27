import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

class ShakeDetector(context: Context, private val onShake: () -> Unit) : SensorEventListener {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private var accelLast: Float = 0f
    private var accelCurrent: Float = 0f
    private var accelDiff: Float = 0f
    private var isShaking = false

    init {
        val accelerometer: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        accelerometer?.also {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            accelCurrent = event.values[0] + event.values[1] + event.values[2]
            val delta = accelCurrent - accelLast
            accelDiff = accelDiff * 0.9f + delta // decay factor
            accelLast = accelCurrent

            if (accelDiff > 12 && !isShaking) { // adjust threshold as needed
                isShaking = true
                onShake()
            } else if (accelDiff < 6) {
                isShaking = false
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    fun unregister() {
        sensorManager.unregisterListener(this)
    }
}
