package com.dicoding.capstone.saiko.view.camera

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.dicoding.capstone.saiko.R
import com.dicoding.capstone.saiko.data.remote.ApiConfig
import com.dicoding.capstone.saiko.data.remote.UploadResponse
import com.dicoding.capstone.saiko.databinding.ActivityAdddaganganBinding
import com.dicoding.capstone.saiko.view.main.MainActivity
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class AddDaganganActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdddaganganBinding
    private var currentImageUri: Uri? = null

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdddaganganBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        binding.galleryButton.setOnClickListener { startGallery() }
        binding.cameraButton.setOnClickListener { startCamera() }
        binding.buttonPosting.setOnClickListener { uploadImage() }
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

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri!!)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.imagePlaceholder.setImageURI(it)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun uploadImage() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()
            val description = binding.editTextDeskripsi.text.toString()

            Log.d("AddDaganganActivity", "Image file path: ${imageFile.path}")
            Log.d("AddDaganganActivity", "Description: $description")

            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "image",
                imageFile.name,
                requestImageFile
            )

            val descriptionBody = RequestBody.create("text/plain".toMediaTypeOrNull(), description)

            Log.d("AddDaganganActivity", "Request Image File: $requestImageFile")
            Log.d("AddDaganganActivity", "Image Multipart: $imageMultipart")
            Log.d("AddDaganganActivity", "Description Body: $descriptionBody")


            val client = ApiConfig.getApiService().uploadImage(imageMultipart, descriptionBody, token = "")
            client.enqueue(object : Callback<UploadResponse> {
                override fun onResponse(
                    call: Call<UploadResponse>,
                    response: Response<UploadResponse>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@AddDaganganActivity, "Upload successful", Toast.LENGTH_SHORT).show()
                        navigateToMainActivity()
                    } else {
                        Log.e("AddDaganganActivity", "Upload failed: ${response.message()}")
                        Log.e("AddDaganganActivity", "Response body: ${response.errorBody()?.string()}")
                        Toast.makeText(this@AddDaganganActivity, "Upload failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                    Log.e("AddDaganganActivity", "Upload failed: ${t.message}", t)
                    Toast.makeText(this@AddDaganganActivity, "Upload failed: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } ?: showToast(getString(R.string.empty_image_warning))
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}
