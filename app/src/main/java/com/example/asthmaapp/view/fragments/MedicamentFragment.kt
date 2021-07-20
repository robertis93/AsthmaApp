package com.example.asthmaapp.view.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.asthmaapp.R
import com.example.asthmaapp.databinding.MedicamentFragmentBinding
import com.example.asthmaapp.utils.SaveMedicamentDialog
import com.example.asthmaapp.viewmodel.viewModels.MedicamentViewModel

class MedicamentFragment : BaseFragment<MedicamentFragmentBinding>() {

    private val medicamentViewModel: MedicamentViewModel by lazy {
        ViewModelProvider(this).get(MedicamentViewModel::class.java)
    }

    override fun inflate(inflater: LayoutInflater): MedicamentFragmentBinding =
        MedicamentFragmentBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        medicamentViewModel.medicamentInfoLiveData.observe(
            viewLifecycleOwner,
            { medicamentInfo ->
                binding.editTextMedicamentName.setText(medicamentInfo.name)
                binding.editTextMedicamentDose.setText(medicamentInfo.dose.toString())
            }
        )

        binding.editTextMedicamentName.doAfterTextChanged {
            medicamentViewModel.changeMedicamentName(it.toString())
            if (it != null) {
                binding.btnSaveMedicamentInfo.isEnabled =
                    it.isNotEmpty()
            }
        }

        binding.editTextMedicamentDose.doAfterTextChanged {
            medicamentViewModel.changeMedicamentDose(it.toString())
            if (it != null) {
                binding.btnSaveMedicamentInfo.isEnabled =
                    it.isNotEmpty()
            }
        }

        binding.btnSaveMedicamentInfo.setOnClickListener {
            medicamentViewModel.addMedicamentInfo()

            val myDialogFragment = SaveMedicamentDialog(R.string.successful_added)
            val manager = activity?.supportFragmentManager
            if (manager != null) {
                myDialogFragment.show(manager, "myDialog")
            }
            val hideKeyboard =
                context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            hideKeyboard.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
            findNavController().navigate(R.id.action_medicalFragment_to_alarmFragment)
        }
    }
}