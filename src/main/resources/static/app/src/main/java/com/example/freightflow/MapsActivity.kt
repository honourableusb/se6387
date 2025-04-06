package com.example.freightflow

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.freightflow.network.RouteRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.freightflow.network.RouteApiService
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.PolyUtil
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.View
import android.widget.Button
import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.CircleOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mapView: MapView
    private var mMap: GoogleMap? = null
    private val originLatLng = LatLng(32.92999719901092, -97.04590528910143)  // Example: DFW Airport (hardcoded location)
    private val LOCATION_PERMISSION_REQUEST_CODE = 1

    private lateinit var startNavigationButton: Button
    private var truckMarker: Marker? = null
    private var polyline: Polyline? = null
    private var polylinePoints: List<LatLng>? = null
    private var currentIndex = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        // Initialize the MapView
        mapView = findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        Log.d("MapActivity", "Reached onCreate")
        // Send route request to backend with origin and destination
        val destination = intent.getStringExtra("destination")
        startNavigationButton = findViewById(R.id.startNavigation)
        startNavigationButton.setOnClickListener {
            Log.d("MapActivity","Button Clicked")
            startNavigation()
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap
        mMap?.mapType = GoogleMap.MAP_TYPE_NORMAL
        Handler().postDelayed({
        val destination = intent.getStringExtra("destination")
        val destinationLatLng = getTerminalCoordinates(destination)
        if (destinationLatLng != null) {
            mMap?.addMarker(MarkerOptions().position(originLatLng).title("Origin"))
            mMap?.addMarker(MarkerOptions().position(destinationLatLng).title("Destination"))
            mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(originLatLng, 12f))

            sendToBackend(originLatLng, destination)
        } else {
            Toast.makeText(this, "Invalid destination terminal", Toast.LENGTH_SHORT).show()
        }
        }, 100)
    }

    private fun getTerminalCoordinates(terminal: String?): LatLng? {
        val terminals = mapOf(
            "Terminal A" to LatLng(32.90519931784437, -97.03620410500054),
            "Terminal B" to LatLng(32.90525030865262, -97.04492894670216),
            "Terminal C" to LatLng(32.89779743017013, -97.03563013684561),
            "Terminal D" to LatLng(32.89803930764378, -97.04472216437125),
            "Terminal E" to LatLng(32.8906161218135, -97.03586014583706)
        )
        return terminals[terminal]  // Return the corresponding LatLng or null if not found
    }

    private fun sendToBackend(originLatLng: LatLng, destination: String?) {
        if (destination != null) {
            val destinationLatLng = getTerminalCoordinates(destination)

            if (destinationLatLng != null) {
                val routeRequest = RouteRequest(
                    originLatitude = originLatLng.latitude,
                    originLongitude = originLatLng.longitude,
                    destinationLatitude = destinationLatLng.latitude,
                    destinationLongitude = destinationLatLng.longitude
                )

                // Send request to backend
                val service = RetrofitClient.retrofitInstance.create(RouteApiService::class.java)
                val call = service.createRoute(routeRequest)

                // Retrofit callback expecting a String response
                call.enqueue(object : Callback<ResponseBody> {

                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        Log.d("MapsActivity", "Got Response")

                        if (response.isSuccessful) {
                            val responseBody = response.body()?.string()
                            Log.d("MapsActivity", "Response Body: $responseBody")

                            if (responseBody != null) {
                                try {
                                    // Clear previous polylines to avoid overlap
                                    mMap?.clear() // This clears the entire map: markers, polylines, etc.

                                    // Parsing the response (Assuming it's JSON)
                                    val jsonObject = JSONObject(responseBody)
                                    val routes = jsonObject.getJSONArray("routes")
                                    if (routes.length() > 0) {
                                        val route = routes.getJSONObject(0)
                                        val legs = route.getJSONArray("legs")
                                        val steps = legs.getJSONObject(0).getJSONArray("steps")
                                        val decodedPolylineList = mutableListOf<LatLng>()

                                        // Loop through the steps and add each polyline with explicit color
                                        for (i in 0 until steps.length()) {
                                            val step = steps.getJSONObject(i)
                                            val polyline = step.getJSONObject("polyline").getString("points")
                                            val decodedPolyline = decodePolyline(polyline)

                                            polylinePoints = decodedPolyline
                                            Log.d("MapsActivity", "OnResponse polylinePoints:$polylinePoints")

                                            // Add polyline to map (for each step) with an explicit color
                                            val polylineOptions = PolylineOptions()
                                                .addAll(decodedPolyline)
                                                .color(0xFF0000FF.toInt())  // Explicitly setting color to Blue (0xFF0000FF)
                                                .width(8f) // Set width for better visibility
                                            mMap?.addPolyline(polylineOptions)  // Add polyline to the map

                                            Log.d("MapsActivity", "Polyline for step $i: $polyline")
                                            decodedPolylineList.addAll(decodedPolyline)  // Add all points to the list

                                        }

                                        // Optionally, adding the overview polyline (blue color)
                                        polylinePoints = decodedPolylineList // Assign to polylinePoints

                                        val overviewPolyline = route.getJSONObject("overview_polyline").getString("points")
                                        val decodedOverviewPolyline = decodePolyline(overviewPolyline)

                                        // Add the overview polyline to the map (blue color)
                                        val overviewPolylineOptions = PolylineOptions()
                                            .addAll(decodedOverviewPolyline)
                                            .color(0xFF0000FF.toInt())  // Blue color for the overview route
                                            .width(8f)  // Set width for better visibility
                                        mMap?.addMarker(MarkerOptions().position(originLatLng).title("Origin"))
                                        mMap?.addMarker(MarkerOptions().position(destinationLatLng))
                                        mMap?.addPolyline(overviewPolylineOptions)  // Add overview polyline to the map
                                        mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(originLatLng, 12f))
                                        Log.d("MapsActivity", "Added Overview Polyline to map")
                                    }
                                } catch (e: JSONException) {
                                    Log.e("MapsActivity", "Error parsing response: ${e.message}")
                                }
                            }
                        } else {
                            Log.e("MapsActivity", "Error Response: ${response.errorBody()?.string()}")
                        }
                    }
                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Log.e("MapsActivity", "Error: ${t.message}")
                    }
                })
            }
        }
    }

    private fun decodePolyline(encodedPolyline: String): List<LatLng> {
        return PolyUtil.decode(encodedPolyline)  // This will decode the polyline into a list of LatLng points
    }

    private fun startNavigation() {
        Log.d("MapsActivity", "Starting Navigation")
        startNavigationButton.visibility = View.GONE

        if (polylinePoints != null && polylinePoints!!.isNotEmpty()) {
            Log.d("MapsActivity", "Polyline Points are ready for movement")
            animateTruckAlongRoute()
        } else {
            Log.e("MapsActivity", "Polyline points are not available")
        }
    }
    private var truckCircle: Circle? = null
    private fun animateTruckAlongRoute() {
        if (polylinePoints != null && polylinePoints!!.isNotEmpty()) {
            // Create a marker for the truck at the origin
//            truckMarker = mMap?.addMarker(MarkerOptions().position(polylinePoints!![0]).title("Truck"))
            truckCircle = mMap?.addCircle(
                CircleOptions()
                .center(polylinePoints!![0]) // Position at the start
                .radius(10.0) // Radius of the circle (in meters)
                .strokeColor(Color.BLUE) // Blue border color
                .fillColor(Color.BLUE) // Blue fill color
                .strokeWidth(2f) // Border width
            )
            // Use Handler with the main looper
            val handler = Handler(Looper.getMainLooper())
            var currentIndex = 0  // Start at the first point
//            val initialZoomLevel = 15f
//            mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(polylinePoints!![0], initialZoomLevel))
            val runnable = object : Runnable {
                override fun run() {
                    if (currentIndex < polylinePoints!!.size) {
                        val nextPoint = polylinePoints!![currentIndex]
                        truckCircle?.center = nextPoint // Update the truck's position
                        mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(nextPoint, 17f)) // Camera follows truck
                        currentIndex++

                        // Log current position to debug
                        Log.d("MapsActivity", "Truck at: $nextPoint")
                        if (currentIndex == polylinePoints!!.size) {
                            Log.d("MapsActivity", "Truck has reached the destination")

                            // Show a toast message that the destination is reached
                            showToastAndRedirect()
                            // Set an onClickListener for the toast

                        }
                        // Repeat the animation every 500ms
                        handler.postDelayed(this, 200)
                    } else {
                        Log.d("MapsActivity", "Navigation complete.")
                    }
                }
            }

            // Start the animation
            handler.post(runnable)
        } else {
            Log.e("MapsActivity", "Polyline Points are empty")
        }
    }
    private fun showToastAndRedirect() {
        // Create a custom Toast using the layout inflater
        val toast = Toast(applicationContext)

        // Inflate the custom toast layout
        val toastLayout = layoutInflater.inflate(R.layout.custom_toast, null)

        // Set the view for the toast
        toast.view = toastLayout

        // Set gravity to center the Toast
        toast.setGravity(Gravity.CENTER, 0, 0) // Center the toast on the screen

        // Show the toast
        toast.show()

        // Redirect to AirportActivity after 3 seconds
        Handler(Looper.getMainLooper()).postDelayed({
            if (!isFinishing) {
                val intent = Intent(this, AirportActivity::class.java)
                startActivity(intent)
                finish()  // Finish the current activity so the user cannot go back
            }
        }, 3000)  // 3 seconds delay
    }
    // Lifecycle methods for map view
    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}