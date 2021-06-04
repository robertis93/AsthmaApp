package com.example.asthmaapp.utils

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.asthmaapp.R
import com.example.asthmaapp.databinding.FragmentAddBinding
import com.example.asthmaapp.databinding.LayoutDialogAddFragmentBinding

class AddMeasureDialog : DialogFragment() {

    private lateinit var binding: LayoutDialogAddFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.timePicker.is24HourView
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = LayoutDialogAddFragmentBinding.inflate(LayoutInflater.from(requireContext()))
        binding.timePicker.setIs24HourView(true)
        return activity?.let {
            val builder = AlertDialog.Builder(it)
                .setView(binding.root)
                .setTitle("Measure")

            //show dialog
            val mAlertDialog = builder.show()

            binding.btnSave.setOnClickListener {
                //mAlertDialog.dismiss()
                val time = binding.timePicker
                val measure = binding.measureDialog.text

            }

            binding.btnCansel.setOnClickListener {
                Log.v("myLogs", "AlarmFragment binding.floatingActionBtnAlarm.setOnClickListener ")
                mAlertDialog.dismiss() //not working why??

            }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
        }


    }




//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        binding = LayoutDialogAddFragmentBinding.inflate(inflater, container, false)
//        val mDialogView =
//            LayoutInflater.from(requireContext())
//                .inflate(R.layout.layout_dialog_add_fragment, null)
//
//        val builder = AlertDialog.Builder(requireContext())
//            .setView(mDialogView)
//            .setTitle("Measure")
//
//        //show dialog
//        val mAlertDialog = builder.show()
//
//        binding.btnSave.setOnClickListener {
//            mAlertDialog.dismiss()
//            val time = binding.timePicker
//            val measure = binding.measureDialog.text
//
//        }
//
//        binding.btnCansel.setOnClickListener {
//            mAlertDialog.dismiss()
//        }
//        return binding.root
//    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//
//        val mDialogView =
//            LayoutInflater.from(requireContext())
//                .inflate(R.layout.layout_dialog_add_fragment, null)
//
//        val builder = AlertDialog.Builder(requireContext())
//            .setView(mDialogView)
//            .setTitle("Measure")
//
//        //show dialog
//        val mAlertDialog = builder.show()
//
//        binding.btnSave.setOnClickListener {
//            mAlertDialog.dismiss()
//            val time = binding.timePicker
//            val measure = binding.measureDialog.text
//
//        }
//
//        binding.btnCansel.setOnClickListener {
//            mAlertDialog.dismiss()
//        }
//
//
//
//    }

