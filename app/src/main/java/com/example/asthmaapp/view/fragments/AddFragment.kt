package com.example.asthmaapp.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.asthmaapp.R
import com.example.asthmaapp.model.MeasureOfDay
import com.example.asthmaapp.viewmodel.viewModels.MeasureOfDayViewModel
import com.example.asthmaapp.databinding.FragmentAddBinding


class AddFragment : Fragment() {

    private lateinit var mMeasureViewModel: MeasureOfDayViewModel

    lateinit var binding: FragmentAddBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mMeasureViewModel = ViewModelProvider(this).get(MeasureOfDayViewModel::class.java)

        binding.button2.setOnClickListener {
            insertDataToDataBase()
        }
    }

    private fun insertDataToDataBase() {
        val firstTime = binding.firstTime.text.toString()
        val secondTime = binding.secondTime.text.toString()
        val thirdTime = binding.thirdTime.text.toString()
        val measure = MeasureOfDay(0, 100, 11, 111, firstTime, secondTime, thirdTime)
        mMeasureViewModel.addMeasure(measure)

//navigate Back
        findNavController().navigate(R.id.action_addFragment_to_listFragment)
        Toast.makeText(requireContext(), "Successfully added", Toast.LENGTH_LONG).show()
    }


}



