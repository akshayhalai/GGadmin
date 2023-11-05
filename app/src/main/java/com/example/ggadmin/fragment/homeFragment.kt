package com.example.ggadmin.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputBinding
import androidx.navigation.fragment.findNavController
import com.example.ggadmin.R
import com.example.ggadmin.activity.AllordersActivity
import com.example.ggadmin.databinding.FragmentHomeBinding


class homeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater)

        binding.button.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment_to_categoryFragment)
        }
        binding.button2.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment_to_productFragment2)
        }
        binding.button3.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment_to_sliderFragment)
        }
        binding.button4.setOnClickListener{
            startActivity(Intent(requireContext(),AllordersActivity::class.java))
        }




        return binding.root

    }

}