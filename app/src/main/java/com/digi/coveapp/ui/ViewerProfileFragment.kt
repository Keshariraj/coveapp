package com.digi.coveapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.digi.coveapp.MainActivity
import com.digi.coveapp.R
import com.digi.coveapp.databinding.FragmentViewerProfileBinding

class ViewerProfileFragment : Fragment() {

    private var _binding: FragmentViewerProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentViewerProfileBinding.inflate(inflater, container, false)
        binding.saveButton.setOnClickListener {
            startActivity(Intent(requireContext(), MainActivity::class.java))
            requireActivity().finish()
        }
        return binding.root
    }
}