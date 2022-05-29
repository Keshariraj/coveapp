package com.digi.coveapp.ui.dashboard

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.digi.coveapp.adapters.BookedEventAdapter
import com.digi.coveapp.databinding.FragmentDashboardBinding
import com.digi.coveapp.listener.OnBookedEventItemClickListener
import com.digi.coveapp.models.Transaction
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class DashboardFragment : Fragment(), OnBookedEventItemClickListener {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private var db = Firebase.firestore
    private var auth = Firebase.auth
    private lateinit var tlist: ArrayList<Transaction>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tlist = arrayListOf()
        binding.recyclerView2.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = BookedEventAdapter(tlist, this@DashboardFragment)
        }
        loadTransactions()

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadTransactions() {
        tlist.clear()
        db.collection("transactions")
            .whereEqualTo("uid", auth.currentUser!!.uid).get()
            .addOnFailureListener { e ->
                showSnack(e.message.toString())
            }
            .addOnSuccessListener {
                if (it.size() > 0) {
                    for (document in it.documents) {
                        val obj = document.toObject<Transaction>()
                        obj?.let { it1 -> tlist.add(it1) }
                    }
                    try {
                        binding.recyclerView2.adapter?.notifyDataSetChanged()
                    } catch (e: Exception) {
                        showSnack(e.message.toString())
                    }
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

    override fun onEventCLick(view: View, transaction: Transaction) {

    }
}