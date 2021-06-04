package com.example.asthmaapp.view.fragments

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.asthmaapp.R
import com.example.asthmaapp.model.MeasureOfDay
import com.example.asthmaapp.viewmodel.viewModels.MeasureOfDayViewModel
import com.example.asthmaapp.databinding.FragmentAddBinding
import com.example.asthmaapp.utils.AddFragmentDialog
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates


class AddFragment : Fragment() {
    val args: AddFragmentArgs by navArgs()
    private lateinit var mMeasureViewModel: MeasureOfDayViewModel
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
        binding.fourLine.visibility = View.GONE
        binding.fiveLine.visibility = View.GONE
        binding.sixLine.visibility = View.GONE
        binding.secMed.visibility = View.GONE
        binding.check2BoxMed.visibility = View.GONE

        binding.thirdLineBtn.setOnClickListener {
            binding.thirdLineBtn.visibility = View.GONE
            binding.fourLine.visibility = View.VISIBLE
        }

        binding.fourLineBtn.setOnClickListener {
            binding.fourLineBtn.visibility = View.GONE
            binding.fiveLine.visibility = View.VISIBLE
        }

        binding.fiveLineBtn.setOnClickListener {
            binding.fiveLineBtn.visibility = View.GONE
            binding.sixLine.visibility = View.VISIBLE
        }

        binding.addOneMedBtn.setOnClickListener {
            binding.addOneMedBtn.visibility = View.GONE
            binding.secMed.visibility = View.VISIBLE
            binding.check2BoxMed.visibility = View.VISIBLE
        }


//       if (args.dateTime != null){   // пришли из notification
//            //фокус на нужный EditText ля записи замера
//           binding.firstMeasure.setFocusableInTouchMode(true);
//           binding.firstMeasure.requestFocus();
//        }

        mMeasureViewModel = ViewModelProvider(this).get(MeasureOfDayViewModel::class.java)

        binding.button2.setOnClickListener {
            insertDataToDataBase()
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

    private fun insertDataToDataBase() {
        try {


            val firstTime = binding.firstTime?.text?.toString()
            val secondTime = binding.secondTime?.text?.toString()
            val thirdTime = binding.thirdTime?.text?.toString()
            val fourTime = binding.fourTime?.text?.toString()
            val fiveTime = binding.fiveTime?.text?.toString()
            val sixTime = binding.sixTime?.text?.toString()
            var firstMeasure = binding.firstMeasure?.text?.toString()
            if (firstMeasure == "") {
                firstMeasure = "0"
            }
            var secondMeasure = binding.secondMeasure?.text?.toString()
            if (secondMeasure == "") {
                secondMeasure = "0"
            }
            var thirdMeasure = binding.thirdMeasure?.text.toString()
            if (thirdMeasure == "") {
                thirdMeasure = "0"
            }
            var fourMeasure = binding.fourMeasure.text?.toString()
            if (fourMeasure == "") {
                fourMeasure = "0"
            }
            var fifeMeasure = binding.fiveMeasure.text?.toString()
            if (fifeMeasure == "") {
                fifeMeasure = "0"
            }
            var sixMeasure = binding.sixMeasure.text?.toString()
            if (sixMeasure == "") {
                sixMeasure = "0"
            }


            val measure = MeasureOfDay(
                0,
                //"25 December 2021",
                "${dayMeasure} / ${mounthMeasure} / ${yearMeasure} ",
                firstMeasure?.toInt(),
                secondMeasure?.toInt(),
                thirdMeasure?.toInt(),
                fourMeasure?.toInt(),
                fifeMeasure?.toInt(),
                sixMeasure?.toInt(),
                firstTime,
                secondTime,
                thirdTime,
                fourTime,
                fiveTime,
                sixTime
            )


//navigate Back
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
            mMeasureViewModel.addMeasure(measure)
        } catch (e: IllegalStateException) {
            val myDialogFragment = AddFragmentDialog()
            val manager = getActivity()?.getSupportFragmentManager()
            if (manager != null) {
                myDialogFragment.show(manager, "myDialog")
            }
        }


    }
}



