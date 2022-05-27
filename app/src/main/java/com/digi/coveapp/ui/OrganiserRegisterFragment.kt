package com.digi.coveapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.digi.coveapp.OrganizerActivity
import com.digi.coveapp.databinding.FragmentOrganiserRegisterBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.lang.Exception

class OrganiserRegisterFragment : Fragment() {
    private var _binding: FragmentOrganiserRegisterBinding? = null
    private val binding get() = _binding!!

    //    firebase code
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        db = Firebase.firestore
        auth = Firebase.auth

        _binding = FragmentOrganiserRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.nextButton.setOnClickListener {
            val email = binding.editEmail.text.toString().trim()
            val password = binding.editPass.text.toString().trim()
            val password2 = binding.editCPass.text.toString().trim()
            if(password != password2){
                binding.passwordTextField.error = "Password do not match"
                return@setOnClickListener
            }
            try {
                auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
                    val i = Intent(requireContext(), OrganizerActivity::class.java)
                    startActivity(i)
                    requireActivity().finish()
                }.addOnFailureListener {
                    Snackbar.make(binding.root, it.message.toString(), Snackbar.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Snackbar.make(binding.root, e.message.toString(), Snackbar.LENGTH_LONG).show()
            }
        }

    }

}