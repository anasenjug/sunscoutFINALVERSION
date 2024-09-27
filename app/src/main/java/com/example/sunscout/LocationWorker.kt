import android.Manifest
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.sunscout.R
import com.example.sunscout.network.RetrofitClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.HttpException

class LocationWorker(private val context: Context) {

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    suspend fun fetchUVIndexManual(): Double? {
        val location = getLastLocation()
        return if (location != null) {
            val latitude = location.latitude
            val longitude = location.longitude
            fetchUVIndexFromAPI(latitude, longitude)
        } else {
            null
        }
    }

    private suspend fun getLastLocation(): android.location.Location? {
        return try {
            if (ActivityCompat.checkSelfPermission(
                    context, Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    context, Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                null
            } else {
                fusedLocationClient.lastLocation.await()
            }
        } catch (e: Exception) {
            null
        }
    }

   private suspend fun fetchUVIndexFromAPI(lat: Double, lon: Double): Double? {
       return withContext(Dispatchers.IO) {
           try {
               val apiKey = "" // Replace with your WeatherAPI Key
               val location = "$lat,$lon" // Format as "latitude,longitude"
               val response = RetrofitClient.instance.getCurrentWeather(apiKey, location)
               response.current.uv
           } catch (e: Exception) {
               Log.e("LocationWorker", "Error fetching UV Index", e)
               null
           }
       }
   }

}