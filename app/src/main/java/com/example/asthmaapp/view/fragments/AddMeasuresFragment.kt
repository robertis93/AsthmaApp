package com.example.asthmaapp.view.fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
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
import com.example.asthmaapp.utils.AddFragmentDialog
import com.example.asthmaapp.view.adapters.AddMeasureAdapter
import com.example.asthmaapp.view.adapters.AddMedicamentTimeAdapter
import com.example.asthmaapp.viewmodel.viewModels.MeasureOfDayViewModel
import com.example.asthmaapp.viewmodel.viewModels.MedicalViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates

class AddMeasuresFragment : Fragment() {
    lateinit var binding: FragmentAddBinding
    private lateinit var idMed: String
    private var dayMilliseconds by Delegates.notNull<Long>()
    private var frequencyMedicament by Delegates.notNull<Int>()
    private lateinit var mDayMeasureViewModel: MeasureOfDayViewModel
    private lateinit var mMedicalViewModel: MedicalViewModel
    private var dateMilli by Delegates.notNull<Long>()
    lateinit var timeAndMeasure: TimeAndMeasure
    private var yearMeasure by Delegates.notNull<Int>()
    private var monthMeasure by Delegates.notNull<Int>()
    private var dayMeasure by Delegates.notNull<Int>()
    private val timeAndMeasureList = mutableListOf<TimeAndMeasure>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val addMeasureAdapter = AddMeasureAdapter()
        val recyclerViewAdd = binding.recyclerMeasure
        recyclerViewAdd.adapter = addMeasureAdapter
        recyclerViewAdd.layoutManager =
            GridLayoutManager(requireContext(), 2, LinearLayoutManager.VERTICAL, false)

        val deleteClickListener: AddMedicamentTimeAdapter.OnDeleteClickListener =
            object : AddMedicamentTimeAdapter.OnDeleteClickListener {   //
                override fun onDeleteAlarmClick() {
                    frequencyMedicament -= 1
                    binding.editFrequencyMedical.setText(frequencyMedicament.toString())
                }
            }
        val addMedicamentTimeAdapter = AddMedicamentTimeAdapter(deleteClickListener)
        val recyclerViewMed = binding.recyclerMed
        recyclerViewMed.adapter = addMedicamentTimeAdapter
        recyclerViewMed.layoutManager =
            GridLayoutManager(requireContext(), 2, LinearLayoutManager.VERTICAL, false)

        //создаем настояющую дату для ограничения по датам DatePicker
        val dateCalendar: Calendar = GregorianCalendar(TimeZone.getTimeZone("GMT+5"))
        dayMilliseconds = dateCalendar.time.time
        val yearToday: Int = dateCalendar.get(Calendar.YEAR)
        val monthToday: Int = dateCalendar.get(Calendar.MONTH)
        val dayOfMonthToday: Int = dateCalendar.get(Calendar.DAY_OF_MONTH)
        val justDayCalendar: Calendar =
            GregorianCalendar(yearToday, monthToday, dayOfMonthToday)
        var justDayMilli = justDayCalendar.time.time

        idMed = justDayMilli.toString()

        //format date
        val currentDate = Date(dayMilliseconds)
        val dateFormat = SimpleDateFormat("dd MMM YYYY")
        val dayToday = dateFormat.format(currentDate)
        binding.dateTextView.text = dayToday

        mMedicalViewModel = ViewModelProvider(this).get(MedicalViewModel::class.java)
        mDayMeasureViewModel = ViewModelProvider(this).get(MeasureOfDayViewModel::class.java)

        mMedicalViewModel.readAllData.observe(
            viewLifecycleOwner,
            { listMedicalInfo ->
                // TODO: сделать, т.к. вылетает если ничего нету
                binding.nameMedical.setText(listMedicalInfo.last().nameOfMedicine)
                binding.editFrequencyMedical.setText(listMedicalInfo.last().frequencyMedicine.toString())
                binding.editTextMedicalDoze.setText(listMedicalInfo.last().doseMedicine.toString())
            }
        )

        binding.changeDayButton.setOnClickListener {
            changeDay()
        }

