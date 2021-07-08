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
import com.example.asthmaapp.model.MeasureOfDay
import com.example.asthmaapp.model.MedicamentTime
import com.example.asthmaapp.model.TimeAndMeasure
import com.example.asthmaapp.utils.AddMeasureFragmentDialog
import com.example.asthmaapp.view.adapters.AddMeasureAdapter
import com.example.asthmaapp.view.adapters.AddMedicamentTimeAdapter
import com.example.asthmaapp.viewmodel.viewModels.MeasureOfDayViewModel
import com.example.asthmaapp.viewmodel.viewModels.MedicamentViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates

class AddMeasuresFragment : BaseFragment<FragmentAddBinding>() {
    private lateinit var idMed: String
    private var dayMilliseconds by Delegates.notNull<Long>()
    private val dayMeasureViewModel: MeasureOfDayViewModel by lazy {
        ViewModelProvider(this).get(MeasureOfDayViewModel::class.java)
    }
    private val medicamentViewModel: MedicamentViewModel by lazy {
        ViewModelProvider(this).get(MedicamentViewModel::class.java)
    }
    private var dateMilli by Delegates.notNull<Long>()
    lateinit var timeAndMeasure: TimeAndMeasure
    private var yearMeasure by Delegates.notNull<Int>()
    private var monthMeasure by Delegates.notNull<Int>()
    private var dayMeasure by Delegates.notNull<Int>()
    private val timeAndMeasureList = mutableListOf<TimeAndMeasure>()

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
        dayMilliseconds = dateCalendar.time.time
        val yearToday: Int = dateCalendar.get(Calendar.YEAR)
        val monthToday: Int = dateCalendar.get(Calendar.MONTH)
        val dayOfMonthToday: Int = dateCalendar.get(Calendar.DAY_OF_MONTH)
        val justDayCalendar: Calendar =
            GregorianCalendar(yearToday, monthToday, dayOfMonthToday)
        val midnightDayMilliseconds = justDayCalendar.time.time

        idMed = midnightDayMilliseconds.toString()

        val currentDate = Date(dayMilliseconds)
        val dateFormat = SimpleDateFormat("dd MMM YYYY")
        val dayToday = dateFormat.format(currentDate)
        binding.dateTextView.text = dayToday

        medicamentViewModel.readAllData.observe(
            viewLifecycleOwner,
            { listMedicalInfo ->
                try {
                    binding.nameMedical.setText(listMedicalInfo.last().name)
                    binding.editTextMedicalDoze.setText(listMedicalInfo.last().dose.toString())
                } catch (e: Exception) {
                    val myDialogFragment =
                        AddMeasureFragmentDialog(R.string.you_forget_write_information_about_medication)
                    val fragmentManager = requireActivity().supportFragmentManager
                    myDialogFragment.show(fragmentManager, "myDialog")
                }
            }
        )

        binding.changeDayButton.setOnClickListener {
            changeDay()
        }

        binding.addOneMeasureBtn.setOnClickListener {
            addTimeOfMeasure(addMeasureAdapter)
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
            val nameMedicamentation = binding.nameMedical.text.toString()
            val doza = binding.editTextMedicalDoze.text.toString().toInt()

            val infoDay = MeasureOfDay(
                idMed,
                dayMilliseconds,
                nameMedicamentation,
                doza
            )
            val measuresOneDay = addMeasureAdapter.getData()
            for (measure in measuresOneDay) {
                dayMeasureViewModel.addTimeAndMeasure(measure)
            }

            dayMeasureViewModel.addMeasure(infoDay)

            val medicamentTime = addMedicamentTimeAdapter.getDataMedTime()
            for (medicTime in medicamentTime) {
                dayMeasureViewModel.addMedicalTime(medicTime)
            }

            findNavController().popBackStack()
        } catch (e: Exception) {
            val myDialogFragment = AddMeasureFragmentDialog(R.string.you_dont_write_all_information)
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
            val medicamentTime =
                MedicamentTime(0, timeHour, timeMinute, dayMilliseconds, idMed)
            addMedicamentTimeAdapter.addData(medicamentTime)
        }

        dialogFragment.cancelBtn.setOnClickListener {
            alertDialog.dismiss()
        }
    }

    private fun addTimeOfMeasure(addMeasureAdapter: AddMeasureAdapter) {
        val builder = AlertDialog.Builder(requireContext())
        val layoutInflater = LayoutInflater.from(requireContext())
        val dialogFragment = LayoutDialogAddFragmentBinding.inflate(layoutInflater)
        dialogFragment.timePicker.is24HourView
        builder.setView(dialogFragment.root)
        builder.setTitle(R.string.measure_alarm_frag)

        val mAlertDialog = builder.show()
        dialogFragment.timePicker.setIs24HourView(true)

        dialogFragment.measureDialog.doAfterTextChanged {
            dialogFragment.btnSave.isEnabled =
                dialogFragment.measureDialog.text.toString().length > 1
        }

        dialogFragment.btnSave.setOnClickListener {
            mAlertDialog.dismiss()
            dialogFragment.timePicker.is24HourView
            val timeHour = dialogFragment.timePicker.hour
            val timeMinute = dialogFragment.timePicker.minute
            val measureWithPicflometr = dialogFragment.measureDialog.text.toString().toInt()
            timeAndMeasure = TimeAndMeasure(0, idMed, timeHour, timeMinute, measureWithPicflometr)
            timeAndMeasureList.add(timeAndMeasure)
            addMeasureAdapter.addData(timeAndMeasure)
        }
        dialogFragment.cancelBtn.setOnClickListener {
            mAlertDialog.dismiss()
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
                dateMilli = dateCalendar.time.time

                idMed = dateMilli.toString()
                dayMilliseconds = dateMilli

                val currentDate = Date(dateMilli)
                val dateFormat = SimpleDateFormat("dd MMM YYYY")
                val day = dateFormat.format(currentDate)

                binding.dateTextView.visibility = View.VISIBLE
                binding.dateTextView.text = day.toString()
            }

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            dateSetListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.datePicker.maxDate = dayMilliseconds
        datePickerDialog.show()
    }
}
