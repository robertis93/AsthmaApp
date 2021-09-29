package com.example.asthmaapp.view.fragments.onboarding

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewbinding.ViewBinding
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.asthmaapp.view.fragments.BaseFragment

class OnBoardingAdapter(
    list: ArrayList<BaseFragment<out ViewBinding>>,
    fm: FragmentManager,
    lifecycle: Lifecycle
) :
    FragmentStateAdapter(fm, lifecycle) {

    private val fragmentList = list
    override fun getItemCount() =
        fragmentList.size

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }
}