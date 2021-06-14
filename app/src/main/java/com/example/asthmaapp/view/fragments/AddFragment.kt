package com.example.asthmaapp.view.fragments

import android.app.DatePickerDialog
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
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
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.properties.Delegates


class AddFragment : Fragment() {
    val args: AddFragmentArgs by navArgs()
    private lateinit var mDayMeasureViewModel: MeasureOfDayViewModel
    private lateinit var mMedicalViewModel: MedicalViewModel
    lateinit var sdf: String
    lateinit var day: String
    var idAddM: Long = -1

    //var idAddM by Delegates.notNull<Int>()

    lateinit var timeAndMeasure: TimeAndMeasure
    var yearMeasure by Delegates.notNull<Int>()
    var mounthMeasure by Delegates.notNull<Int>()
    var dayMeasure by Delegates.notNull<Int>()

    //для времени и замера
    val timeAndMeasureList = mutableListOf<TimeAndMeasure>()
    val medicTimeList = mutableListOf<MedicamentTime>()


    lateinit var binding: FragmentAddBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val idMed = UUID.randomUUID().toString()
        binding.textDate.setVisibility(View.GONE);

        //val observable = Observable.from(arrayOf("one", "two", "three"))

        // определяем слушателя нажатия элемента в списке
        // определяем слушателя нажатия элемента в списке
//        val alarmClickListener: AddFragmentMedicamentTimeAdapter.OnAlarmClickListener =
//            object : AddFragmentMedicamentTimeAdapter.OnAlarmClickListener {   //
//                override fun onDeleteAlarmClick(medicamentTime: MedicamentTime, position: Int) {
//                    medicTimeList.removeAt(position)
//
//                    Log.v("myLogs", "AddFragment  medicTime.remove(medicamentTime) ")
//
//                //mMedicamentTimeViewModel.deleteMedicalTime(medicamentTime)
//                }
//            }

//        val timeMeasureListener: AddFragmentMeasureAdapter.OnMeasureClickListener =
//            object : AddFragmentMeasureAdapter.OnMeasureClickListener {   //
//                override fun onDeleteMeasureClick(timeAndMeasure: TimeAndMeasure, position: Int) {
//                    mTimeAndMeasureViewModel.deleteTimeMeasure(timeAndMeasure)
//                }
//            }

        //recycler
        val addAdapter = AddFragmentMeasureAdapter()
        val recyclerViewAdd = binding.recyclerMeasure
        recyclerViewAdd.adapter = addAdapter
        recyclerViewAdd.layoutManager = LinearLayoutManager(requireContext())


        //recycler for MedicTime
        val medicAdapter = AddFragmentMedicamentTimeAdapter()
        val recyclerViewMed = binding.recyclerMed
        recyclerViewMed.adapter = medicAdapter
        recyclerViewMed.layoutManager = LinearLayoutManager(requireContext())


//MeasureViewModel
        //  mMeasureViewModel = ViewModelProvider(this).get(MeasureOfDayViewModel::class.java)
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
            dialogFragment.timePicker.is24HourView

            //сохраняем в бд замер
            dialogFragment.btnSave.setOnClickListener {
                // TODO: 04.06.2021 exception
                //try {
                mAlertDialog.dismiss()
                dialogFragment.timePicker.is24HourView
                val timeHour = dialogFragment.timePicker.hour
                val timeMinute = dialogFragment.timePicker.minute
                val measurePicf = dialogFragment.measureDialog.text.toString().toInt()
                timeAndMeasure = TimeAndMeasure(0, timeHour, timeMinute, measurePicf, idMed)
                timeAndMeasureList.add(timeAndMeasure)

                // val timeAndMeasureDao = MeasureDataBase.getDataBase(requireContext()).timeAndMeasureDao()
                //val id = timeAndMeasureDao.addTimeAndMeasure(timeAndMeasure)
                // idAddM =  mDayMeasureViewModel.addTimeAndMeasure(timeAndMeasure)

                addAdapter.addData(timeAndMeasure)  //знаю что неправильно, должно быть что то одно,
                // но не получается иначе,
                // тогда теряются значения

                //     mTimeAndMeasureViewModel.addTimeAndMeasure(timeAndMeas)
//            } catch (e: Exception) {
//                val myDialogFragment = AddFragmentDialog("Вы забыли указать значение замера!")
//                val manager = getActivity()?.getSupportFragmentManager()
//                if (manager != null) {
//                    myDialogFragment.show(manager, "myDialog")
//                }
//
//            }
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
            builder.setTitle("Measure")

            //show dialog
            val mAlertDialog = builder.show()
            dialogFragment.timePicker.is24HourView

            //сохраняем в бд замер
            dialogFragment.btnSave.setOnClickListener {
                try {
                    mAlertDialog.dismiss()
                    val timeHour = dialogFragment.timePicker.hour
                    val timeMinute = dialogFragment.timePicker.minute
                    val chekBox = true
                    val medicamentTime = MedicamentTime(0, timeHour, timeMinute, day, chekBox)
                    medicAdapter.addData(medicamentTime)

                } catch (e: Exception) {
                    val myDialogFragment = AddFragmentDialog("Вы забыли указать значение замера!")
                    val manager = getActivity()?.getSupportFragmentManager()
                    if (manager != null) {
                        myDialogFragment.show(manager, "myDialog")
                    }

                }

            }
            dialogFragment.btnCansel.setOnClickListener {
                Log.v("myLogs", "AddFragment  dialogFragment.btnCansel.setOnClickListener ")
                mAlertDialog.dismiss()
            }

        }


//       if (args.dateTime != null){   // пришли из notification
//            //фокус на нужный EditText ля записи замера
//           binding.firstMeasure.setFocusableInTouchMode(true);
//           binding.firstMeasure.requestFocus();
//        }


