package com.example.asthmaapp.view.fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.asthmaapp.R
import com.example.asthmaapp.databinding.FragmentAddBinding
import com.example.asthmaapp.databinding.LayoutDialogAddFragmentBinding
import com.example.asthmaapp.databinding.LayoutDialogMedicalAddFragmentBinding
import com.example.asthmaapp.model.Measure
import com.example.asthmaapp.model.MedicamentInfo
import com.example.asthmaapp.model.TakeMedicamentTimeEntity
import com.example.asthmaapp.utils.AddMeasureDialog
import com.example.asthmaapp.utils.DateUtil.dateTimeStampToSimpleDateFormatDayMonthYear
import com.example.asthmaapp.utils.DateUtil.dayTimeStamp
import com.example.asthmaapp.view.adapters.AddMeasureAdapter
import com.example.asthmaapp.view.adapters.AddMedicamentTimeAdapter
import com.example.asthmaapp.viewmodel.viewModels.MeasurementsPerDayViewModel
import java.util.*
import kotlin.properties.Delegates

class AddMeasuresFragment : BaseFragment<FragmentAddBinding>() {
    private var currentDayTimeStamp by Delegates.notNull<Long>()
    private val measurementsPerDayViewModel: MeasurementsPerDayViewModel by lazy {
        ViewModelProvider(this).get(MeasurementsPerDayViewModel::class.java)
    }
    private var dateAfterChangedTimestamp by Delegates.notNull<Long>()
    lateinit var measure: Measure
    private var yearMeasure by Delegates.notNull<Int>()
    private var monthMeasure by Delegates.notNull<Int>()
    private var dayMeasure by Delegates.notNull<Int>()

    private val timeAndMeasureList = mutableListOf<Measure>()

    override fun inflate(inflater: LayoutInflater): FragmentAddBinding =
        FragmentAddBinding.inflate(inflater)

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val addMeasureAdapter = AddMeasureAdapter()
        val recyclerViewAdd = binding.recyclerMeasure
        recyclerViewAdd.adapter = addMeasureAdapter
        recyclerViewAdd.layoutManager =
            GridLayoutManager(binding.recyclerMed.context, 2, LinearLayoutManager.HORIZONTAL, false)

        val addMedicamentTimeAdapter = AddMedicamentTimeAdapter()
        val recyclerViewMed = binding.recyclerMed
        recyclerViewMed.adapter = addMedicamentTimeAdapter
        recyclerViewMed.layoutManager =
            GridLayoutManager(binding.recyclerMed.context, 2, LinearLayoutManager.HORIZONTAL, false)

        val dateCalendar: Calendar = GregorianCalendar(TimeZone.getTimeZone("GMT+5"))
        currentDayTimeStamp = dateCalendar.time.time

        binding.dateTextView.text =
            dateTimeStampToSimpleDateFormatDayMonthYear(
                currentDayTimeStamp
            )

        measurementsPerDayViewModel.getAllMedicamentInfo.observe(
            viewLifecycleOwner,
            { listMedicalInfo ->
                try {
                    binding.editTextNameMedicament.setText(listMedicalInfo.last().name)
                    binding.editTextMedicamentDose.setText(listMedicalInfo.last().dose.toString())
                } catch (e: Exception) {
                    val myDialogFragment =
                        AddMeasureDialog(R.string.you_forget_write_information_about_medication)
                    val fragmentManager = requireActivity().supportFragmentManager
                    myDialogFragment.show(fragmentManager, "myDialog")
                }
            }
        )

        binding.changeDayButton.setOnClickListener {
            changeDay()
        }
        binding.addOneMeasureBtn.setOnClickListener {
            addMeasure(addMeasureAdapter)
        }

        binding.addOneMedBtn.setOnClickListener {
            addTimeTakeMedicament(addMedicamentTimeAdapter)
        }

        binding.saveBtn.setOnClickListener {
            insertToDataBase(addMeasureAdapter, addMedicamentTimeAdapter)
        }
    }

    private fun insertToDataBase(
        addMeasureAdapter: AddMeasureAdapter,
        addMedicamentTimeAdapter: AddMedicamentTimeAdapter
    ) {
        try {
            val nameMedication = binding.editTextNameMedicament.text.toString()
            val doseMedication = binding.editTextMedicamentDose.text.toString().toInt()

            val medicamentInfo =
                MedicamentInfo(
                    currentDayTimeStamp.toString(),
                    nameMedication,
                    doseMedication
                )
            measurementsPerDayViewModel.addMedicamentInfo(medicamentInfo)

            val measuresPerDay = addMeasureAdapter.getListMeasure()
            for (measure in measuresPerDay) {
                measurementsPerDayViewModel.addMeasure(measure)
            }

            val medicamentTimePerDay = addMedicamentTimeAdapter.getListMedicamentTime()
            for (medicamentTime in medicamentTimePerDay) {
                measurementsPerDayViewModel.addTakeMedicamentTime(medicamentTime)
            }

            findNavController().popBackStack()
        } catch (e: Exception) {
            val myDialogFragment = AddMeasureDialog(R.string.you_dont_write_all_information)
            val fragmentManager = requireActivity().supportFragmentManager
            myDialogFragment.show(fragmentManager, "myDialog")
        }
    }

    private fun addTimeTakeMedicament(addMedicamentTimeAdapter: AddMedicamentTimeAdapter) {
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
            val medicamentDayTimeStamp = dayTimeStamp(currentDayTimeStamp, timeHour, timeMinute)
            val medicamentTime =
                TakeMedicamentTimeEntity(
                    0,
                    medicamentDayTimeStamp,
                    currentDayTimeStamp.toString()
                )
            addMedicamentTimeAdapter.addData(medicamentTime)
        }

        dialogFragment.cancelBtn.setOnClickListener {
            alertDialog.dismiss()
        }
    }

    private fun addMeasure(addMeasureAdapter: AddMeasureAdapter) {
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
            val measureDayTimeStamp = dayTimeStamp(currentDayTimeStamp, timeHour, timeMinute)
            val measureWithPeakFlowMeter = dialogFragment.measureDialog.text.toString().toInt()

            measure =
                Measure(0, measureDayTimeStamp, measureWithPeakFlowMeter)
            timeAndMeasureList.add(measure)
            addMeasureAdapter.addMeasure(measure)
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

                yearMeasure = datePicker.year
                monthMeasure = datePicker.month
                dayMeasure = datePicker.dayOfMonth

                val dateCalendar: Calendar =
                    GregorianCalendar(yearMeasure, monthMeasure, dayMeasure)

                dateAfterChangedTimestamp = dateCalendar.time.time

                currentDayTimeStamp = dateAfterChangedTimestamp

                binding.dateTextView.visibility = View.VISIBLE
                binding.dateTextView.text =
                    dateTimeStampToSimpleDateFormatDayMonthYear(dateAfterChangedTimestamp)
            }

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            dateSetListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.datePicker.maxDate = currentDayTimeStamp
        datePickerDialog.show()
    }
}
