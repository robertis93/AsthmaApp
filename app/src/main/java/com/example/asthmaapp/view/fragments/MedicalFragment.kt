package com.example.asthmaapp.view.fragments

import android.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
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
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        mMedicalViewModel = ViewModelProvider(this).get(MedicalViewModel::class.java)

        binding.btnSaveMedicalInfo.setOnClickListener {
            insertDataToDataBase()
            Log.i("myLogs", "MedicalFragment binding.btnSaveMedicalInfo.setOnClickListener ")
        }

//заполняем поля Edit последними значениями из базы данных чтобы пользователь видел, что он принимает
         mMedicalViewModel.readAllData.observe(viewLifecycleOwner, Observer { medication ->
             binding.editTextMedicalInfo.setText(
                 try {
                     medication.last().nameOfMedicine
                 }
             catch (e : NoSuchElementException) {
                 "Введите ваше лкарство"
             })
         //adapter.refreshAlarms(alarm)
        })

        mMedicalViewModel.readAllData.observe(viewLifecycleOwner, Observer { medication ->
            binding.editTextMedicalDose.setText(
                try {
                    medication.last().doseMedicine.toString()
                }
                catch (e : NoSuchElementException) {
                    "0"
                })
            //adapter.refreshAlarms(alarm)
        })

        mMedicalViewModel.readAllData.observe(viewLifecycleOwner, Observer { medication ->
            binding.editFrequencyMedical.setText(
                try {
                    medication.last().frequencyMedicine.toString()
                }
                catch (e : NoSuchElementException) {
                    "0"
                })
            //adapter.refreshAlarms(alarm)
        })



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