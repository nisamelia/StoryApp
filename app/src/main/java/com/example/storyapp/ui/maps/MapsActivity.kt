package com.example.storyapp.ui.maps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.example.storyapp.R
import com.example.storyapp.data.response.ListStoryItem

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.storyapp.databinding.ActivityMapsBinding
import com.example.storyapp.ui.factory.StoryViewModelFactory
import com.example.storyapp.ui.main.MainViewModel
import com.google.android.gms.maps.model.LatLngBounds

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private val mainViewModel by viewModels<MapsViewModel> {
        StoryViewModelFactory.getInstance(this)
    }

    private lateinit var mMap: GoogleMap
    private val boundsBuilder = LatLngBounds.Builder()
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
//    override fun onMapReady(googleMap: GoogleMap) {
//        mMap = googleMap
//
//        // Add a marker in Sydney and move the camera
//        val sydney = LatLng(-34.0, 151.0)
//        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
//        mMap.uiSettings.isZoomControlsEnabled = true
//        mMap.uiSettings.isIndoorLevelPickerEnabled = true
//        mMap.uiSettings.isCompassEnabled = true
//        mMap.uiSettings.isMapToolbarEnabled = true

        override fun onMapReady(googleMap: GoogleMap) {
            mMap = googleMap

            mMap.uiSettings.isZoomControlsEnabled = true
            mMap.uiSettings.isIndoorLevelPickerEnabled = true
            mMap.uiSettings.isCompassEnabled = true
            mMap.uiSettings.isMapToolbarEnabled = true

            // Move the camera to a default location
            val defaultLocation = LatLng(-34.0, 151.0)
            mMap.moveCamera(CameraUpdateFactory.newLatLng(defaultLocation))
            mainViewModel.setMarkers()
            mainViewModel.story.observe(this) { storyResponse ->
                if (!storyResponse.error) {
                    mMap.clear()
                    val data = storyResponse.listStory
                    data.forEach { item ->
                        val latLng = LatLng(item.lat, item.lon)
                        mMap.addMarker(
                            MarkerOptions()
                                .position(latLng)
                                .title(item.name)
                                .snippet(item.description)
                        )
                        boundsBuilder.include(latLng)
                    }
                    val bounds: LatLngBounds = boundsBuilder.build()
                    mMap.animateCamera(
                        CameraUpdateFactory.newLatLngBounds(
                            bounds,
                            resources.displayMetrics.widthPixels,
                            resources.displayMetrics.heightPixels,
                            300
                        )
                    )
                } else {
                    showError(storyResponse.message)
                }
            }
        }
    }

//    private fun updateMapMarkers(data: List<ListStoryItem>) {
//        mMap.clear()  // Clear existing markers
//        data.forEach { data ->
//            val latLng = LatLng(data.lat, data.lon)
//            mMap.addMarker(
//                MarkerOptions()
//                    .position(latLng)
//                    .title(data.name)
//                    .snippet(data.description)
//            )
//        }
//    }

    private fun showError(message: String?) {
        // Show error message to the user
    }
//}