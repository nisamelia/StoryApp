package com.example.storyapp.ui.add

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.storyapp.data.api.ApiConfig
import com.example.storyapp.data.pref.UserPreferences
import com.example.storyapp.data.pref.dataStore
import com.example.storyapp.data.response.AddStoryResponse
import com.example.storyapp.data.utils.getImageUri
import com.example.storyapp.data.utils.reduceFileImage
import com.example.storyapp.data.utils.uriToFile
import com.example.storyapp.databinding.ActivityAddStoryBinding
import com.example.storyapp.ui.main.MainActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException

class AddStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddStoryBinding
    private var currentImageUri: Uri? = null
    private var myLocation: Location? = null

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Kamera Diizinkan", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Kamera Tidak Diizinkan", Toast.LENGTH_LONG).show()
            }
        }


    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        binding.cameraButton.setOnClickListener { startCamera() }
        binding.galleryButton.setOnClickListener { startGallery() }
        binding.buttonAdd.setOnClickListener { startUpload() }

        binding.getLocationButton!!.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                getMyLocation()
            } else {
                myLocation = null
            }
        }
    }

    private val requestLocationLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }

        }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        myLocation = location
                        Toast.makeText(
                            this,
                            "Koordinat : ${location.latitude}, ${location.longitude}",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(this, "Lokasi Tidak Tesedia", Toast.LENGTH_LONG).show()
                    }
                }.addOnFailureListener { exception ->
                    Toast.makeText(
                        this,
                        "Gagal Mendapatkan Lokasi: ${exception.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
        } else {
            requestLocationLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            requestLocationLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
    }


    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }

private fun startUpload() {
    currentImageUri?.let { uri ->
        val imageFile = uriToFile(uri, this)
        val isReduced = imageFile.reduceFileImage()

        if (!isReduced) {
            showToast("Gambar Terlalu Besar Untuk Diunggah")
            return
        }

        Log.d("Image File", "showImage: ${imageFile.path}")
        val description = binding.edAddDescription.text.toString()
        showLoading(true)

        val requestBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            imageFile.name,
            requestImageFile
        )

        lifecycleScope.launch {
            try {
                val userPreferences = UserPreferences.getInstance(dataStore)
                val token = userPreferences.getSession().first().token
                val lat = myLocation?.latitude
                val long = myLocation?.longitude

                if (token.isNotEmpty()) {
                    val apiService = ApiConfig.getApiService(token)
                    val successResponse =
                        apiService.uploadImage(multipartBody, requestBody, lat, long)
                    showToast(successResponse.message)
                    showLoading(false)
                    moveMain()
                } else {
                    showToast("Autentikasi Gagal")
                }
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, AddStoryResponse::class.java)
                showToast(errorResponse.message)
                showLoading(false)
            }
        }
    } ?: showToast("Isi dulu datanya ya!")
}

    private fun moveMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}