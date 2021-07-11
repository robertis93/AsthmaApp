package com.example.asthmaapp.view.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.asthmaapp.R
import com.example.asthmaapp.databinding.MedicalFragmentBinding
import com.example.asthmaapp.model.MedicamentInfo
import com.example.asthmaapp.utils.SaveMedicamentDialog
import com.example.asthmaapp.viewmodel.viewModels.MeasurementsPerDayViewModel

class MedicamentFragment : BaseFragment<MedicalFragmentBinding>() {

    private val measurementsPerDayViewModel: MeasurementsPerDayViewModel by lazy {
        ViewModelProvider(this).get(MeasurementsPerDayViewModel::class.java)
    }

    override fun inflate(inflater: LayoutInflater): MedicalFragmentBinding =
        MedicalFragmentBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        measurementsPerDayViewModel.readAllData.observe(viewLifecycleOwner, Observer { medication ->
            if (medication.isNotEmpty()) {
                binding.editTextMedicalInfo.setText(medication.last().name)
                binding.editTextMedicalDose.setText(medication.last().dose.toString())
            }
        })

        binding.btnSaveMedicalInfo.setOnClickListener {
            insertDataToDataBase()
            val myDialogFragment = SaveMedicamentDialog(R.string.successful_added)
            val manager = activity?.getSupportFragmentManager()
            if (manager != null) {
                myDialogFragment.show(manager, "myDialog")
            }
            val hideKeyboard = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            hideKeyboard.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        }
    }

    private fun insertDataToDataBase() {
        val nameMedication = binding.editTextMedicalInfo.text.toString()
        val doseMedication = binding.editTextMedicalDose.text.toString().toInt()
        val medicamentInfo =
            MedicamentInfo(
                0,
                nameMedication,
                doseMedication
            )
        measurementsPerDayViewModel.addMedicalInfo(medicamentInfo)
    }
}