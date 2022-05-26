package com.digi.coveapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.digi.coveapp.R
import com.digi.coveapp.databinding.FragmentEventDetalisBinding
import com.digi.coveapp.databinding.FragmentHomeBinding


class EventDetalis : Fragment() {

    private var _binding: FragmentEventDetalisBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEventDetalisBinding.inflate(inflater, container, false)



        return binding.root
    }

}