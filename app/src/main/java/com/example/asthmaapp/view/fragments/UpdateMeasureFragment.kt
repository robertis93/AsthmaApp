package com.example.asthmaapp.view.fragments

//class UpdateMeasureFragment : BaseFragment<FragmentUpdateBinding>() {
//
//    private val args by navArgs<UpdateMeasureFragmentArgs>()
//    private val measureViewModel: MeasureOfDayViewModel by lazy {
//        ViewModelProvider(this).get(MeasureOfDayViewModel::class.java)
//    }
//
//    override fun inflate(inflater: LayoutInflater): FragmentUpdateBinding =
//        FragmentUpdateBinding.inflate(inflater)
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        setHasOptionsMenu(true)
//
//        binding.dateTextView.text = com.example.asthmaapp.utils.longToStringCalendar(args.currentItemDay.day.day)
//        binding.nameMedical.setText(args.currentItemDay.day.nameMedicament)
//        binding.editTextMedicalDoze.setText(args.currentItemDay.day.doseMedicament.toString())
//
//        val idMed = args.currentItemDay.day.id
//        val measuresList = args.currentItemDay.measures
//        val timeList = args.currentItemDay.medicamentTime
//
//        val timeAndMeasureClickListener = object : UpdateMeasureAdapter.OnClickListener {
//            override fun onDeleteClick(timeMakeMeasure: TimeMakeMeasure, position: Int) {
//                measureViewModel.deleteTimeMeasure(timeMakeMeasure)
//                Toast.makeText(
//                    requireContext(), "Успешно удалено",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        }
//
//        val updateMeasureAdapter = UpdateMeasureAdapter(measuresList, timeAndMeasureClickListener)
//        val recyclerView = binding.recyclerMeasure
//        recyclerView.adapter = updateMeasureAdapter
//        recyclerView.layoutManager = LinearLayoutManager(
//            binding.recyclerMeasure.context,
//            LinearLayoutManager.VERTICAL,
//            false
//        )
//
//        val timeClickListener = object : UpdateMedicamentTimeAdapter.OnClickListener {   //
//            override fun onDeleteClick(timeTakeMedicament: TimeTakeMedicament, position: Int) {
//                measureViewModel.deleteMedicalTime(timeTakeMedicament)
//                Toast.makeText(
//                    requireContext(), "Успешно удалено",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        }
//
//        val updateMedicamentTimeAdapter = UpdateMedicamentTimeAdapter(timeList, timeClickListener)
//        val recyclerViewMedTime = binding.recyclerMed
//        recyclerViewMedTime.adapter = updateMedicamentTimeAdapter
//        recyclerViewMedTime.layoutManager =
//            GridLayoutManager(binding.recyclerMed.context, 2, LinearLayoutManager.HORIZONTAL, false)
//
//        binding.addOneMeasureBtn.setOnClickListener {
//            addMeasureWithTime(updateMeasureAdapter, idMed)
//        }
//
//        binding.addTimeTakeMedicamentBtn.setOnClickListener {
//            addTimeTakeMedicament(updateMedicamentTimeAdapter, idMed)
//        }
//
//        binding.saveBtn.setOnClickListener {
//            saveMeasurementsPerDay(updateMeasureAdapter, updateMedicamentTimeAdapter)
//        }
//    }
//
//    private fun addTimeTakeMedicament(
//        updateMedicamentTimeAdapter: UpdateMedicamentTimeAdapter,
//        idMed: String
//    ) {
//        val builder = androidx.appcompat.app.AlertDialog.Builder(requireContext())
//        val layoutInflater = LayoutInflater.from(requireContext())
//        val dialogFragment = LayoutDialogMedicalAddFragmentBinding.inflate(layoutInflater)
//        builder.setView(dialogFragment.root)
//        builder.setTitle(R.string.take_medicament)
//
//        val mAlertDialog = builder.show()
//        dialogFragment.timePicker.setIs24HourView(true)
//
//        dialogFragment.btnSave.setOnClickListener {
//            mAlertDialog.dismiss()
//            val timeHour = dialogFragment.timePicker.hour
//            val timeMinute = dialogFragment.timePicker.minute
//            val medicamentTime =
//                TimeTakeMedicament(
//                    0,
//                    timeHour,
//                    timeMinute,
//                    args.currentItemDay.day.day,
//                    idMed
//                )
//            updateMedicamentTimeAdapter.addData(medicamentTime)
//            measureViewModel.addMedicalTime(medicamentTime)
//
//
//        }
//        dialogFragment.cancelBtn.setOnClickListener {
//            Log.v("myLogs", "AddFragment  dialogFragment.btnCansel.setOnClickListener ")
//            mAlertDialog.dismiss()
//        }
//    }
//
//    private fun addMeasureWithTime(updateMeasureAdapter: UpdateMeasureAdapter, idMed: String) {
//        val builder = androidx.appcompat.app.AlertDialog.Builder(requireContext())
//        val layoutInflater = LayoutInflater.from(requireContext())
//        val dialogFragment = LayoutDialogAddFragmentBinding.inflate(layoutInflater)
//        builder.setView(dialogFragment.root)
//        builder.setTitle(R.string.measure_alarm_frag)
//
//        val alertDialog = builder.show()
//        dialogFragment.timePicker.setIs24HourView(true)
//        dialogFragment.measureDialog.doAfterTextChanged { it: Editable? ->
//            if (dialogFragment.measureDialog.getText().toString().length > 1)
//                dialogFragment.btnSave.setEnabled(true)
//            else dialogFragment.btnSave.isEnabled = false
//        }
//
//        dialogFragment.btnSave.setOnClickListener {
//            try {
//                alertDialog.dismiss()
//                val timeHour = dialogFragment.timePicker.hour
//                val timeMinute = dialogFragment.timePicker.minute
//                val measurePicf = dialogFragment.measureDialog.text.toString().toInt()
//                val timeAndMeasure = TimeMakeMeasure(0, idMed, timeHour, timeMinute, measurePicf)
//                updateMeasureAdapter.addData(timeAndMeasure)
//                measureViewModel.addTimeAndMeasure(timeAndMeasure)
//
//            } catch (e: Exception) {
//                val myDialogFragment = AddMeasureFragmentDialog(R.string.you_dont_write_all_information)
//                val manager = getActivity()?.getSupportFragmentManager()
//                if (manager != null) {
//                    myDialogFragment.show(manager, "myDialog")
//                }
//            }
//        }
//        dialogFragment.cancelBtn.setOnClickListener {
//            alertDialog.dismiss()
//        }
//    }
//
//    private fun saveMeasurementsPerDay(
//        updateMeasureAdapter: UpdateMeasureAdapter,
//        updateMedicamentTimeAdapter: UpdateMedicamentTimeAdapter
//    ) {
//        val dayMeasure = args.currentItemDay.day.day
//        val nameMedicament = binding.nameMedical.text.toString()
//        val doseMedicament = binding.editTextMedicalDoze.text.toString()
//        val updateMeasure =
//            MeasureOfDay(
//                args.currentItemDay.day.id,
//                dayMeasure,
//                nameMedicament,
//                doseMedicament.toInt()
//            )
//        val timeAndMeasureInfo = updateMeasureAdapter.getData()
//        for (timeAndMeasure in timeAndMeasureInfo)
//            measureViewModel.updateTimeMeasure(timeAndMeasure)
//        measureViewModel.updateMeasure(updateMeasure)
//
//        val timeInfo = updateMedicamentTimeAdapter.getData()
//        for (time in timeInfo)
//            measureViewModel.updateMedicalTime(time)
//        measureViewModel.updateMeasure(updateMeasure)
//        Toast.makeText(requireContext(), "Updated success", Toast.LENGTH_SHORT).show()
//        findNavController().navigate(R.id.action_updateFragment_to_listFragment)
//    }
//}