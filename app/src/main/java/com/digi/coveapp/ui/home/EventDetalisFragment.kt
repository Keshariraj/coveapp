package com.digi.coveapp.ui.home

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ShareCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.digi.coveapp.databinding.FragmentEventDetalisBinding
import com.digi.coveapp.models.Event
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem


class EventDetalisFragment : Fragment() {

    private var _binding: FragmentEventDetalisBinding? = null
    private val binding get() = _binding!!
    private var db = Firebase.firestore
    private var auth = Firebase.auth
    private lateinit var eventId: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventDetalisBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val name = EventDetalisFragmentArgs.fromBundle(requireArguments()).name
        db.collection("events").whereEqualTo("eventName", name).get()
            .addOnSuccessListener {
                if (it.size() > 0) {
                    val event = it.documents[0].toObject<com.digi.coveapp.models.Event>()
                    eventId = it.documents[0].id
                    if (event != null) {
                        updateEventUI(event)
                    } else {
                        showSnack("Could not load Event Data")
                    }
                } else {
                    showSnack("No events with Name found")
                }
            }
            .addOnFailureListener {
                showSnack(it.message.toString())
            }
    }

    private fun updateEventUI(event: Event) {
        Glide.with(binding.eventImage).load(event.banner).into(binding.eventImage)
        binding.eventTitle.text = event.eventName
        binding.btnAbout.setOnClickListener {
            showAlert("About the Event", event.summary)
        }
        binding.btnDateTime.text = event.date
        binding.btnBuy.text = "Register For ₹${event.price}"
        binding.btnBuy.setOnClickListener {
            findNavController().navigate(
                EventDetalisFragmentDirections.actionEventDetalisToBuyTicketFragment(
                    eventId,
                    event.eventName,
                    event.price
                )
            )
        }
        binding.btnShare.setOnClickListener {
            // create a share intent
            ShareCompat.IntentBuilder.from(requireActivity())
                .setType("text/plain")
                .setText("Book events and conferences using the new Cove App.")
                .startChooser()
        }
        binding.btnReview.setOnClickListener {
            findNavController().navigate(
                EventDetalisFragmentDirections.actionEventDetalisToReviewFragment(
                    event.eventName,
                    eventId
                )
            )
        }
        binding.carousel.registerLifecycle(lifecycle)
        val list = mutableListOf<CarouselItem>()
        list.add(
            CarouselItem(
                imageUrl = event.img1,
            )
        )
        list.add(
            CarouselItem(
                imageUrl = event.img2,
            )
        )
// Image URL with header
        val headers = mutableMapOf<String, String>()
        headers["header_key"] = "header_value"

        list.add(
            CarouselItem(
                imageUrl = event.img3,
                headers = headers
            )
        )
        list.add(
            CarouselItem(
                imageUrl = event.img4,
                headers = headers
            )
        )
        binding.carousel.setData(list)
    }

    private fun showSnack(s: String) {
        Snackbar.make(binding.root, s, Snackbar.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showAlert(title: String, msg: String) {
        val alertBuilder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        alertBuilder.setTitle(title)
        alertBuilder.setMessage(msg)
        alertBuilder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        alertBuilder.show()


    }
}