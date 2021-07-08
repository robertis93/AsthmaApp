package com.example.asthmaapp.view.fragments.onboarding.screens

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.asthmaapp.R
import com.example.asthmaapp.databinding.FragmentThirdScreenBinding
import com.example.asthmaapp.view.fragments.BaseFragment


class ThirdScreen : BaseFragment<FragmentThirdScreenBinding>() {
    override fun inflate(inflater: LayoutInflater): FragmentThirdScreenBinding =
        FragmentThirdScreenBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.finishOnboardingBtn.setOnClickListener {
            findNavController().navigate(R.id.action_viewPagerFragment_to_medicalFragment)
            onBoardingFinished()
        }
    }

    private fun onBoardingFinished() {
        val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("Finished", true)
        editor.apply()
    }
}