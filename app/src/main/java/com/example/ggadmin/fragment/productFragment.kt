package com.example.ggadmin.fragment

import android.os.Binder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.ggadmin.R
import com.example.ggadmin.databinding.FragmentCategoryBinding
import com.example.ggadmin.databinding.FragmentProductBinding

class productFragment : Fragment() {

    private lateinit var binding: FragmentProductBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductBinding.inflate(layoutInflater)

        binding.floatingActionButton.setOnClickListener{

        Navigation.findNavController(it).navigate(R.id.action_productFragment2_to_addproductFragment)
        }
      return binding.root
    }

}