package com.example.asthmaapp.view.fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
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
import com.example.asthmaapp.view.adapters.AddFragmentMeasureAdapter
import com.example.asthmaapp.view.adapters.AddFragmentMedicamentTimeAdapter
import com.example.asthmaapp.viewmodel.viewModels.MeasureOfDayViewModel
import com.example.asthmaapp.viewmodel.viewModels.MedicalViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates


class AddMeasuresFragment : Fragment() {

    private lateinit var mDayMeasureViewModel: MeasureOfDayViewModel
    private lateinit var mMedicalViewModel: MedicalViewModel

    var dateMilli by Delegates.notNull<Long>()
    lateinit var timeAndMeasure: TimeAndMeasure
    var yearMeasure by Delegates.notNull<Int>()
    var mounthMeasure by Delegates.notNull<Int>()
    var dayMeasure by Delegates.notNull<Int>()

    //для времени и замера
    val timeAndMeasureList = mutableListOf<TimeAndMeasure>()


    lateinit var binding: FragmentAddBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddBinding.inflate(inflater, container, false)
        return binding.root
    }


    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val idMed = UUID.randomUUID().toString()
        binding.textDate.setVisibility(View.GONE)


        //recycler
        val addAdapter = AddFragmentMeasureAdapter()
        val recyclerViewAdd = binding.recyclerMeasure
        recyclerViewAdd.adapter = addAdapter
        recyclerViewAdd.layoutManager =
            GridLayoutManager(requireContext(), 2, LinearLayoutManager.VERTICAL, false)


        //recycler for MedicTime
        val medicAdapter = AddFragmentMedicamentTimeAdapter()
        val recyclerViewMed = binding.recyclerMed
        recyclerViewMed.adapter = medicAdapter
        recyclerViewMed.layoutManager =
            GridLayoutManager(requireContext(), 2, LinearLayoutManager.VERTICAL, false)

        //создаем настояющую дату для ограничения по датам DatePicker
        val dateCalendar: Calendar = GregorianCalendar(TimeZone.getTimeZone("GMT+5"))
        val dayMilli = dateCalendar.time.time


