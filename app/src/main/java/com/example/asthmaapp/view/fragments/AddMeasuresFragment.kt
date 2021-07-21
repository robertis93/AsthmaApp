package com.example.asthmaapp.view.fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.asthmaapp.R
import com.example.asthmaapp.databinding.FragmentAddBinding
import com.example.asthmaapp.databinding.LayoutDialogAddFragmentBinding
import com.example.asthmaapp.databinding.LayoutDialogMedicalAddFragmentBinding
import com.example.asthmaapp.model.Measure
import com.example.asthmaapp.model.TakeMedicamentTimeEntity
import com.example.asthmaapp.view.adapters.AddMeasureAdapter
import com.example.asthmaapp.view.adapters.AddMedicamentTimeAdapter
import com.example.asthmaapp.viewmodel.viewModels.AddMeasuresViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class AddMeasuresFragment : BaseFragment<FragmentAddBinding>() {
    private val viewModel: AddMeasuresViewModel by lazy {
        ViewModelProvider(this).get(AddMeasuresViewModel::class.java)
    }

    override fun inflate(inflater: LayoutInflater): FragmentAddBinding =
        FragmentAddBinding.inflate(inflater)

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setObservers()
        setInitMedicament()

        binding.changeDayButton.setOnClickListener {
            changeDay()
        }
        binding.addOneMeasureBtn.setOnClickListener {
            addMeasure()
        }

        binding.addOneMedBtn.setOnClickListener {
            addTimeTakeMedicament()
        }

        binding.saveBtn.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                val medicamentName = binding.editTextNameMedicament.text.toString()
                val medicamentDose = binding.editTextMedicamentDose.text.toString()
                viewModel.save(medicamentName, medicamentDose)
                withContext(Dispatchers.Main) {
                    findNavController().popBackStack()
                }
            }
        }
    }

    private fun setInitMedicament() {
        lifecycleScope.launch {
            val medicamentInfo = viewModel.getInitMedicamentInfo()
            binding.editTextNameMedicament.setText(medicamentInfo?.name)
            binding.editTextMedicamentDose.setText(medicamentInfo?.dose.toString())
        }
    }

    private fun setObservers() {
        val measureAdapterListener = object : AddMeasureAdapter.DeleteListener {
            override fun onDeleteMeasureClick(measure: Measure) {
                viewModel.onDeleteMeasureClick(measure)
            }
        }

        val taleMedicamentAdapterListener = object : AddMedicamentTimeAdapter.DeleteClickListener {
            override fun onDeleteTakeMedicamentTime(takeMedicamentTimeEntity: TakeMedicamentTimeEntity) {
                viewModel.onDeleteMedicamentClick(takeMedicamentTimeEntity)
            }
        }
        viewModel.measureListLiveData.observe(viewLifecycleOwner) { measureList ->
            val addMeasureAdapter = AddMeasureAdapter(measureList, measureAdapterListener)
            val recyclerViewAdd = binding.recyclerMeasure
            recyclerViewAdd.layoutManager =
                GridLayoutManager(
                    binding.recyclerMed.context,
                    4,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            recyclerViewAdd.adapter = addMeasureAdapter
        }

        viewModel.takeMedicamentTimeListLiveData.observe(viewLifecycleOwner) {
            val addMedicamentTimeAdapter =
                AddMedicamentTimeAdapter(it, taleMedicamentAdapterListener)
            val recyclerViewMed = binding.recyclerMed
            recyclerViewMed.layoutManager =
                GridLayoutManager(
                    binding.recyclerMed.context,
                    2,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            recyclerViewMed.adapter = addMedicamentTimeAdapter

        }

        viewModel.dateLiveData.observe(viewLifecycleOwner) { date ->
            binding.dateTextView.text = date
        }
    }

    private fun addTimeTakeMedicament() {
        val builder = AlertDialog.Builder(requireContext())
        val layoutInflater = LayoutInflater.from(requireContext())
        val dialogFragment = LayoutDialogMedicalAddFragmentBinding.inflate(layoutInflater)
        builder.setView(dialogFragment.root)
        builder.setTitle("Время приема лекарства")

        val alertDialog = builder.show()
        dialogFragment.timePicker.setIs24HourView(true)

        dialogFragment.btnSave.setOnClickListener {
            alertDialog.dismiss()
            val timeHour = dialogFragment.timePicker.hour
            val timeMinute = dialogFragment.timePicker.minute
            viewModel.onAddTakeMedicamentTimeClick(timeHour, timeMinute)
        }

        dialogFragment.cancelBtn.setOnClickListener {
            alertDialog.dismiss()
        }
    }

    private fun addMeasure() {
        val builder = AlertDialog.Builder(requireContext())
        val layoutInflater = LayoutInflater.from(requireContext())
        val dialogFragment = LayoutDialogAddFragmentBinding.inflate(layoutInflater)
        dialogFragment.timePicker.is24HourView
        builder.setView(dialogFragment.root)
        builder.setTitle(R.string.measure_alarm_frag)

        val alertDialog = builder.show()
        dialogFragment.timePicker.setIs24HourView(true)

        dialogFragment.measureDialog.doAfterTextChanged {
            dialogFragment.btnSave.isEnabled =
                dialogFragment.measureDialog.text.toString().length > 1
        }

        dialogFragment.btnSave.setOnClickListener {
            alertDialog.dismiss()
            dialogFragment.timePicker.is24HourView
            val timeHour = dialogFragment.timePicker.hour
            val timeMinute = dialogFragment.timePicker.minute
            val measureWithPeakFlowMeter = dialogFragment.measureDialog.text.toString().toInt()
            viewModel.onAddMeasureClick(timeHour, timeMinute, measureWithPeakFlowMeter)
        }
        dialogFragment.cancelBtn.setOnClickListener {
            alertDialog.dismiss()
        }
    }

    private fun changeDay() {
        val calendar = Calendar.getInstance()
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { datePicker, year, monthOfYear, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                calendar.add(Calendar.DAY_OF_MONTH, 1)

                val yearMeasure = datePicker.year
                val monthMeasure = datePicker.month
                val dayMeasure = datePicker.dayOfMonth

                val dateCalendar: Calendar =
                    GregorianCalendar(yearMeasure, monthMeasure, dayMeasure)

                val dateAfterChangedTimestamp = dateCalendar.time.time
                viewModel.changeDate(dateAfterChangedTimestamp)
            }

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            dateSetListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.datePicker.maxDate = viewModel.getMaxDateForDialog()
        datePickerDialog.show()
    }
}