        // Время и замер
        binding.addOneMeasureBtn.setOnClickListener {
            addTimeOfMeasure(addMeasureAdapter)
        }

        binding.addOneMedBtn.setOnClickListener {
            addTimeTakeMedicament(addMedicamentTimeAdapter)
        }

        binding.saveBtn.setOnClickListener {
            insertToDataToDataBase(addMeasureAdapter, addMedicamentTimeAdapter)
        }
    }

    private fun insertToDataToDataBase(
        addMeasureAdapter: AddMeasureAdapter,
        addMedicamentTimeAdapter: AddMedicamentTimeAdapter
    ) {
        try {
            val nameMedicamentation = binding.nameMedical.text.toString()
            val doza = binding.editTextMedicalDoze.text.toString().toInt()
            val frequency = binding.editFrequencyMedical.text.toString().toInt()

            val infoDay = MeasureOfDay(
                idMed,
                dayMilliseconds,
                nameMedicamentation,
                doza,
                frequency
            )
            val measuresOneDay = addMeasureAdapter.getData()
            for (measure in measuresOneDay) {
                mDayMeasureViewModel.addTimeAndMeasure(measure)
            }

            mDayMeasureViewModel.addMeasure(infoDay)

            val medicamentTime = addMedicamentTimeAdapter.getDataMedTime()
            for (medicTime in medicamentTime) {
                mDayMeasureViewModel.addMedicalTime(medicTime)
            }

            findNavController().popBackStack()
        } catch (e: Exception) {
            val myDialogFragment = AddFragmentDialog("Вы заполнили не все поля")
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

        //show dialog
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

        //frequency take medicament
        frequencyMedicament = addMedicamentTimeAdapter.itemCount + 1
        binding.editFrequencyMedical.setText(frequencyMedicament.toString())

        dialogFragment.cancelBtn.setOnClickListener {
            Log.v("myLogs", "AddFragment  dialogFragment.btnCansel.setOnClickListener ")
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

        // show dialog
        val mAlertDialog = builder.show()
        dialogFragment.timePicker.setIs24HourView(true)

        // listener EditText
        dialogFragment.measureDialog.doAfterTextChanged {
            dialogFragment.btnSave.isEnabled =
                dialogFragment.measureDialog.text.toString().length > 1
        }

        //сохраняем в бд замер
        dialogFragment.btnSave.setOnClickListener {
            mAlertDialog.dismiss()
            dialogFragment.timePicker.is24HourView
            val timeHour = dialogFragment.timePicker.hour
            val timeMinute = dialogFragment.timePicker.minute
            val measurePicf = dialogFragment.measureDialog.text.toString().toInt()
            timeAndMeasure = TimeAndMeasure(0, timeHour, timeMinute, measurePicf, idMed)
            timeAndMeasureList.add(timeAndMeasure)

            addMeasureAdapter.addData(timeAndMeasure)
        }

        dialogFragment.cancelBtn.setOnClickListener {
            mAlertDialog.dismiss()
        }
    }

    private fun changeDay() {
        Log.v("myLogs", "AlarmFragment binding.floatingActionBtnAlarm.setOnClickListener ")
        val cal = Calendar.getInstance()

        val dateSetListener =
            DatePickerDialog.OnDateSetListener { datePicker, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                cal.add(Calendar.DAY_OF_MONTH, 1)

                yearMeasure = datePicker.year
                monthMeasure = datePicker.month
                dayMeasure = datePicker.dayOfMonth

                val dateCalendar: Calendar =
                    GregorianCalendar(yearMeasure, monthMeasure, dayMeasure)
                dateMilli = dateCalendar.time.time

                idMed = dateMilli.toString()
                dayMilliseconds = dateMilli
                // format date
                val currentDate = Date(dateMilli)
                val dateFormat = SimpleDateFormat("dd MMM YYYY")
                val ddate = dateFormat.format(currentDate)

                binding.dateTextView.visibility = View.VISIBLE
                binding.dateTextView.text = ddate.toString()
            }

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            dateSetListener,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.datePicker.maxDate = dayMilliseconds
        datePickerDialog.show()
    }
}
