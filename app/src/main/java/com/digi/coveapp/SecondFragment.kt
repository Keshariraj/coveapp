package com.digi.coveapp

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.digi.coveapp.databinding.FragmentSecondBinding
import com.digi.coveapp.models.Event
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream


class SecondFragment : Fragment() {
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private var photoUri1: Uri? = null
    private var photoUri2: Uri? = null
    private var photoUri3: Uri? = null
    private var photoUri4: Uri? = null
    private var photoUri5: Uri? = null
    private var imageUrl1: String? = null
    private var imageUrl2: String? = null
    private var imageUrl3: String? = null
    private var imageUrl4: String? = null
    val REQUEST_IMAGE_GET = 1

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        db = Firebase.firestore
        auth = Firebase.auth
        storage = Firebase.storage
        return binding.root
    }

    fun selectImage(requestCode: Int) {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(intent, requestCode)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.editimg1.setOnClickListener {
            selectImage(1)
        }
        binding.editimg2.setOnClickListener {
            selectImage(2)
        }
        binding.editimg3.setOnClickListener {
            selectImage(3)
        }
        binding.editimg4.setOnClickListener {
            selectImage(4)
        }
        binding.editImageBanner.setOnClickListener {
            selectImage(5)
        }

        binding.btnSubmit.setOnClickListener {
            uploadDetails()
        }

    }

    fun convertToString(imgUri: Uri): String? {
        val u = Utils()
        return u.encodeImgToString(requireActivity(), imgUri)
    }

    private fun uploadDetails() {
        val user = auth.currentUser
        if (user != null) {
            try {
                val evntName = binding.editEventName.text.toString()
                val loc = binding.editLocation.text.toString()
                val price = binding.editPrice.text.toString()
                val date = binding.editDate.text.toString()
                val summ = binding.editSummary.text.toString()
                val banner = photoUri5?.let { convertToString(it) }
                val img1 = photoUri1?.let { convertToString(it) }
                val img2 = photoUri2?.let { convertToString(it) }
                val img3 = photoUri3?.let { convertToString(it) }
                val img4 = photoUri4?.let { convertToString(it) }
                if (evntName.length > 5 && loc.isNotEmpty() && price.isNotEmpty() && summ.isNotBlank() ) {
                    db.collection("events").document(user.uid).set(
                        Event(
                            eventName = evntName,
                            location = loc,
                            price = price,
                            date = date,
                            summary = summ,
                            banner = banner!!,
                            img1 = img1!!,
                            img2 = img2!!,
                            img3 = img3!!,
                            img4 = img4!!
                        )
                    ).addOnFailureListener {
                        Snackbar.make(
                            binding.root,
                            it.message.toString(),
                            Snackbar.LENGTH_LONG
                        ).show()
                    }.addOnSuccessListener {
                        Snackbar.make(
                            binding.root,
                            "Details Updated!",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
            } catch (e: Exception) {

                Snackbar.make(binding.root, e.message.toString(), Snackbar.LENGTH_LONG).show()
//                Snackbar.make(binding.root, "please select all images and fill all fields",Snackbar.LENGTH_LONG).show()
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                1 -> {
                    photoUri1 = data?.data
                    Glide.with(requireActivity()).load(photoUri1).into(binding.editimg1)
                }
                2 -> {
                    photoUri2 = data?.data
                    Glide.with(requireActivity()).load(photoUri2).into(binding.editimg2)
                }
                3 -> {
                    photoUri3 = data?.data
                    Glide.with(requireActivity()).load(photoUri3).into(binding.editimg3)
                }
                4 -> {
                    photoUri4 = data?.data
                    Glide.with(requireActivity()).load(photoUri4).into(binding.editimg4)
                }
                5 -> {
                    photoUri5 = data?.data
                    Glide.with(requireActivity()).load(photoUri5).into(binding.editImageBanner)
                }
            }
        } else {
            Toast.makeText(context, "Select an Image", Toast.LENGTH_LONG).show()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}