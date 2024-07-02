package com.digi.coveapp.organizer

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.digi.coveapp.R
import com.digi.coveapp.adapters.EventStaffAdapter
import com.digi.coveapp.databinding.FragmentFirstBinding
import com.digi.coveapp.listener.OnEventItemClickListener
import com.digi.coveapp.models.Event
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase


class OrganizerEventListFragment : androidx.fragment.app.Fragment(), OnEventItemClickListener {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!
    private var db = Firebase.firestore
    private var auth = Firebase.auth
    private var eventList = arrayListOf<Event>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.editbtn.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
        binding.recyclerView.apply {
            layoutManager =
                LinearLayoutManager(requireContext())

            adapter = EventStaffAdapter(
                eventList,
                this@OrganizerEventListFragment,
            )
        }
        loadEvents()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadEvents() {
        eventList.clear()
        db.collection("events").get()
            .addOnSuccessListener { querySnapShot ->
                if (querySnapShot.size() > 0) {
                    for (document in querySnapShot.documents) {
                        eventList.add(document.toObject<Event>()!!)
                    }
                    binding.recyclerView.adapter?.notifyDataSetChanged()
                } else {
                    showSnack("No event in Cloud")
                }

            }
            .addOnFailureListener {
                showSnack(it.message.toString())
            }
    }

    private fun showSnack(s: String) {
        Snackbar.make(binding.root, s, Snackbar.LENGTH_LONG).show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onEventClick(view: View, event: Event) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Event")
            .setMessage("Are you sure you want to delete this event?")
            .setPositiveButton("Yes") { dialog, which ->
                db.collection("events").whereEqualTo("eventName", event.eventName).get()
                    .addOnSuccessListener {
                        if (it.size() > 0) {
                            val eventId = it.documents[0].id
                            db.collection("events").document(eventId).delete()
                                .addOnSuccessListener {
                                    showSnack("Event Deleted")
                                    loadEvents()
                                }
                                .addOnFailureListener { e ->
                                    showSnack(e.message.toString())
                                }
                        } else {
                            showSnack("no items found")
                        }
                    }
                    .addOnFailureListener { e ->
                        showSnack(e.message.toString())
                    }
            }
            .setNegativeButton("No") { dialog, which ->
                showSnack("Event not Deleted")
            }
            .show()
    }


}
