package com.dicoding.capstone.saiko.view.main.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.dicoding.capstone.saiko.R
import de.hdodenhof.circleimageview.CircleImageView

class AccountFragment : Fragment() {

    private lateinit var profileNameTextView: TextView
    private lateinit var profilePhoneTextView: TextView
    private lateinit var profileImageView: CircleImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_account, container, false)

        profileNameTextView = view.findViewById(R.id.tv_profile_name)
        profilePhoneTextView = view.findViewById(R.id.tv_profile_location)
        profileImageView = view.findViewById(R.id.profile_image)

        loadUserProfile()

        // Set up click listeners for each option
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
        }

        return view
    }

    private fun loadUserProfile() {
        val sharedPref: SharedPreferences = requireActivity().getSharedPreferences("USER_PREF", Context.MODE_PRIVATE)
        val userName = sharedPref.getString("USER_NAME", "Nama Pengguna")
        val userPhone = sharedPref.getString("USER_PHONE", "Nomor Telepon")

        profileNameTextView.text = userName
        profilePhoneTextView.text = userPhone
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
