package com.example.asthmaapp.view.fragments

import android.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.asthmaapp.R
import com.example.asthmaapp.databinding.FragmentUpdateBinding
import com.example.asthmaapp.databinding.MedicalFragmentBinding
import com.example.asthmaapp.model.MeasureOfDay
import com.example.asthmaapp.model.models.MedicalInfo
import com.example.asthmaapp.viewmodel.viewModels.MeasureOfDayViewModel
import com.example.asthmaapp.viewmodel.viewModels.MedicalViewModel

class MedicalFragment : Fragment() {

    lateinit var binding: MedicalFragmentBinding

    private lateinit var mMedicalViewModel: MedicalViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = MedicalFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        mMedicalViewModel = ViewModelProvider(this).get(MedicalViewModel::class.java)

        binding.btnSaveMedicalInfo.setOnClickListener {
            insertDataToDataBase()
            Log.i("myLogs", "MedicalFragment binding.btnSaveMedicalInfo.setOnClickListener ")
        }

    }

    private fun insertDataToDataBase() {
        val nameMedication = binding.editTextMedicalInfo.text.toString()
        val frequencyMedication = binding.editFrequencyMedical.text.toString().toInt()
        val doseMedication = binding.editTextMedicalDose.text.toString().toInt()
        val medicalInfo =
            MedicalInfo(
                0,
                nameMedication,
                frequencyMedication,
                doseMedication
            )
        mMedicalViewModel.addMedicalInfo(medicalInfo)

        Toast.makeText(requireContext(), "Updated success", Toast.LENGTH_SHORT).show()
    }

}