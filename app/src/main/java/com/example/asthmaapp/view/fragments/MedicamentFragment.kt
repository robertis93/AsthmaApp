package com.example.asthmaapp.view.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.asthmaapp.R
import com.example.asthmaapp.databinding.MedicamentFragmentBinding
import com.example.asthmaapp.utils.DateUtil.checkingEnableButton
import com.example.asthmaapp.viewmodel.viewModels.MedicamentViewModel
import kotlinx.coroutines.launch

class MedicamentFragment : BaseFragment<MedicamentFragmentBinding>() {
    val blockCharacterSet ="1234567890"


    private val medicamentViewModel: MedicamentViewModel by lazy {
        ViewModelProvider(this).get(MedicamentViewModel::class.java)
    }

    override fun inflate(inflater: LayoutInflater): MedicamentFragmentBinding =
        MedicamentFragmentBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setInitMedicament()
        checkingEnableButton(
            binding.medicamentNameEditText,
            binding.medicamentDoseEditText,
            binding.saveBtn
        )
        binding.medicamentDoseEditText.doAfterTextChanged{s ->

                if (s != null) {
                    for (i in 1..s.length) {
                        if (!blockCharacterSet.contains(s[i-1])) {
                                binding.medicamentDoseEditText.setText("")
                            binding.medicamentDoseLayoutEditText.prefixText = "Введите целое число"
                        }
                        else {
                            binding.medicamentDoseLayoutEditText.prefixText = ""
                        }
                    }
                }
            }


        binding.saveBtn.setOnClickListener {
            val medicamentName = binding.medicamentNameEditText.text.toString()
            val medicamentDose = binding.medicamentDoseEditText.text.toString()
            medicamentViewModel.addMedicamentInfo(medicamentName, medicamentDose)
            val hideKeyboard =
                context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            hideKeyboard.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
            findNavController().navigate(R.id.action_medicalFragment_to_alarmFragment)
        }

    }

    private fun setInitMedicament() {
        lifecycleScope.launch {
            val medicamentInfo = medicamentViewModel.getInitMedicamentInfo()
            if (medicamentInfo != null) {
                binding.medicamentDoseEditText.setText(medicamentInfo.dose.toString())
                binding.medicamentNameEditText.setText(medicamentInfo.name)
            } else {
                checkingEnableButton(
                    binding.medicamentNameEditText,
                    binding.medicamentDoseEditText,
                    binding.saveBtn
                )
            }
        }
    }
}