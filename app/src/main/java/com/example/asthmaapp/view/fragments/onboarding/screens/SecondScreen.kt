package com.example.asthmaapp.view.fragments.onboarding.screens

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.asthmaapp.R
import com.example.asthmaapp.databinding.FragmentFirstScreenBinding
import com.example.asthmaapp.databinding.FragmentSecondScreenBinding


class SecondScreen : Fragment() {

    lateinit var binding: FragmentSecondScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        Log.v("myLogs", "SecondScreen onCreateView")
        binding = FragmentSecondScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)
        binding.btnSecondScreen.setOnClickListener {
            viewPager?.currentItem = 2
        }
    }
}