        binding.saveBtn.setOnClickListener {
            //insertToDataToDataBase()
            // try {
            val nameMedicamentaion = binding.nameMedical.text.toString()
            val doza = binding.editTextMedicalDoze.text.toString().toInt()
            val frequency = binding.editFrequencyMedical.text.toString().toInt()
            //val lisis = addAdapter.getData()
            // val lisis = addAdapter.getDataTest()
//            val idNeed = idAddM.toString().toInt()
            val infoDay = MeasureOfDay(
                idMed,
                day,
                nameMedicamentaion,
                doza,
                frequency
            )
//                for (measure in timeAndMeasureList){
//                    mDayMeasureViewModel.addTimeAndMeasure(measure)
//                }

            //insert time and measure
            val measuresOneDay = addAdapter.getData()
            for (measure in measuresOneDay) {
                // idAddM =  mDayMeasureViewModel.addTimeAndMeasure(measure).toString().toInt()
                mDayMeasureViewModel.addTimeAndMeasure(measure)
            }

            //insert Day
            mDayMeasureViewModel.addMeasure(infoDay)

            //     mDayMeasureViewModel.addTimeAndMeasures(measuresOneDay)
            //  mDayMeasureViewModel.addTimeAndMeasure(lisis)
//                for (medicTime in medicTimeList) {
//                    mMedicamentTimeViewModel.addMedicalTime(medicTime)
//                }
//                for (timeAndMeasure in timeAndMeasureList) {
//                    mDayMeasureViewModel.addTimeAndMeasure(timeAndMeasure)
//                }

//navigate Back
            findNavController().navigate(R.id.action_addFragment_to_listFragment)


//            } catch (e: Exception) {
//                val myDialogFragment = AddFragmentDialog("Вы заполнили не все поля")
//                val manager = getActivity()?.getSupportFragmentManager()
//                if (manager != null) {
//                    myDialogFragment.show(manager, "myDialog")
//                }
//            }
        }

        binding.selectDayBtn.setOnClickListener {
            Log.v("myLogs", "AlarmFragment binding.floatingActionBtnAlarm.setOnClickListener ")
            val cal = Calendar.getInstance()

            val dateSetListener =
                DatePickerDialog.OnDateSetListener { datePicker, year, monthOfYear, dayOfMonth ->
                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, monthOfYear)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                    val myFormat = "dd.MM.yyyy" // mention the format you need
                    sdf = SimpleDateFormat(myFormat, Locale.US).toString()

                    yearMeasure = datePicker.year
                    mounthMeasure = datePicker.month
                    dayMeasure = datePicker.dayOfMonth

                    day = "${dayMeasure}/${mounthMeasure}/${yearMeasure}"
                    val current = LocalDateTime.now()
                    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
                    sdf = current.format(formatter)
                    binding.textDate.setVisibility(View.VISIBLE)
                    binding.textDate.text = day
                    // val formatted = current.format(formatter)

                }

            context?.let { it1 ->
                DatePickerDialog(
                    it1,
                    dateSetListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                ).show()
            }


        }
