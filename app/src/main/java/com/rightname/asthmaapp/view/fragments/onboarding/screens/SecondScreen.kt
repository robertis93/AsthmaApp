package com.rightname.asthmaapp.view.fragments.onboarding.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.rightname.asthmaapp.R
import com.rightname.asthmaapp.databinding.FragmentSecondScreenBinding
import com.rightname.asthmaapp.view.fragments.BaseFragment


class SecondScreen : BaseFragment<FragmentSecondScreenBinding>() {

    override fun inflate(inflater: LayoutInflater): FragmentSecondScreenBinding =
        FragmentSecondScreenBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)
        binding.toSecondScreenBtn.setOnClickListener {
            viewPager?.currentItem = 2
        }
    }
}