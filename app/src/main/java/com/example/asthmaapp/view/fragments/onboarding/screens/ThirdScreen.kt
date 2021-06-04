package com.example.asthmaapp.view.fragments.onboarding.screens

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.asthmaapp.R
import com.example.asthmaapp.databinding.FragmentListBinding
import com.example.asthmaapp.databinding.FragmentThirdScreenBinding


class ThirdScreen : Fragment() {
    lateinit var binding: FragmentThirdScreenBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentThirdScreenBinding.inflate(inflater, container, false)
        Log.v("myLogs", "ThirdScreen onCreateView")
        binding.finishOnboardingBtn.setOnClickListener {
findNavController().navigate(R.id.action_viewPagerFragment_to_listFragment)
            onBoardingFinished()
        }

        return binding.root
    }
    private fun onBoardingFinished(){
        val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("Finished", true)
        editor.apply()
    }
}