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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.asthmaapp.databinding.FragmentAddBinding
import com.example.asthmaapp.databinding.LayoutDialogAddFragmentBinding
import com.example.asthmaapp.databinding.LayoutDialogMedicalAddFragmentBinding
import com.example.asthmaapp.model.MedicamentTime
import com.example.asthmaapp.model.TimeAndMeasure
import com.example.asthmaapp.model.models.Alarm
import com.example.asthmaapp.utils.AddFragmentDialog
import com.example.asthmaapp.view.adapters.AddFragmentMeasureAdapter
import com.example.asthmaapp.view.adapters.AddFragmentMedicamentTimeAdapter
import com.example.asthmaapp.view.adapters.AlarmAdapter
import com.example.asthmaapp.viewmodel.viewModels.MeasureOfDayViewModel
import com.example.asthmaapp.viewmodel.viewModels.MedicalViewModel
import com.example.asthmaapp.viewmodel.viewModels.MedicamentTimeViewModel
import com.example.asthmaapp.viewmodel.viewModels.TimeAndMeasureViewModel
import java.lang.Exception
import java.util.*
import kotlin.properties.Delegates


class AddFragment : Fragment() {
    val args: AddFragmentArgs by navArgs()
    private lateinit var mMeasureViewModel: MeasureOfDayViewModel
    private lateinit var mTimeAndMeasureViewModel: TimeAndMeasureViewModel
    private lateinit var mMedicamentTimeViewModel: MedicamentTimeViewModel
    private lateinit var mMedicalViewModel: MedicalViewModel
    lateinit var sdf: String
    var yearMeasure by Delegates.notNull<Int>()
    var mounthMeasure by Delegates.notNull<Int>()
    var dayMeasure by Delegates.notNull<Int>()


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

        // определяем слушателя нажатия элемента в списке
        // определяем слушателя нажатия элемента в списке
        val alarmClickListener: AddFragmentMedicamentTimeAdapter.OnAlarmClickListener =
            object : AddFragmentMedicamentTimeAdapter.OnAlarmClickListener {   //
                override fun onDeleteAlarmClick(medicamentTime: MedicamentTime, position: Int) {
                    mMedicamentTimeViewModel.deleteMedicalTime(medicamentTime)
                }
            }

        val timeMeasureListener: AddFragmentMeasureAdapter.OnMeasureClickListener =
            object : AddFragmentMeasureAdapter.OnMeasureClickListener {   //
                override fun onDeleteMeasureClick(timeAndMeasure: TimeAndMeasure, position: Int) {
                    mTimeAndMeasureViewModel.deleteTimeMeasure(timeAndMeasure)
                }
            }

        //recycler
        val addAdapter = AddFragmentMeasureAdapter(timeMeasureListener)
        val recyclerViewAdd = binding.recyclerMeasure
        recyclerViewAdd.adapter = addAdapter
        recyclerViewAdd.layoutManager = LinearLayoutManager(requireContext())

        //recycler for MedicTime
        val medicAdapter = AddFragmentMedicamentTimeAdapter(alarmClickListener)
        val recyclerViewMed = binding.recyclerMed
        recyclerViewMed.adapter = medicAdapter
        recyclerViewMed.layoutManager = LinearLayoutManager(requireContext())

//MeasureViewModel
        //  mMeasureViewModel = ViewModelProvider(this).get(MeasureOfDayViewModel::class.java)
        mTimeAndMeasureViewModel = ViewModelProvider(this).get(TimeAndMeasureViewModel::class.java)
        mMedicamentTimeViewModel = ViewModelProvider(this).get(MedicamentTimeViewModel::class.java)
        mMedicalViewModel = ViewModelProvider(this).get(MedicalViewModel::class.java)


        mTimeAndMeasureViewModel.readAllData.observe(
            viewLifecycleOwner,
            Observer { timeAndMeasure ->
                addAdapter.setData(timeAndMeasure)
            })

        mMedicamentTimeViewModel.readAllData.observe(
            viewLifecycleOwner,
            Observer { medicamentTime ->
                medicAdapter.setData(medicamentTime)
            })



//        binding.fourLine.visibility = View.GONE
//        binding.fiveLine.visibility = View.GONE
//        binding.sixLine.visibility = View.GONE
//        binding.secMed.visibility = View.GONE
//        binding.check2BoxMed.visibility = View.GONE
//
//        binding.thirdLineBtn.setOnClickListener {
//            binding.thirdLineBtn.visibility = View.GONE
//            binding.fourLine.visibility = View.VISIBLE
//        }
//
//        binding.fourLineBtn.setOnClickListener {
//            binding.fourLineBtn.visibility = View.GONE
//            binding.fiveLine.visibility = View.VISIBLE
//        }
//
//        binding.fiveLineBtn.setOnClickListener {
//            binding.fiveLineBtn.visibility = View.GONE
//            binding.sixLine.visibility = View.VISIBLE
//        }
        binding.addOneMeasureBtn.setOnClickListener {
            openDialog()
        }

