package com.example.asthmaapp.view.fragments

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.asthmaapp.R
import com.example.asthmaapp.databinding.FragmentUpdateBinding
import com.example.asthmaapp.databinding.LayoutDialogAddFragmentBinding
import com.example.asthmaapp.databinding.LayoutDialogMedicalAddFragmentBinding
import com.example.asthmaapp.model.Measure
import com.example.asthmaapp.model.MedicamentInfo
import com.example.asthmaapp.model.TakeMedicamentTimeEntity
import com.example.asthmaapp.view.adapters.UpdateMeasureAdapter
import com.example.asthmaapp.view.adapters.UpdateMedicamentTimeAdapter
import com.example.asthmaapp.viewmodel.viewModels.MeasurementsPerDayViewModel
import kotlin.properties.Delegates


class UpdateMeasureFragment : BaseFragment<FragmentUpdateBinding>() {

    private val args by navArgs<UpdateMeasureFragmentArgs>()
    private var dayTimeStamp by Delegates.notNull<Long>()
    private var idMedicament by Delegates.notNull<Int>()
    private val measureViewModel: MeasurementsPerDayViewModel by lazy {
        ViewModelProvider(this).get(MeasurementsPerDayViewModel::class.java)
    }

    override fun inflate(inflater: LayoutInflater): FragmentUpdateBinding =
        FragmentUpdateBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        dayTimeStamp = args.currentItemDay.dateTimestamp
        binding.dateTextView.text =
            com.example.asthmaapp.utils.millisecondsToStringDateDayMonthYear(dayTimeStamp)

        binding.nameMedical.setText(args.currentItemDay.takeMedicamentTimeList.map { it.medicamentInfo.name }.toString())
        binding.editTextMedicalDoze.setText(args.currentItemDay.takeMedicamentTimeList.map { it.medicamentInfo.dose }.toString())
        idMedicament = (args.currentItemDay.takeMedicamentTimeList.map { it.medicamentInfo.id }.toString().toInt())


