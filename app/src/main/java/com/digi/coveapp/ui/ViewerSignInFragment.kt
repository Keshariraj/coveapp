package com.digi.coveapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.digi.coveapp.databinding.FragmentDashboardBinding
import com.digi.coveapp.databinding.FragmentViewerSiginBinding
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit


class ViewerSignInFragment(phoneNumber: String) : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var signInRequest: BeginSignInRequest

    private var _binding: FragmentViewerSiginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentViewerSiginBinding.inflate(inflater, container, false)
        auth = Firebase.auth
        db = Firebase.firestore
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
    val options = PhoneAuthOptions.newBuilder(auth)
        .setPhoneNumber(phoneNumber)       // Phone number to verify
        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
        .setActivity(requireActivity())                 // Activity (for callback binding)
        .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
        .build()
    override fun onStart() {
        super.onStart()
        updateUI(auth.currentUser)
    }

    fun updateUI(user: FirebaseUser?) {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}