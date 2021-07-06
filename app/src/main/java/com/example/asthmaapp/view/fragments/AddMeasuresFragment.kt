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
import java.text.SimpleDateFormat
import java.util.*

class AddMeasuresFragment : Fragment() {
    lateinit var binding: FragmentAddBinding
    private lateinit var viewModel: MeasureOfDayViewModel


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
        viewModel = ViewModelProvider(this).get(MeasureOfDayViewModel::class.java)
        val recyclerViewAdd = binding.recyclerMeasure
        recyclerViewAdd.layoutManager =
            GridLayoutManager(requireContext(), 2, LinearLayoutManager.VERTICAL, false)
        val timeMeasureAdapterListener = object : AddMeasureAdapter.Listener{
            override fun onDeleteClick(timeAndMeasure: TimeAndMeasure) {
            }

        }
        viewModel.timeMeasuresLiveData.observe(viewLifecycleOwner){
            val addMeasureAdapter = AddMeasureAdapter(it, timeMeasureAdapterListener)
            recyclerViewAdd.adapter = addMeasureAdapter
        }

        val deleteClickListener: AddMedicamentTimeAdapter.OnDeleteClickListener =
            object : AddMedicamentTimeAdapter.OnDeleteClickListener {   //
                override fun onDeleteAlarmClick(medicamentTime: MedicamentTime) {

                }
            }

        val recyclerViewMed = binding.recyclerMed
        recyclerViewMed.layoutManager =
            GridLayoutManager(requireContext(), 2, LinearLayoutManager.VERTICAL, false)
        viewModel.timeMedicineLiveData.observe(viewLifecycleOwner){
            val addMedicamentTimeAdapter = AddMedicamentTimeAdapter(it, deleteClickListener)
            recyclerViewMed.adapter = addMedicamentTimeAdapter
        }

        viewModel.measureOfDayLiveData.observe(
            viewLifecycleOwner,
            { measureOfDay ->
                // TODO: сделать, т.к. вылетает если ничего нету
                binding.nameMedical.setText(measureOfDay.nameMedicament)
                binding.editFrequencyMedical.setText(measureOfDay.frequency.toString())
                binding.editTextMedicalDoze.setText(measureOfDay.doza.toString())
                val currentDate = Date(measureOfDay.day)
                val dateFormat = SimpleDateFormat("dd MMM YYYY")
                val ddate = dateFormat.format(currentDate)
                binding.dateTextView.text = ddate.toString()
            }
        )

        viewModel.timeMedicineLiveData.observe(viewLifecycleOwner){
            binding.editFrequencyMedicalLayout.editText?.setText(it.size.toString())
        }

        binding.changeDayButton.setOnClickListener {
            changeDay()
        }

        // Время и замер
        binding.addOneMeasureBtn.setOnClickListener {
            addTimeOfMeasure()
        }

/*        binding.addOneMedBtn.setOnClickListener {
            addTimeTakeMedicament(addMedicamentTimeAdapter)
        }*/

        binding.saveBtn.setOnClickListener {
            viewModel.save()
            findNavController().popBackStack()
        }

        /*binding.nameMedicalLayout.editText?.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                TODO("Not yet implemented")
            }

            override fun afterTextChanged(s: Editable?) {
                TODO("Not yet implemented")
            }
        })*/
    }



    /*        val myDialogFragment = AddFragmentDialog("Вы заполнили не все поля")
            val fragmentManager = requireActivity().supportFragmentManager
            myDialogFragment.show(fragmentManager, "myDialog")
*/

    /*private fun addTimeTakeMedicament(addMedicamentTimeAdapter: AddMedicamentTimeAdapter) {
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
    }*/

    private fun addTimeOfMeasure() {
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

        dialogFragment.btnSave.setOnClickListener {
            mAlertDialog.dismiss()
            dialogFragment.timePicker.is24HourView
            val timeHour = dialogFragment.timePicker.hour
            val timeMinute = dialogFragment.timePicker.minute
            val measurePicf = dialogFragment.measureDialog.text.toString().toInt()
            viewModel.addTimeAndMeasure(timeHour, timeMinute, measurePicf)
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

                val yearMeasure = datePicker.year
                val monthMeasure = datePicker.month
                val dayMeasure = datePicker.dayOfMonth

                val dateCalendar: Calendar =
                    GregorianCalendar(yearMeasure, monthMeasure, dayMeasure)
                val dateMilli = dateCalendar.time.time
                viewModel.changeDate(dateMilli)

            }

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            dateSetListener,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        )

        val dateCalendar: Calendar = GregorianCalendar(TimeZone.getTimeZone("GMT+5"))
        val dayMilliseconds = dateCalendar.time.time
        datePickerDialog.datePicker.maxDate = dayMilliseconds
        datePickerDialog.show()
    }
}
