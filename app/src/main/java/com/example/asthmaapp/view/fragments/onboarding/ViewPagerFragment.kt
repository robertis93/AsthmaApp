package com.example.asthmaapp.view.fragments.onboarding

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.asthmaapp.R
import com.example.asthmaapp.databinding.FragmentListBinding
import com.example.asthmaapp.databinding.FragmentViewPagerBinding
import com.example.asthmaapp.view.fragments.onboarding.screens.FirstScreen
import com.example.asthmaapp.view.fragments.onboarding.screens.SecondScreen
import com.example.asthmaapp.view.fragments.onboarding.screens.ThirdScreen


class ViewPagerFragment : Fragment() {

    lateinit var binding: FragmentViewPagerBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentViewPagerBinding.inflate(inflater, container, false)
        Log.v("myLogs", "ViewPagerFragment  onCreateView")

        val fragmentList = arrayListOf<Fragment>(
            FirstScreen(),
            SecondScreen(),
            ThirdScreen()
        )

        val adapter = ViewPagerAdapter(
            fragmentList,

            requireActivity().supportFragmentManager,
            lifecycle
        )
        binding.viewPager.adapter = adapter
        // Страницы, выходящие за пределы этого ограничения, будут удалены из иерархии представлений
        binding.viewPager.offscreenPageLimit = 1
        return binding.root
    }
}