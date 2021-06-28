package com.example.asthmaapp.view.fragments

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
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
import com.example.asthmaapp.model.MeasureOfDay
import com.example.asthmaapp.model.MedicamentTime
import com.example.asthmaapp.model.TimeAndMeasure
import com.example.asthmaapp.utils.AddFragmentDialog
import com.example.asthmaapp.view.adapters.*
import com.example.asthmaapp.viewmodel.viewModels.MeasureOfDayViewModel


class UpdateFragment : Fragment() {

    private val args by navArgs<UpdateFragmentArgs>()

    lateinit var binding: FragmentUpdateBinding

    private lateinit var mMeasureViewModel: MeasureOfDayViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUpdateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mMeasureViewModel = ViewModelProvider(this).get(MeasureOfDayViewModel::class.java)
        binding.textDate.setText(com.example.asthmaapp.utils.longToStringCalendar(args.currentItemDay.day.day))
        binding.nameMedical.setText(args.currentItemDay.day.nameMedicament)
        binding.editTextMedicalDoze.setText(args.currentItemDay.day.doza.toString())
        binding.editFrequencyMedical.setText(args.currentItemDay.day.frequency.toString())

        val idMed = args.currentItemDay.day.id

        val measuresList = args.currentItemDay.measures
        //удаление времени при нажатии кнопки удалить
// определяем слушателя нажатия элемента в списке
        // определяем слушателя нажатия элемента в списке TimeAndMeasure
        val timeAndMeasureClickListener: UpdateFrMeasureAdapter.OnClickListener =
            object : UpdateFrMeasureAdapter.OnClickListener {   //
                override fun onDeleteClick(timeAndMeasure: TimeAndMeasure, position: Int) {

                    mMeasureViewModel.deleteTimeMeasure(timeAndMeasure)
                    Toast.makeText(
                        requireContext(), "Был выбран пункт ",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }


        //recycler Measures
        val adapter = UpdateFrMeasureAdapter(measuresList, timeAndMeasureClickListener)
        val recyclerView = binding.recyclerMeasure
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(
            binding.recyclerMeasure.context,
            2,
            LinearLayoutManager.HORIZONTAL,
            false
        )

        val timeList = args.currentItemDay.medicamentTime


        //удаление времени при нажатии кнопки удалить
// определяем слушателя нажатия элемента в списке
        // определяем слушателя нажатия элемента в списке TimeAndMeasure
        val timeClickListener: UpdatemMedicamentTimeAdapter.OnClickListener =
            object : UpdatemMedicamentTimeAdapter.OnClickListener {   //
                override fun onDeleteClick(medicamentTime: MedicamentTime, position: Int) {
                    mMeasureViewModel.deleteMedicalTime(medicamentTime)
                    Toast.makeText(
                        requireContext(), "Успешно удалено",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        val medTimeAdapter = UpdatemMedicamentTimeAdapter(timeList, timeClickListener)
        val recyclerViewMedTime = binding.recyclerMed
        recyclerViewMedTime.adapter = medTimeAdapter
        recyclerViewMedTime.layoutManager =
            GridLayoutManager(binding.recyclerMed.context, 2, LinearLayoutManager.HORIZONTAL, false)

       val medicamentTimeQuantity =  medTimeAdapter.itemCount


        binding.saveBtn.setOnClickListener {
            val dayMeasure = args.currentItemDay.day.day
            // val dayMeasure = binding.textDate.text.toString().toLong()
            val nameMed = binding.nameMedical.text.toString()
            val dozaMed = binding.editTextMedicalDoze.text.toString()
            val freqMed = binding.editFrequencyMedical.text.toString()
            val updateMeasure =
                MeasureOfDay(
                    args.currentItemDay.day.id,
                    dayMeasure,
                    nameMed,
                    dozaMed.toInt(),
                    freqMed.toInt()
                )

//update
            val timeAndMeasureInfo = adapter.getData()
            for (timeAndMeasure in timeAndMeasureInfo)
                mMeasureViewModel.updateTimeMeasure(timeAndMeasure)
            mMeasureViewModel.updateMeasure(updateMeasure)

            //medicamentTime
            val timeInfo = medTimeAdapter.getData()
            for (time in timeInfo)
                mMeasureViewModel.updateMedicalTime(time)
            mMeasureViewModel.updateMeasure(updateMeasure)
            //mMeasureViewModel.updateTimeMeasure()


            Toast.makeText(requireContext(), "Updated success", Toast.LENGTH_SHORT).show()

            //navigate back
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)// updateItem()
        }
        //addMenu
        setHasOptionsMenu(true)


        binding.addOneMeasureBtn.setOnClickListener {
            val builder = androidx.appcompat.app.AlertDialog.Builder(requireContext())
            val layoutInflater = LayoutInflater.from(requireContext())
            val dialogFragment = LayoutDialogAddFragmentBinding.inflate(layoutInflater)
            dialogFragment.timePicker.is24HourView
            builder.setView(dialogFragment.root)
            builder.setTitle(R.string.measure_alarm_frag)

            //show dialog
            val mAlertDialog = builder.show()
            dialogFragment.timePicker.setIs24HourView(true)

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
                val timeAndMeasure = TimeAndMeasure(0, timeHour, timeMinute, measurePicf, idMed)

                adapter.addData(timeAndMeasure)
                mMeasureViewModel.addTimeAndMeasure(timeAndMeasure)



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


        binding.addOneMedTimeBtn.setOnClickListener {
            val builder = androidx.appcompat.app.AlertDialog.Builder(requireContext())
            val layoutInflater = LayoutInflater.from(requireContext())
            val dialogFragment = LayoutDialogMedicalAddFragmentBinding.inflate(layoutInflater)
            builder.setView(dialogFragment.root)
            builder.setTitle(R.string.take_medicament)

            //show dialog
            val mAlertDialog = builder.show()
            dialogFragment.timePicker.setIs24HourView(true)

            //сохраняем в бд замер
            dialogFragment.btnSave.setOnClickListener {
                mAlertDialog.dismiss()
                val timeHour = dialogFragment.timePicker.hour
                val timeMinute = dialogFragment.timePicker.minute
                val medicamentTime =
                    MedicamentTime(
                        0,
                        timeHour,
                        timeMinute,
                        args.currentItemDay.day.day,
                        idMed
                    )
                medTimeAdapter.addData(medicamentTime)
                mMeasureViewModel.addMedicalTime(medicamentTime)
            }
            dialogFragment.btnCansel.setOnClickListener {
                Log.v("myLogs", "AddFragment  dialogFragment.btnCansel.setOnClickListener ")
                mAlertDialog.dismiss()
            }


        }
    }

}