//MeasureViewModel
        mMedicalViewModel = ViewModelProvider(this).get(MedicalViewModel::class.java)
        mDayMeasureViewModel = ViewModelProvider(this).get(MeasureOfDayViewModel::class.java)


        //Вреия и замер
        binding.addOneMeasureBtn.setOnClickListener {
            //openDialog()
            val builder = AlertDialog.Builder(requireContext())
            val layoutInflater = LayoutInflater.from(requireContext())
            val dialogFragment = LayoutDialogAddFragmentBinding.inflate(layoutInflater)
            dialogFragment.timePicker.is24HourView
            builder.setView(dialogFragment.root)
            builder.setTitle("Measure")

            //show dialog
            val mAlertDialog = builder.show()
            dialogFragment.timePicker.setIs24HourView(true)

            //listener EditText
            dialogFragment.measureDialog.doAfterTextChanged { it: Editable? ->
                if (dialogFragment.measureDialog.getText().toString().length > 1)
                    dialogFragment.btnSave.setEnabled(true)
                else dialogFragment.btnSave.isEnabled = false
            }

            //сохраняем в бд замер
            dialogFragment.btnSave.setOnClickListener {
                // TODO: 04.06.2021 exception
                try {
                    mAlertDialog.dismiss()
                    dialogFragment.timePicker.is24HourView
                    val timeHour = dialogFragment.timePicker.hour
                    val timeMinute = dialogFragment.timePicker.minute
                    val measurePicf = dialogFragment.measureDialog.text.toString().toInt()
                    timeAndMeasure = TimeAndMeasure(0, timeHour, timeMinute, measurePicf, idMed)
                    timeAndMeasureList.add(timeAndMeasure)

                    addAdapter.addData(timeAndMeasure)
                } catch (e: Exception) {
                    val myDialogFragment = AddFragmentDialog("Вы забыли указать значение замера!")
                    val manager = getActivity()?.getSupportFragmentManager()
                    if (manager != null) {
                        myDialogFragment.show(manager, "myDialog")
                    }

                }
            }

            dialogFragment.btnCansel.setOnClickListener {
                mAlertDialog.dismiss()
            }
        }

        binding.addOneMedBtn.setOnClickListener {
            //openDialogMedical()
            val builder = AlertDialog.Builder(requireContext())
            val layoutInflater = LayoutInflater.from(requireContext())
            val dialogFragment = LayoutDialogMedicalAddFragmentBinding.inflate(layoutInflater)
            builder.setView(dialogFragment.root)
            builder.setTitle("Время приема лекарства")

            //show dialog
            val mAlertDialog = builder.show()
            dialogFragment.timePicker.setIs24HourView(true)

            dialogFragment.btnSave.setOnClickListener {

                try {
                    mAlertDialog.dismiss()
                    val timeHour = dialogFragment.timePicker.hour
                    val timeMinute = dialogFragment.timePicker.minute
                    val medicamentTime =
                        MedicamentTime(0, timeHour, timeMinute, dateMilli, idMed)
                    medicAdapter.addData(medicamentTime)

                } catch (e: Exception) {
                    val myDialogFragment = AddFragmentDialog("Сперва выберите дату!")
                    val manager = getActivity()?.getSupportFragmentManager()
                    if (manager != null) {
                        myDialogFragment.show(manager, "myDialog")
                    }
                }
            }

            val frequencyMedicament = medicAdapter.itemCount
            binding.editFrequencyMedical.setText(frequencyMedicament.toString())

            dialogFragment.btnCansel.setOnClickListener {
                Log.v("myLogs", "AddFragment  dialogFragment.btnCansel.setOnClickListener ")
                mAlertDialog.dismiss()
            }
        }

        binding.saveBtn.setOnClickListener {
            //insertToDataToDataBase()
            try {
                val nameMedicamentaion = binding.nameMedical.text.toString()
                val doza = binding.editTextMedicalDoze.text.toString().toInt()
                val frequency = binding.editFrequencyMedical.text.toString().toInt()

                val infoDay = MeasureOfDay(
                    idMed,
                    dateMilli,
                    nameMedicamentaion,
                    doza,
                    frequency
                )


                //insert time and measure
                val measuresOneDay = addAdapter.getData()
                for (measure in measuresOneDay) {
                    // idAddM =  mDayMeasureViewModel.addTimeAndMeasure(measure).toString().toInt()
                    mDayMeasureViewModel.addTimeAndMeasure(measure)
                }

                //insert Day
                mDayMeasureViewModel.addMeasure(infoDay)

                val medicamentTime = medicAdapter.getDataMedTime()
                for (medicTime in medicamentTime) {
                    mDayMeasureViewModel.addMedicalTime(medicTime)
                }

//navigate Back
                findNavController().navigate(R.id.action_addFragment_to_listFragment)


            } catch (e: Exception) {
                val myDialogFragment = AddFragmentDialog("Вы заполнили не все поля")
                val manager = activity?.getSupportFragmentManager()
                if (manager != null) {
                    myDialogFragment.show(manager, "myDialog")
                }
            }
        }

        binding.selectDayBtn.setOnClickListener {
            Log.v("myLogs", "AlarmFragment binding.floatingActionBtnAlarm.setOnClickListener ")
            val cal = Calendar.getInstance()

            val dateSetListener =
                DatePickerDialog.OnDateSetListener { datePicker, year, monthOfYear, dayOfMonth ->
                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, monthOfYear)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    cal.add(Calendar.DAY_OF_MONTH, 1)

                    yearMeasure = datePicker.year
                    mounthMeasure = datePicker.month
                    dayMeasure = datePicker.dayOfMonth

                    val dateCalendar: Calendar =
                        GregorianCalendar(yearMeasure, mounthMeasure, dayMeasure)
                    dateMilli = dateCalendar.time.time

                    //format date
                    val currentDate = Date(dateMilli)
                    val dateFormat = SimpleDateFormat("dd MMM YYYY")
                    val ddate = dateFormat.format(currentDate)

                    binding.textDate.setVisibility(View.VISIBLE)
                    binding.textDate.text = ddate.toString()
                }

            val datePickerDialog = DatePickerDialog(
                requireContext(),
                dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            )

            datePickerDialog.datePicker.setMaxDate(dayMilli)
            datePickerDialog.show()
        }
    }
}





