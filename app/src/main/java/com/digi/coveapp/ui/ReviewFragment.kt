package com.digi.coveapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.digi.coveapp.databinding.FragmentReviewBinding
import com.digi.coveapp.models.Review
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ReviewFragment : Fragment() {

    private var _binding: FragmentReviewBinding? = null
    private val binding get() = _binding!!
    private var db = Firebase.firestore
    private var auth = Firebase.auth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val eventId = ReviewFragmentArgs.fromBundle(requireArguments()).eventId
        val name = ReviewFragmentArgs.fromBundle(requireArguments()).name
        updateUI(eventId, name)
    }

    private fun updateUI(eventId: String, name: String) {
        binding.textEventTitle.text = name
        binding.btnSendReview.setOnClickListener {
            val review = binding.editReview.text.toString().trim()
            val rating: Int = binding.eventRating.rating.toInt()
            val reviewItem = Review(
                eventId = eventId,
                rating = rating,
                review = review,
                uid = auth.currentUser?.uid!!
            )
            db.collection("reviews")
                .add(reviewItem)
                .addOnSuccessListener {
                    findNavController().navigate(
                        ReviewFragmentDirections.actionReviewFragmentToEventDetalis(
                            name
                        )
                    )
                }
                .addOnFailureListener {
                    showSnack(it.message.toString())
                }
        }
    }

    private fun showSnack(s: String) {
        Snackbar.make(binding.root, s, Snackbar.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}