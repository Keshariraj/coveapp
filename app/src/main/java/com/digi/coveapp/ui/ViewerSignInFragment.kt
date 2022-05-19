package com.digi.coveapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.digi.coveapp.MainActivity
import com.digi.coveapp.databinding.FragmentViewerSiginBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit

class ViewerSignInFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var storedVerificationId: String
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken

    private var _binding: FragmentViewerSiginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentViewerSiginBinding.inflate(inflater, container, false)
        auth = Firebase.auth
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.generateOtpButton.setOnClickListener {
            val phone = binding.phoneNumberTextField.editText?.text.toString()
            if (phone.length == 10) {
                startPhoneNumberVerification("+91$phone")
            } else {
                Snackbar.make(
                    binding.root,
                    "Please enter correct phone number.",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun startPhoneNumberVerification(phoneNumber: String) {
        auth.useAppLanguage()

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(requireActivity())                 // Activity (for callback binding)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    updateUI(auth.currentUser)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    when (e) {
                        is FirebaseAuthInvalidCredentialsException -> Snackbar.make(
                            binding.root,
                            "Firebase auth is invalid.",
                            Snackbar.LENGTH_SHORT
                        ).show()
                        is FirebaseTooManyRequestsException -> Snackbar.make(
                            binding.root,
                            "Too many requests, try later.",
                            Snackbar.LENGTH_SHORT
                        ).show()
                        else -> Snackbar.make(
                            binding.root,
                            "Failed to send the OTP ${e.message.toString()}",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    storedVerificationId = verificationId
                    resendToken = token
                    val direction =
                        ViewerSignInFragmentDirections.actionViewerSiginFragmentToOtpVerificationFragment(
                            verificationId
                        )
                    findNavController().navigate(direction)
                }
            })
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    override fun onStart() {
        super.onStart()
        updateUI(auth.currentUser)
    }

    fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            val intent = Intent(requireActivity(), MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


