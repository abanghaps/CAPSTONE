package com.dicoding.capstone.saiko.view.main.fragment

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.dicoding.capstone.saiko.R
import com.dicoding.capstone.saiko.view.welcome.WelcomeActivity
import com.google.firebase.auth.FirebaseAuth
import de.hdodenhof.circleimageview.CircleImageView
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Suppress("DEPRECATION")
class AccountFragment : Fragment() {

    private lateinit var profileNameTextView: TextView
    private lateinit var profilePhoneTextView: TextView
    private lateinit var profileImageView: CircleImageView
    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPref: SharedPreferences
    private lateinit var currentPhotoPath: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_account, container, false)

        profileNameTextView = view.findViewById(R.id.tv_profile_name)
        profilePhoneTextView = view.findViewById(R.id.tv_profile_location)
        profileImageView = view.findViewById(R.id.profile_image)

        auth = FirebaseAuth.getInstance()
        sharedPref = requireActivity().getSharedPreferences("USER_PREF", Context.MODE_PRIVATE)

        loadUserProfile()

        profileImageView.setOnClickListener {
            showImagePickerDialog()
        }

        view.findViewById<LinearLayout>(R.id.layout_account_settings).setOnClickListener {
            showToast("Pengaturan Akun")
        }
        view.findViewById<LinearLayout>(R.id.layout_rating_history).setOnClickListener {
            showToast("Riwayat Penilaian Saya")
        }
        view.findViewById<LinearLayout>(R.id.layout_help_center).setOnClickListener {
            showToast("Pusat Bantuan")
        }
        view.findViewById<LinearLayout>(R.id.layout_logout).setOnClickListener {
            showToast("Keluar")
            logoutUser()
        }

        return view
    }

    private fun loadUserProfile() {
        val userName = sharedPref.getString("USER_NAME", "Nama Pengguna")
        val userPhone = sharedPref.getString("USER_PHONE", "Nomor Telepon")
        val profileImageUri = sharedPref.getString("USER_PHOTO", "")

        profileNameTextView.text = userName
        profilePhoneTextView.text = userPhone
        if (!profileImageUri.isNullOrEmpty()) {
            Glide.with(this)
                .load(profileImageUri)
                .into(profileImageView)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showImagePickerDialog() {
        val options = arrayOf("Kamera", "Galeri")
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Pilih Gambar Dari")
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> openCamera()
                1 -> openGallery()
            }
        }
        builder.show()
    }

    private fun openCamera() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
        } else {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val photoFile: File = createImageFile()
            val photoURI: Uri = FileProvider.getUriForFile(
                requireContext(),
                "com.dicoding.capstone.saiko.fileprovider",
                photoFile
            )
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            startActivityForResult(intent, REQUEST_CAMERA)
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_GALLERY)
    }

    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

    @Deprecated("Deprecated in Java")
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CAMERA -> {
                    val file = File(currentPhotoPath)
                    if (file.exists()) {
                        profileImageView.setImageURI(Uri.fromFile(file))
                        saveProfileImage(Uri.fromFile(file))
                    }
                }
                REQUEST_GALLERY -> {
                    val selectedImageUri = data?.data
                    if (selectedImageUri != null) {
                        profileImageView.setImageURI(selectedImageUri)
                        saveProfileImage(selectedImageUri)
                    }
                }
            }
        }
    }

    private fun saveProfileImage(uri: Uri) {
        sharedPref.edit().putString("USER_PHOTO", uri.toString()).apply()
    }

    private fun logoutUser() {
        auth.signOut()
        val intent = Intent(activity, WelcomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }

    companion object {
        private const val REQUEST_CAMERA_PERMISSION = 100
        private const val REQUEST_CAMERA = 101
        private const val REQUEST_GALLERY = 102
    }
}
