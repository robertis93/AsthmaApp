package com.rightname.asthmaapp.view.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.rightname.asthmaapp.R
import com.rightname.asthmaapp.databinding.MedicamentFragmentBinding
import com.rightname.asthmaapp.utils.DateUtil.takeMedicamentCheckingEnableSaveButton
import com.rightname.asthmaapp.viewmodel.viewModels.MedicamentViewModel
import kotlinx.coroutines.launch

class MedicamentFragment : BaseFragment<MedicamentFragmentBinding>() {

    private val medicamentViewModel: MedicamentViewModel by lazy {
        ViewModelProvider(this).get(MedicamentViewModel::class.java)
    }

    override fun inflate(inflater: LayoutInflater): MedicamentFragmentBinding =
        MedicamentFragmentBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setInitMedicament()
        takeMedicamentCheckingEnableSaveButton(
            binding.editTextMedicamentName,
            binding.editTextMedicamentDose,
            binding.saveBtn
        )

        binding.saveBtn.setOnClickListener {
            val medicamentName = binding.editTextMedicamentName.text.toString()
            val medicamentDose = binding.editTextMedicamentDose.text.toString()
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
                binding.editTextMedicamentDose.setText(medicamentInfo.dose.toString())
                binding.editTextMedicamentName.setText(medicamentInfo.name)
            } else {
                takeMedicamentCheckingEnableSaveButton(
                    binding.editTextMedicamentName,
                    binding.editTextMedicamentDose,
                    binding.saveBtn
                )
            }
        }
    }
}