        val deleteMeasureClickListener = object : UpdateMeasureAdapter.OnClickListener {
            override fun onDeleteClick(measure: Measure, position: Int) {
                measureViewModel.deleteMeasure(measure)
                Toast.makeText(
                    requireContext(), "Успешно удалено",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        val updateMeasureAdapter =
            UpdateMeasureAdapter(args.currentItemDay.measureList, deleteMeasureClickListener)
        val recyclerView = binding.recyclerMeasure
        recyclerView.adapter = updateMeasureAdapter
        recyclerView.layoutManager = LinearLayoutManager(
            binding.recyclerMeasure.context,
            LinearLayoutManager.VERTICAL,
            false
        )

        val deleteTakeMedicamentTimeClickListener =
            object : UpdateMedicamentTimeAdapter.OnClickListener {   //
                override fun onDeleteClick(
                    takeMedicamentTime: TakeMedicamentTimeEntity,
                    position: Int
                ) {
                    measureViewModel.deleteTakeMedicamentTime(takeMedicamentTime)
                    Toast.makeText(
                        requireContext(), "Успешно удалено",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        val updateTakeMedicamentTimeAdapter = UpdateMedicamentTimeAdapter(
            args.currentItemDay.takeMedicamentTimeList,
            deleteTakeMedicamentTimeClickListener
        )
        val recyclerViewMedTime = binding.recyclerMed
        recyclerViewMedTime.adapter = updateTakeMedicamentTimeAdapter
        recyclerViewMedTime.layoutManager =
            GridLayoutManager(binding.recyclerMed.context, 2, LinearLayoutManager.HORIZONTAL, false)

        binding.addOneMeasureBtn.setOnClickListener {
            addMeasure(updateMeasureAdapter, dayTimeStamp.toString())
        }

        binding.addTimeTakeMedicamentBtn.setOnClickListener {
            addTakeMedicamentTime(updateTakeMedicamentTimeAdapter)
        }

        binding.saveBtn.setOnClickListener {
            saveMeasurementsPerDay(updateMeasureAdapter, updateTakeMedicamentTimeAdapter)
        }
    }

    private fun addTakeMedicamentTime(
        updateMedicamentTimeAdapter: UpdateMedicamentTimeAdapter
    ) {
        val builder = androidx.appcompat.app.AlertDialog.Builder(requireContext())
        val layoutInflater = LayoutInflater.from(requireContext())
        val dialogFragment = LayoutDialogMedicalAddFragmentBinding.inflate(layoutInflater)
        builder.setView(dialogFragment.root)
        builder.setTitle(R.string.take_medicament)

        val mAlertDialog = builder.show()
        dialogFragment.timePicker.setIs24HourView(true)

        dialogFragment.btnSave.setOnClickListener {
            mAlertDialog.dismiss()
            val timeHour = dialogFragment.timePicker.hour
            val timeMinute = dialogFragment.timePicker.minute
            val medicamentTime =
                TakeMedicamentTimeEntity(
                    0,
                    dayTimeStamp,
                    timeHour,
                    timeMinute,
                    idMedicament,
                )
            updateMedicamentTimeAdapter.addData(medicamentTime)
            measureViewModel.addTakeMedicamentTime(medicamentTime)
        }
        dialogFragment.cancelBtn.setOnClickListener {
            Log.v("myLogs", "AddFragment  dialogFragment.btnCansel.setOnClickListener ")
            mAlertDialog.dismiss()
        }
    }

    private fun addMeasure(updateMeasureAdapter: UpdateMeasureAdapter, idMed: String) {
        val builder = androidx.appcompat.app.AlertDialog.Builder(requireContext())
        val layoutInflater = LayoutInflater.from(requireContext())
        val dialogFragment = LayoutDialogAddFragmentBinding.inflate(layoutInflater)
        builder.setView(dialogFragment.root)
        builder.setTitle(R.string.measure_alarm_frag)

        val alertDialog = builder.show()
        dialogFragment.timePicker.setIs24HourView(true)
        dialogFragment.measureDialog.doAfterTextChanged { it: Editable? ->
            if (dialogFragment.measureDialog.getText().toString().length > 1)
                dialogFragment.btnSave.setEnabled(true)
            else dialogFragment.btnSave.isEnabled = false
        }

        dialogFragment.btnSave.setOnClickListener {
            alertDialog.dismiss()
            val timeHour = dialogFragment.timePicker.hour
            val timeMinute = dialogFragment.timePicker.minute
            val measureWithPeakFlowMeter = dialogFragment.measureDialog.text.toString().toInt()
            val measure = Measure(0, dayTimeStamp, timeHour, timeMinute, measureWithPeakFlowMeter)
            updateMeasureAdapter.addMeasure(measure)
            measureViewModel.addTimeAndMeasure(measure)
        }
        dialogFragment.cancelBtn.setOnClickListener {
            alertDialog.dismiss()
        }
    }

    private fun saveMeasurementsPerDay(
        updateMeasureAdapter: UpdateMeasureAdapter,
        updateMedicamentTimeAdapter: UpdateMedicamentTimeAdapter
    ) {

        val nameMedicament = binding.nameMedical.text.toString()
        val doseMedicament = binding.editTextMedicalDoze.text.toString()
        val medicamentInfo =
            MedicamentInfo(
                idMedicament,
                nameMedicament,
                doseMedicament.toInt()
            )
        measureViewModel.updateMedicamentInfo(medicamentInfo)

        val listMeasure = updateMeasureAdapter.getAllMeasure()
        for (measure in listMeasure)
            measureViewModel.updateMeasure(measure)
//
//        val timeInfo = updateMedicamentTimeAdapter.getData()
//        for (time in timeInfo)
//            measureViewModel.updateMedicalTime(time)
//        measureViewModel.updateMeasure(updateMeasure)
        Toast.makeText(requireContext(), "Updated success", Toast.LENGTH_SHORT).show()
        findNavController().navigate(R.id.action_updateFragment_to_listFragment)
    }
}