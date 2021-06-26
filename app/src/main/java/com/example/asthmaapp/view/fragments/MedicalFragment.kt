package com.example.asthmaapp.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.asthmaapp.databinding.MedicalFragmentBinding
import com.example.asthmaapp.model.models.MedicalInfo
import com.example.asthmaapp.utils.AddFragmentDialog
import com.example.asthmaapp.utils.MedicalDialog
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
            val myDialogFragment = MedicalDialog("успешно сохранено")
            val manager = activity?.getSupportFragmentManager()
            if (manager != null) {
                myDialogFragment.show(manager, "myDialog")

            }
        }

//заполняем поля Edit последними значениями из базы данных чтобы пользователь видел, что он принимает
        mMedicalViewModel.readAllData.observe(viewLifecycleOwner, Observer { medication ->
            if (medication.size > 0) {
                binding.editTextMedicalInfo.setText(medication.last().nameOfMedicine)
                binding.editFrequencyMedical.setText(medication.last().frequencyMedicine.toString())
                binding.editTextMedicalDose.setText(medication.last().doseMedicine.toString())
            }
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
    }
}