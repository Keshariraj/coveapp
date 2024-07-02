package com.digi.coveapp.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.digi.coveapp.R
import com.digi.coveapp.databinding.FragmentBuyTicketBinding
import com.digi.coveapp.models.Transaction
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class BuyTicketFragment : Fragment() {
    private var _binding: FragmentBuyTicketBinding? = null
    private val binding get() = _binding!!
    private var db = Firebase.firestore
    private var auth = Firebase.auth
    private lateinit var eventId: String
    private lateinit var price: String
    private lateinit var name: String
    private var GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBuyTicketBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        eventId = BuyTicketFragmentArgs.fromBundle(requireArguments()).eventId
        name = BuyTicketFragmentArgs.fromBundle(requireArguments()).name
        price = BuyTicketFragmentArgs.fromBundle(requireArguments()).price
        updateUI()
    }

    private fun updateUI() {
        binding.btnAmount.text = price
        binding.textEventName.text = name
        binding.btnOpenUpi.setOnClickListener {
            launchGooglePay(price)
        }
    }

    private val result =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                db.collection("transactions").add(
                    Transaction(
                        uid = auth.currentUser!!.uid,
                        eventId = eventId,
                        eventName = name,
                        amount = price
                    )
                ).addOnSuccessListener {
                    findNavController().navigate(R.id.action_buyTicketFragment_to_navigation_dashboard)
                }.addOnFailureListener { e ->
                    showSnack(e.message.toString())
                }
            } else {
                showSnack("Cancelled Payment")
            }
        }

    private fun launchGooglePay(price: String) {
        val uri: Uri = Uri.Builder()
            .scheme("upi")
            .authority("pay")
            .appendQueryParameter("pa", "keshariraj01@okhdfcbank")
            .appendQueryParameter("pn", "Cove App")
            .appendQueryParameter("mc", "your-merchant-code")
            .appendQueryParameter("tr", "your-transaction-ref-id")
            .appendQueryParameter("tn", "Booking Registration Amount")
            .appendQueryParameter("am", price)
            .appendQueryParameter("cu", "INR")
            .build()
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = uri
        intent.setPackage(GOOGLE_PAY_PACKAGE_NAME)
        result.launch(intent)
    }

    private fun showSnack(s: String) {
        Snackbar.make(binding.root, s, Snackbar.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}