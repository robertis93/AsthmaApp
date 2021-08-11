package com.rightname.asthmaapp.view.fragments.onboarding.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.rightname.asthmaapp.R
import com.rightname.asthmaapp.databinding.FragmentFirstScreenBinding
import com.rightname.asthmaapp.view.fragments.BaseFragment

class FirstScreen : BaseFragment<FragmentFirstScreenBinding>() {
    override fun inflate(inflater: LayoutInflater): FragmentFirstScreenBinding =
        FragmentFirstScreenBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)
        binding.nextBtn.setOnClickListener {
            viewPager?.currentItem = 1
        }
    }
}