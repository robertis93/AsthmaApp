package com.example.asthmaapp.view.fragments

import android.annotation.SuppressLint
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
import com.example.asthmaapp.view.adapters.AddAndUpdateMeasureAdapter
import com.example.asthmaapp.view.adapters.AddAndUpdateMedicamentTimeAdapter
import com.example.asthmaapp.viewmodel.viewModels.AddAndUpdateMeasureViewModelFactory
import com.example.asthmaapp.viewmodel.viewModels.AddAndUpdateMeasuresViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class AddAndUpdateMeasuresFragment : BaseFragment<FragmentAddBinding>() {
    private val args by navArgs<AddAndUpdateMeasuresFragmentArgs>()
    private val viewModel: AddAndUpdateMeasuresViewModel by viewModels {
        val mode = if (args.currentItemDay != null) {
            AddAndUpdateMeasuresViewModel.Mode.Update(args.currentItemDay!!)
        } else
            AddAndUpdateMeasuresViewModel.Mode.Add
        AddAndUpdateMeasureViewModelFactory(requireActivity().application, mode)
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
        val measureAdapterListener = object : AddAndUpdateMeasureAdapter.DeleteListener {
            override fun onDeleteMeasureClick(measure: Measure) {
                viewModel.onDeleteMeasureClick(measure)
            }

            override fun onUpdateMeasureClick(measure: Measure, position: Int) {
                updateTimeMeasure(measure, position)
            }
        }

        val taleMedicamentAdapterListener =
            object : AddAndUpdateMedicamentTimeAdapter.ClickListener {
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

        viewModel.measureListLiveData.observe(viewLifecycleOwner) { measureList ->
            val updateMeasureMode = args.currentItemDay != null
            val addMeasureAdapter =
                AddAndUpdateMeasureAdapter(measureList, measureAdapterListener, updateMeasureMode)
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
            val updateMedicamentMode = args.currentItemDay != null
            val addMedicamentTimeAdapter =
                AddAndUpdateMedicamentTimeAdapter(
                    it,
                    taleMedicamentAdapterListener,
                    updateMedicamentMode
                )
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

    private fun updateTimeMeasure(currentItem: Measure, position: Int) {
        val builder =
            androidx.appcompat.app.AlertDialog.Builder(requireContext())
        val layoutInflater = LayoutInflater.from(requireContext())
        val dialogFragment = LayoutDialogAddFragmentBinding.inflate(layoutInflater)
        dialogFragment.timePicker.is24HourView
        builder.setView(dialogFragment.root)
        builder.setTitle(R.string.measure_alarm_frag)

        val alertDialog = builder.show()
        dialogFragment.timePicker.setIs24HourView(true)

        dialogFragment.measureDialog.doAfterTextChanged { it: Editable? ->
            dialogFragment.btnSave.isEnabled =
                dialogFragment.measureDialog.getText().toString().length > 1
        }

        dialogFragment.timePicker.hour =
            DateUtil.timestampToDisplayHour(currentItem.dateTimestamp).toInt()
        dialogFragment.timePicker.minute =
            DateUtil.timestampToDisplayMinute(currentItem.dateTimestamp)
                .toInt()
        dialogFragment.measureDialog.setText(currentItem.value.toString())

        dialogFragment.btnSave.setOnClickListener {
            alertDialog.dismiss()
            dialogFragment.timePicker.is24HourView
            val timeHour = dialogFragment.timePicker.hour
            val timeMinute = dialogFragment.timePicker.minute
            val measurePeakFlowMeter = dialogFragment.measureDialog.text.toString().toInt()
            viewModel.onUpdateMeasureClick(position, timeHour, timeMinute, measurePeakFlowMeter)
            /* holder.binding.timeTextView.text = " ${DateUtil.timeCorrectDisplay(timeHour)} : ${
                 DateUtil.timeCorrectDisplay(
                     timeMinute
                 )
             } "
             holder.binding.measureText.text = measurePeakFlowMeter.toString()
             timeAndMeasureList[position].dateTimestamp =
                 DateUtil.dayTimeStampWithNewTime(currentItem.dateTimestamp, timeHour, timeMinute)
             timeAndMeasureList[position].value = measurePeakFlowMeter*/
        }

        dialogFragment.cancelBtn.setOnClickListener {
            alertDialog.dismiss()
        }
    }

    private fun updateTakeMedicamentTime(currentItem: TakeMedicamentTimeEntity, position: Int) {
        val builder =
            androidx.appcompat.app.AlertDialog.Builder(requireContext())
        val layoutInflater = LayoutInflater.from(requireContext())
        val dialogFragment = LayoutDialogMedicalAddFragmentBinding.inflate(layoutInflater)
        dialogFragment.timePicker.is24HourView
        builder.setView(dialogFragment.root)
        builder.setTitle(R.string.measure_alarm_frag)

        val alertDialog = builder.show()
        dialogFragment.timePicker.setIs24HourView(true)

        dialogFragment.timePicker.hour =
            DateUtil.timestampToDisplayHour(currentItem.dateTimeStamp).toInt()
        dialogFragment.timePicker.minute =
            DateUtil.timestampToDisplayTime(currentItem.dateTimeStamp)
                .toInt()

        dialogFragment.btnSave.setOnClickListener {
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
