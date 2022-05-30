package com.digi.coveapp.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.digi.coveapp.adapters.EventViewerAdapter
import com.digi.coveapp.databinding.FragmentHomeBinding
import com.digi.coveapp.listener.OnEventItemClickListener
import com.digi.coveapp.models.Event
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment(), OnEventItemClickListener {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var db = Firebase.firestore
    private var auth = Firebase.auth
    private var eventList = arrayListOf<Event>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.eventList.apply {
            layoutManager =
                LinearLayoutManager(requireContext())

            adapter = EventViewerAdapter(
                eventList,
                this@HomeFragment,
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
                    binding.eventList.adapter?.notifyDataSetChanged()
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

    override fun onEventCLick(view: View, event: Event) {
        findNavController().navigate(HomeFragmentDirections.actionNavigationHomeToEventDetalis(event.eventName))
    }
}
