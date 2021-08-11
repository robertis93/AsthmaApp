package com.rightname.asthmaapp.view.fragments.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.rightname.asthmaapp.databinding.FragmentViewPagerBinding
import com.rightname.asthmaapp.view.fragments.BaseFragment
import com.rightname.asthmaapp.view.fragments.onboarding.screens.FirstScreen
import com.rightname.asthmaapp.view.fragments.onboarding.screens.SecondScreen
import com.rightname.asthmaapp.view.fragments.onboarding.screens.ThirdScreen

class OnBoardingFragment : BaseFragment<FragmentViewPagerBinding>() {

    override fun inflate(inflater: LayoutInflater) =
        FragmentViewPagerBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val springDotsIndicator = binding.springDotsIndicator
        val viewPager = binding.viewPager
        val btn = binding.toNextFragmentBtn

        val fragmentList = arrayListOf(
            FirstScreen(),
            SecondScreen(),
            ThirdScreen()
        )
        val adapter = OnBoardingAdapter(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )
        binding.viewPager.adapter = adapter
        springDotsIndicator.setViewPager2(viewPager)
        binding.viewPager.offscreenPageLimit = 1
    }
}