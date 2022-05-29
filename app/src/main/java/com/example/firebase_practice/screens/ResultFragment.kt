package com.example.firebase_practice.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.firebase_practice.databinding.FragmentResultBinding

class ResultFragment : Fragment() {

    private lateinit var binding:FragmentResultBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentResultBinding.inflate(layoutInflater)

        return binding.root
    }


}