package com.digi.coveapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.digi.coveapp.R
import com.digi.coveapp.databinding.FragmentChoiceBinding
import com.digi.coveapp.databinding.FragmentSplashBinding

class ChoiceFragment : Fragment() {
    private var _binding: FragmentChoiceBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentChoiceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.orgButton.setOnClickListener { findNavController().navigate(R.id.action_choiceFragment_to_organiserSiginFragment) }
        binding.userButton.setOnClickListener { findNavController().navigate(R.id.action_choiceFragment_to_viewerSiginFragment) }

    }
}