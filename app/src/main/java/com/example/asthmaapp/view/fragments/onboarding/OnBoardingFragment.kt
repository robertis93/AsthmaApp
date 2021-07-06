package com.example.asthmaapp.view.fragments.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.asthmaapp.databinding.FragmentViewPagerBinding
import com.example.asthmaapp.view.fragments.onboarding.screens.FirstScreen
import com.example.asthmaapp.view.fragments.onboarding.screens.SecondScreen
import com.example.asthmaapp.view.fragments.onboarding.screens.ThirdScreen

class OnBoardingFragment : Fragment() {

    lateinit var binding: FragmentViewPagerBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentViewPagerBinding.inflate(inflater, container, false)

        val springDotsIndicator = binding.springDotsIndicator
        val viewPager = binding.viewPager

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
        // Страницы, выходящие за пределы этого ограничения, будут удалены из иерархии представлений
        binding.viewPager.offscreenPageLimit = 1
        return binding.root
    }
}