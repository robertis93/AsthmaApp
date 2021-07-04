package com.example.asthmaapp.view.fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
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
import com.example.asthmaapp.view.adapters.AddFragmentMeasureAdapter
import com.example.asthmaapp.view.adapters.AddFragmentMedicamentTimeAdapter
import com.example.asthmaapp.viewmodel.viewModels.MeasureOfDayViewModel
import com.example.asthmaapp.viewmodel.viewModels.MedicalViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates


class AddMeasuresFragment : Fragment() {

    lateinit var idMed : String
    var dayMilli by Delegates.notNull<Long>()
    private var frequencyMedicament by Delegates.notNull<Int>()
    var counter = 0
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




        //recycler
        val addAdapter = AddFragmentMeasureAdapter()
        val recyclerViewAdd = binding.recyclerMeasure
        recyclerViewAdd.adapter = addAdapter
        recyclerViewAdd.layoutManager =
            GridLayoutManager(requireContext(), 2, LinearLayoutManager.VERTICAL, false)

        //удаление времени при нажатии кнопки удалить
        // определяем слушателя нажатия элемента в списке
        // определяем слушателя нажатия элемента в списке

        val deleteClickListener: AddFragmentMedicamentTimeAdapter.OnDeleteClickListener =
            object : AddFragmentMedicamentTimeAdapter.OnDeleteClickListener {   //
                override fun onDeleteAlarmClick() {

                    frequencyMedicament-- - 1
                    binding.editFrequencyMedical.setText(frequencyMedicament.toString())
                }
            }

        //recycler for MedicTime
        val medicAdapter = AddFragmentMedicamentTimeAdapter(deleteClickListener)
        val recyclerViewMed = binding.recyclerMed
        recyclerViewMed.adapter = medicAdapter
        recyclerViewMed.layoutManager =
            GridLayoutManager(requireContext(), 2, LinearLayoutManager.VERTICAL, false)




        //создаем настояющую дату для ограничения по датам DatePicker
        val dateCalendar: Calendar = GregorianCalendar(TimeZone.getTimeZone("GMT+5"))
        dayMilli = dateCalendar.time.time
        val yearToday: Int = dateCalendar.get(Calendar.YEAR)
        val monthToday: Int = dateCalendar.get(Calendar.MONTH)
        val dayOfMonthToday: Int = dateCalendar.get(Calendar.DAY_OF_MONTH)
        val justDayCalendar: Calendar =
            GregorianCalendar(yearToday, monthToday, dayOfMonthToday)
        var justDayMilli = justDayCalendar.time.time

        idMed = justDayMilli.toString()

        //format date
        val currentDate = Date(dayMilli)
        val dateFormat = SimpleDateFormat("dd MMM YYYY")
        val dayToday = dateFormat.format(currentDate)
        binding.dateTextView.text = dayToday

//MeasureViewModel
        mMedicalViewModel = ViewModelProvider(this).get(MedicalViewModel::class.java)
        mDayMeasureViewModel = ViewModelProvider(this).get(MeasureOfDayViewModel::class.java)

        mMedicalViewModel.readAllData.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { listMedicalInfo ->

                binding.nameMedical.setText(listMedicalInfo.last().nameOfMedicine)
                binding.editFrequencyMedical.setText(listMedicalInfo.last().frequencyMedicine.toString())
                binding.editTextMedicalDoze.setText(listMedicalInfo.last().doseMedicine.toString())
            })


        binding.changeDayButton.setOnClickListener {
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

                    idMed = dateMilli.toString()
                    dayMilli = dateMilli
                    //format date
                    val currentDate = Date(dateMilli)
                    val dateFormat = SimpleDateFormat("dd MMM YYYY")
                    val ddate = dateFormat.format(currentDate)

                    binding.dateTextView.setVisibility(View.VISIBLE)
                    binding.dateTextView.text = ddate.toString()
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

        //Вреия и замер
        binding.addOneMeasureBtn.setOnClickListener {
            //openDialog()
            val builder = AlertDialog.Builder(requireContext())
            val layoutInflater = LayoutInflater.from(requireContext())
            val dialogFragment = LayoutDialogAddFragmentBinding.inflate(layoutInflater)
            dialogFragment.timePicker.is24HourView
            builder.setView(dialogFragment.root)
            builder.setTitle(R.string.measure_alarm_frag)

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

            dialogFragment.cancelBtn.setOnClickListener {
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
                        MedicamentTime(0, timeHour, timeMinute, dayMilli, idMed)
                    medicAdapter.addData(medicamentTime)

                } catch (e: Exception) {
                    val myDialogFragment = AddFragmentDialog("Сперва выберите дату!")
                    val manager = getActivity()?.getSupportFragmentManager()
                    if (manager != null) {
                        myDialogFragment.show(manager, "myDialog")
                    }
                }
            }

            //frequency take medicament
            frequencyMedicament = medicAdapter.itemCount + 1
            binding.editFrequencyMedical.setText(frequencyMedicament.toString())
            counter++

            dialogFragment.cancelBtn.setOnClickListener {
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
                    dayMilli,
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


    }
}





