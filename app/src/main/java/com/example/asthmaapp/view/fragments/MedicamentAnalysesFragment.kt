package com.example.asthmaapp.view.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.asthmaapp.R
import com.example.asthmaapp.databinding.FragmentAddBinding
import com.example.asthmaapp.databinding.LayoutDialogAddFragmentBinding
import com.example.asthmaapp.databinding.LayoutDialogMedicalAddFragmentBinding
import com.example.asthmaapp.model.Measure
import com.example.asthmaapp.model.TakeMedicamentTimeEntity
import com.example.asthmaapp.utils.DateUtil
import com.example.asthmaapp.view.adapters.EditMeasureAdapter
import com.example.asthmaapp.view.adapters.EditTakeMedicamentTimeAdapter
import com.example.asthmaapp.viewmodel.viewModels.AddAndUpdateMeasureViewModelFactory
import com.example.asthmaapp.viewmodel.viewModels.MedicamentAnalysesViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class MedicamentAnalysesFragment : BaseFragment<FragmentAddBinding>() {
    private val args by navArgs<MedicamentAnalysesFragmentArgs>()
    private val viewModel: MedicamentAnalysesViewModel by viewModels {
        val mode = if (args.currentItemDay != null) {
            MedicamentAnalysesViewModel.Mode.Update(args.currentItemDay!!)
        } else {
            MedicamentAnalysesViewModel.Mode.Add
        }
        AddAndUpdateMeasureViewModelFactory(requireActivity().application, mode)
    }

    override fun inflate(inflater: LayoutInflater): FragmentAddBinding =
        FragmentAddBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.changeDayBtn.setOnClickListener {
            changeDay()
        }
        binding.addOneMeasureBtn.setOnClickListener {
            addMeasure()
        }

        binding.addOneTakeMedicamentTimeBtn.setOnClickListener {
            addTimeTakeMedicament()
        }

        DateUtil.checkingEnableButton(
            binding.nameMedicamentEditText,
            binding.doseMedicamentEditText,
            binding.saveBtn
        )

        binding.saveBtn.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                val medicamentName = binding.nameMedicamentEditText.text.toString()
                val medicamentDose = binding.doseMedicamentEditText.text.toString()
                viewModel.save(medicamentName, medicamentDose)
                withContext(Dispatchers.Main) {
                    findNavController().popBackStack()
                }
            }
        }

        initObservers()
        getMedicamentInfo()
    }

    private fun getMedicamentInfo() {
        lifecycleScope.launch {
            val medicamentInfo = viewModel.getInitMedicamentInfo()
            binding.nameMedicamentEditText.setText(medicamentInfo?.name)
            if (medicamentInfo != null) {
                binding.doseMedicamentEditText.setText(medicamentInfo.dose.toString())
            }
        }
    }

    private fun initObservers() {
        val measureAdapterListener = object : EditMeasureAdapter.ClickListener {
            override fun onDeleteMeasureClick(measure: Measure) {
                viewModel.onDeleteMeasureClick(measure)
            }

            override fun onUpdateMeasureClick(measure: Measure, position: Int) {
                updateMeasure(measure, position)
            }
        }

        val takeMedicamentAdapterListener =
            object : EditTakeMedicamentTimeAdapter.ClickListener {
                override fun onDeleteTakeMedicamentTime(takeMedicamentTimeEntity: TakeMedicamentTimeEntity) {
                    viewModel.onDeleteMedicamentClick(takeMedicamentTimeEntity)
                }

                override fun onUpdateTakeMedicamentTime(
                    takeMedicamentTimeEntity: TakeMedicamentTimeEntity,
                    position: Int
                ) {
                    updateTakeMedicamentTime(takeMedicamentTimeEntity, position)
                }
            }

        val updateMeasureMode = args.currentItemDay != null
        val editMeasureAdapter =
            EditMeasureAdapter(measureAdapterListener, updateMeasureMode)
        val editMeasureRecyclerView = binding.measureRecyclerView
        editMeasureRecyclerView.adapter = editMeasureAdapter
        editMeasureRecyclerView.layoutManager =
            GridLayoutManager(
                context,
                4,
                LinearLayoutManager.HORIZONTAL,
                false
            )

        val updateMedicamentMode = args.currentItemDay != null
        val editTakeMedicamentTimeAdapter =
            EditTakeMedicamentTimeAdapter(
                takeMedicamentAdapterListener,
                updateMedicamentMode
            )
        val editTakeMedicamentTimeRecyclerView = binding.takeMedicamentTimeRecyclerView
        editTakeMedicamentTimeRecyclerView.adapter = editTakeMedicamentTimeAdapter
        editTakeMedicamentTimeRecyclerView.layoutManager =
            GridLayoutManager(
                context,
                2,
                LinearLayoutManager.HORIZONTAL,
                false
            )

        viewModel.measureListLiveData.observe(viewLifecycleOwner) { measureList ->
            editMeasureAdapter.setData(measureList)
        }

        viewModel.takeMedicamentTimeListLiveData.observe(viewLifecycleOwner) { takeMedicamentTimeList ->
            editTakeMedicamentTimeAdapter.setData(takeMedicamentTimeList)
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
        builder.setTitle(getString(R.string.take_medicament))

        val alertDialog = builder.show()
        dialogFragment.timePicker.setIs24HourView(true)

        dialogFragment.saveBtn.setOnClickListener {
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
            dialogFragment.saveBtn.isEnabled =
                dialogFragment.measureDialog.text.toString().length in 2..4
        }

        dialogFragment.saveBtn.setOnClickListener {
            alertDialog.dismiss()
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

    private fun updateMeasure(currentItem: Measure, position: Int) {
        val builder = AlertDialog.Builder(requireContext())
        val layoutInflater = LayoutInflater.from(requireContext())
        val dialogFragment = LayoutDialogAddFragmentBinding.inflate(layoutInflater)
        dialogFragment.timePicker.is24HourView
        builder.setView(dialogFragment.root)
        builder.setTitle(R.string.measure_alarm_frag)

        val alertDialog = builder.show()
        dialogFragment.timePicker.setIs24HourView(true)

        dialogFragment.measureDialog.doAfterTextChanged { it: Editable? ->
            dialogFragment.saveBtn.isEnabled =
                dialogFragment.measureDialog.text.toString().length > 1
        }

        dialogFragment.timePicker.hour =
            DateUtil.timestampToDisplayHour(currentItem.dateTimestamp).toInt()
        dialogFragment.timePicker.minute =
            DateUtil.timestampToDisplayMinute(currentItem.dateTimestamp)
                .toInt()
        dialogFragment.measureDialog.setText(currentItem.value.toString())

        dialogFragment.saveBtn.setOnClickListener {
            alertDialog.dismiss()
            dialogFragment.timePicker.is24HourView
            val timeHour = dialogFragment.timePicker.hour
            val timeMinute = dialogFragment.timePicker.minute
            val measurePeakFlowMeter = dialogFragment.measureDialog.text.toString().toInt()
            viewModel.onUpdateMeasureClick(position, timeHour, timeMinute, measurePeakFlowMeter)
        }

        dialogFragment.cancelBtn.setOnClickListener {
            alertDialog.dismiss()
        }
    }

    private fun updateTakeMedicamentTime(currentItem: TakeMedicamentTimeEntity, position: Int) {
        val builder = AlertDialog.Builder(requireContext())
        val layoutInflater = LayoutInflater.from(requireContext())
        val dialogFragment = LayoutDialogMedicalAddFragmentBinding.inflate(layoutInflater)
        dialogFragment.timePicker.is24HourView
        builder.setView(dialogFragment.root)
        builder.setTitle(R.string.measure_alarm_frag)

        val alertDialog = builder.show()
        dialogFragment.timePicker.setIs24HourView(true)

        dialogFragment.timePicker.hour =
            DateUtil.timestampToDisplayHour(currentItem.dateTimestamp).toInt()
        dialogFragment.timePicker.minute =
            DateUtil.timestampToDisplayMinute(currentItem.dateTimestamp)
                .toInt()

        dialogFragment.saveBtn.setOnClickListener {
            alertDialog.dismiss()
            dialogFragment.timePicker.is24HourView
            val timeHour = dialogFragment.timePicker.hour
            val timeMinute = dialogFragment.timePicker.minute
            viewModel.onUpdateTakeMedicamentTimeClick(position, timeHour, timeMinute)
        }
        dialogFragment.cancelBtn.setOnClickListener {
            alertDialog.dismiss()
        }
    }
}
