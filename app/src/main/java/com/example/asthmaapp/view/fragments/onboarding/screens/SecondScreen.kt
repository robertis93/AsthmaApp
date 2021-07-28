package com.example.asthmaapp.view.fragments.onboarding.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.example.asthmaapp.R
import com.example.asthmaapp.databinding.FragmentSecondScreenBinding
import com.example.asthmaapp.view.fragments.BaseFragment


class SecondScreen : BaseFragment<FragmentSecondScreenBinding>() {

    override fun inflate(inflater: LayoutInflater): FragmentSecondScreenBinding =
        FragmentSecondScreenBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)
        binding.btnSecondScreen.setOnClickListener {
            viewPager?.currentItem = 2
        }
    }
}