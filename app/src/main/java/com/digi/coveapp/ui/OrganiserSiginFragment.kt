package com.digi.coveapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.digi.coveapp.OrganizerActivity
import com.digi.coveapp.R
import com.digi.coveapp.databinding.FragmentOrganiserSiginBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class OrganiserSiginFragment : Fragment() {


    private var _binding: FragmentOrganiserSiginBinding? = null
    private val binding get() = _binding!!

    //    firebase code
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        firebase
        db = Firebase.firestore
        auth = Firebase.auth

        _binding = FragmentOrganiserSiginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.nextButton.setOnClickListener {
            val email = binding.editEmail.text.toString().trim()
            val password = binding.editPassword.text.toString().trim()
            try {
                auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
                    updateUI(it.user)
                }.addOnFailureListener {
                    Snackbar.make(binding.root, it.message.toString(), Snackbar.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Snackbar.make(binding.root, e.message.toString(), Snackbar.LENGTH_LONG).show()

            }
        }
        binding.signupTextview.setOnClickListener {
            findNavController().navigate(R.id.action_organiserSiginFragment_to_organiserRegisterFragment2)
        }

    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            val i = Intent(requireContext(), OrganizerActivity::class.java)
            startActivity(i)
            requireActivity().finish()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        updateUI(auth.currentUser)
    }


}