        binding.addOneMedBtn.setOnClickListener {
            openDialogMedical()

        }


//       if (args.dateTime != null){   // пришли из notification
//            //фокус на нужный EditText ля записи замера
//           binding.firstMeasure.setFocusableInTouchMode(true);
//           binding.firstMeasure.requestFocus();
//        }


        binding.saveBtn.setOnClickListener {
            insertToDataToDataBase()
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

    }

    private fun insertToDataToDataBase() {
        val day = sdf
        val listOfTimeMeasure: List<TimeAndMeasure>
        val listOfTimeOfMedicament: List<MedicamentTime>
        val nameMedicamentaion = binding.nameMedical.text.toString()
        val doza = binding.editTextMedicalDoze.text.toString()
        val frequency = binding.editFrequencyMedical.text.toString()

        //val infoDay = MeasureOfDay(
//                0,
//                //"25 December 2021",
//                "${dayMeasure} / ${mounthMeasure} / ${yearMeasure} ",
//                firstMeasure?.toInt(),
//                secondMeasure?.toInt(),
//                thirdMeasure?.toInt(),
//                fourMeasure?.toInt(),
//                fifeMeasure?.toInt(),
//                sixMeasure?.toInt(),
//                firstTime,
//                secondTime,
//                thirdTime,
//                fourTime,
//                fiveTime,
//                sixTime
//            )
//
//
////navigate Back
//            findNavController().navigate(R.id.action_addFragment_to_listFragment)
//            mMeasureViewModel.addMeasure(measure)
//        } catch (e: IllegalStateException) {
//            val myDialogFragment = AddFragmentDialog()
//            val manager = getActivity()?.getSupportFragmentManager()
//            if (manager != null) {
//                myDialogFragment.show(manager, "myDialog")
//            }
//        }
//
    }

    private fun openDialogMedical() {

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
                val medicamentTime = MedicamentTime(0, timeHour, timeMinute, chekBox)
                mMedicamentTimeViewModel.addMedicalTime(medicamentTime)
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



    //диалоговое окно
    private fun openDialog() {

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
            try {
                mAlertDialog.dismiss()
                dialogFragment.timePicker.is24HourView
                val timeHour = dialogFragment.timePicker.hour
                val timeMinute = dialogFragment.timePicker.minute
                val measurePicf = dialogFragment.measureDialog.text.toString().toInt()
                val timeAndMeas = TimeAndMeasure(0, timeHour, timeMinute, measurePicf)
                mTimeAndMeasureViewModel.addTimeAndMeasure(timeAndMeas)
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


//    private fun insertDataToDataBase() {
//        try {
//
//
//            val firstTime = binding.firstTime?.text?.toString()
//            val secondTime = binding.secondTime?.text?.toString()
//            val thirdTime = binding.thirdTime?.text?.toString()
//            val fourTime = binding.fourTime?.text?.toString()
//            val fiveTime = binding.fiveTime?.text?.toString()
//            val sixTime = binding.sixTime?.text?.toString()
//            var firstMeasure = binding.firstMeasure?.text?.toString()
//            if (firstMeasure == "") {
//                firstMeasure = "0"
//            }
//            var secondMeasure = binding.secondMeasure?.text?.toString()
//            if (secondMeasure == "") {
//                secondMeasure = "0"
//            }
//            var thirdMeasure = binding.thirdMeasure?.text.toString()
//            if (thirdMeasure == "") {
//                thirdMeasure = "0"
//            }
//            var fourMeasure = binding.fourMeasure.text?.toString()
//            if (fourMeasure == "") {
//                fourMeasure = "0"
//            }
//            var fifeMeasure = binding.fiveMeasure.text?.toString()
//            if (fifeMeasure == "") {
//                fifeMeasure = "0"
//            }
//            var sixMeasure = binding.sixMeasure.text?.toString()
//            if (sixMeasure == "") {
//                sixMeasure = "0"
//            }
//
//
//            val measure = MeasureOfDay(
//                0,
//                //"25 December 2021",
//                "${dayMeasure} / ${mounthMeasure} / ${yearMeasure} ",
//                firstMeasure?.toInt(),
//                secondMeasure?.toInt(),
//                thirdMeasure?.toInt(),
//                fourMeasure?.toInt(),
//                fifeMeasure?.toInt(),
//                sixMeasure?.toInt(),
//                firstTime,
//                secondTime,
//                thirdTime,
//                fourTime,
//                fiveTime,
//                sixTime
//            )
//
//
////navigate Back
//            findNavController().navigate(R.id.action_addFragment_to_listFragment)
//            mMeasureViewModel.addMeasure(measure)
//        } catch (e: IllegalStateException) {
//            val myDialogFragment = AddFragmentDialog()
//            val manager = getActivity()?.getSupportFragmentManager()
//            if (manager != null) {
//                myDialogFragment.show(manager, "myDialog")
//            }
//        }
//

        //}
    }
}