//        timeAndMeasure.toObservable()
//
//            .subscribeBy(
//
//                onNext = { addAdapter.setData(timeAndMeasure)},
//                onError = { it.printStackTrace() },
//                onComplete = { println("onComplete!") }
//
//            )

    }

    private fun insertToDataToDataBase() {
//        try {
//            val nameMedicamentaion = binding.nameMedical.text.toString()
//            val doza = binding.editTextMedicalDoze.text.toString().toInt()
//            val frequency = binding.editFrequencyMedical.text.toString().toInt()
//
//            val infoDay = MeasureOfDay(
//                0,
//                day,
//                nameMedicamentaion,
//                doza,
//                frequency
//            )
//
////navigate Back
//            findNavController().navigate(R.id.action_addFragment_to_listFragment)
//            mMeasureViewModel.addMeasure(infoDay)
//
//            for (medicTime in medicTimeList) {
//                mMedicamentTimeViewModel.addMedicalTime(medicTime)
//            }
//            for (timeAndMeasure in timeAndMeasureList) {
//                mTimeAndMeasureViewModel.addTimeAndMeasure(timeAndMeasure)
//            }
//
//        } catch (e: Exception) {
//            val myDialogFragment = AddFragmentDialog("Вы заполнили не все поля")
//            val manager = getActivity()?.getSupportFragmentManager()
//            if (manager != null) {
//                myDialogFragment.show(manager, "myDialog")
//            }
//        }

    }

    private fun openDialogMedical() {
//
//        val builder = AlertDialog.Builder(requireContext())
//        val layoutInflater = LayoutInflater.from(requireContext())
//        val dialogFragment = LayoutDialogMedicalAddFragmentBinding.inflate(layoutInflater)
//        builder.setView(dialogFragment.root)
//        builder.setTitle("Measure")
//
//        //show dialog
//        val mAlertDialog = builder.show()
//        dialogFragment.timePicker.is24HourView
//
//        //сохраняем в бд замер
//        dialogFragment.btnSave.setOnClickListener {
//            try {
//                mAlertDialog.dismiss()
//                val timeHour = dialogFragment.timePicker.hour
//                val timeMinute = dialogFragment.timePicker.minute
//                val chekBox = true
//                val medicamentTime = MedicamentTime(0, timeHour, timeMinute, chekBox)
//                mMedicamentTimeViewModel.addMedicalTime(medicamentTime)
//            } catch (e: Exception) {
//                val myDialogFragment = AddFragmentDialog("Вы забыли указать значение замера!")
//                val manager = getActivity()?.getSupportFragmentManager()
//                if (manager != null) {
//                    myDialogFragment.show(manager, "myDialog")
//                }
//
//            }
//
//        }
//        dialogFragment.btnCansel.setOnClickListener {
//            Log.v("myLogs", "AddFragment  dialogFragment.btnCansel.setOnClickListener ")
//            mAlertDialog.dismiss()
//        }
    }


    //диалоговое окно
    private fun openDialog() {

//        val builder = AlertDialog.Builder(requireContext())
//        val layoutInflater = LayoutInflater.from(requireContext())
//        val dialogFragment = LayoutDialogAddFragmentBinding.inflate(layoutInflater)
//        dialogFragment.timePicker.is24HourView
//        builder.setView(dialogFragment.root)
//        builder.setTitle("Measure")
//
//        //show dialog
//        val mAlertDialog = builder.show()
//        dialogFragment.timePicker.is24HourView
//
//        //сохраняем в бд замер
//        dialogFragment.btnSave.setOnClickListener {
//            // TODO: 04.06.2021 exception
//            //try {
//            mAlertDialog.dismiss()
//            dialogFragment.timePicker.is24HourView
//            val timeHour = dialogFragment.timePicker.hour
//            val timeMinute = dialogFragment.timePicker.minute
//            val measurePicf = dialogFragment.measureDialog.text.toString().toInt()
//            val timeAndMeas = TimeAndMeasure(0, timeHour, timeMinute, measurePicf)
//            timeAndMeasureList.add(timeAndMeas)
//            //addAdapter.setData(timeAndMeasure)
//            //     mTimeAndMeasureViewModel.addTimeAndMeasure(timeAndMeas)
////            } catch (e: Exception) {
////                val myDialogFragment = AddFragmentDialog("Вы забыли указать значение замера!")
////                val manager = getActivity()?.getSupportFragmentManager()
////                if (manager != null) {
////                    myDialogFragment.show(manager, "myDialog")
////                }
////
////            }
//        }
//
//        dialogFragment.btnCansel.setOnClickListener {
//            mAlertDialog.dismiss()
//        }
    }
}




