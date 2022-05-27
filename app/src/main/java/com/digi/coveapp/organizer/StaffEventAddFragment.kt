package com.digi.coveapp.organizer

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.digi.coveapp.R
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


class StaffEventAddFragment : Fragment() {
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage

    private var photoUri1: Uri? = null
    private var photoUri2: Uri? = null
    private var photoUri3: Uri? = null
    private var photoUri4: Uri? = null
    private var photoUri5: Uri? = null

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!
    private var uploadCheckList = arrayListOf<Boolean>()
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
        uploadCheckList = arrayListOf(false, false, false, false, false)
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


    private fun uploadDetails() {
        val user = auth.currentUser
        if (user != null) {
            try {
                val evntName = binding.editEventName.text.toString()
                val loc = binding.editLocation.text.toString()
                val price = binding.editPrice.text.toString()
                val date = binding.editDate.text.toString()
                val summ = binding.editSummary.text.toString()
                val banner = photoUri5?.let { sendToFirebaseStorage(it, binding.editImageBanner) }
                photoUri1?.let { sendToFirebaseStorage(it, binding.editimg1) }
                photoUri2?.let { sendToFirebaseStorage(it, binding.editimg2) }
                photoUri3?.let { sendToFirebaseStorage(it, binding.editimg3) }
                photoUri4?.let { sendToFirebaseStorage(it, binding.editimg4) }
                if (evntName.length > 5 && loc.isNotEmpty() && price.isNotEmpty() && summ.isNotBlank() && allTrue(
                        uploadCheckList
                    )
                ) {
                    binding.btnSubmit.isEnabled = false
                    val bannerUrl = binding.editImageBanner.tag as String
                    db.collection("events").document(user.uid).set(
                        Event(
                            eventName = evntName,
                            location = loc,
                            price = price,
                            date = date,
                            summary = summ,
                            banner = bannerUrl,
                            img1 = binding.editimg1.tag as String,
                            img2 = binding.editimg2.tag as String,
                            img3 = binding.editimg3.tag as String,
                            img4 = binding.editimg4.tag as String,
                        )
                    ).addOnFailureListener {
                        Snackbar.make(
                            binding.root,
                            it.message.toString(),
                            Snackbar.LENGTH_LONG
                        ).show()
                        Log.d("Failed", it.message.toString())
                        binding.btnSubmit.isEnabled = true
                    }.addOnSuccessListener {
                        Snackbar.make(
                            binding.root,
                            "Details Updated!",
                            Snackbar.LENGTH_LONG
                        ).show()

                        binding.btnSubmit.isEnabled = true
                    }
                }
                else{
                    Snackbar.make(binding.root,"Please wait", Snackbar.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Log.d("ERROR", e.message.toString())
                Snackbar.make(binding.root, e.message.toString(), Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun sendToFirebaseStorage(imgUri: Uri, img: ImageView) {
        val eventFolderRef = storage.reference.child("events").child(imgUri.lastPathSegment.toString())
        eventFolderRef.putFile(imgUri).addOnProgressListener { task ->
            val progress = task.bytesTransferred / task.totalByteCount * 100
            Log.e("Uploading", "=>${progress} %")
        }.addOnCompleteListener { it ->
            if (it.isSuccessful) {
                eventFolderRef.downloadUrl.addOnSuccessListener { urlUri ->
                    img.tag = urlUri.toString()
                    when (img.id) {
                        R.id.editImageBanner -> uploadCheckList[0] = true
                        R.id.editimg1 -> uploadCheckList[1] = true
                        R.id.editimg2 -> uploadCheckList[2] = true
                        R.id.editimg3 -> uploadCheckList[3] = true
                        R.id.editimg4 -> uploadCheckList[4] = true
                    }
                }
            } else {
                Snackbar.make(binding.root, it.exception?.message.toString(), Snackbar.LENGTH_LONG)
                    .show()
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

    private fun allTrue(values: ArrayList<Boolean>): Boolean {
        for (value in values) {
            if (!value) return false
        }
        return true
    }

}