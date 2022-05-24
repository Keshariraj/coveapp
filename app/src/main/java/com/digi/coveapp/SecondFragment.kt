package com.digi.coveapp

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.digi.coveapp.databinding.FragmentSecondBinding


class SecondFragment : Fragment() {
    private var imgUrl:String ?= null
    val REQUEST_IMAGE_GET = 1

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    fun selectImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_GET)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.editimg1.setOnClickListener {
            val dialog = SelectorDialogFragment()
            dialog.show(childFragmentManager,"choose")
            selectImage()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_GET) {
            if(resultCode == Activity.RESULT_OK){
                val thumbnail: Bitmap? = data?.getParcelableExtra("data")
                Glide.with(requireActivity()).load().into(binding.editimg1)
            }
            else{
                Toast.makeText(context,"Select an Image",Toast.LENGTH_LONG).show()
            }
        }
    }

    class SelectorDialogFragment: DialogFragment(){

    }
}