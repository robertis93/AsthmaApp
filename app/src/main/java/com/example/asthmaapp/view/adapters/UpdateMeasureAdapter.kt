package com.example.asthmaapp.view.adapters

import android.annotation.SuppressLint
import android.text.Editable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.example.asthmaapp.R
import com.example.asthmaapp.databinding.LayoutDialogAddFragmentBinding
import com.example.asthmaapp.databinding.UpdateMeasuresInnerItemBinding
import com.example.asthmaapp.model.TimeAndMeasure


class UpdateMeasureAdapter(
    timeAndMeasure: List<TimeAndMeasure>,
    var onClickListener: OnClickListener
) :
    RecyclerView.Adapter<UpdateMeasureAdapter.UpdateMeasureViewHolder>() {

    private val timeAndMeasureList = timeAndMeasure.toMutableList()

    interface OnClickListener {
        fun onDeleteClick(timeAndMeasure: TimeAndMeasure, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpdateMeasureViewHolder {
        val binding = UpdateMeasuresInnerItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return UpdateMeasureViewHolder(binding)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: UpdateMeasureViewHolder, position: Int) {
        val currentItem = timeAndMeasureList[position]

        holder.binding.hourText.text = currentItem.hour.toString()
        holder.binding.minuteText.text = com.example.asthmaapp.utils.timeConvert(currentItem.minute)
        holder.binding.measureText.text = currentItem.measure.toString()

        holder.binding.editAlarmImage.setOnClickListener {
            editTimeMeasure(holder, position, currentItem)
        }
        holder.binding.deleteIcon.setOnClickListener {
            deleteData(timeAndMeasureList[position])
            onClickListener.onDeleteClick(currentItem, position)
        }
    }

    private fun editTimeMeasure(
        holder: UpdateMeasureAdapter.UpdateMeasureViewHolder,
        position: Int,
        currentItem: TimeAndMeasure
    ) {
        val builder =
            androidx.appcompat.app.AlertDialog.Builder(holder.binding.editAlarmImage.context)
        val layoutInflater = LayoutInflater.from(holder.binding.editAlarmImage.context)
        val dialogFragment = LayoutDialogAddFragmentBinding.inflate(layoutInflater)
        dialogFragment.timePicker.is24HourView
        builder.setView(dialogFragment.root)
        builder.setTitle(R.string.measure_alarm_frag)

        val alertDialog = builder.show()
        dialogFragment.timePicker.setIs24HourView(true)

        dialogFragment.measureDialog.doAfterTextChanged { it: Editable? ->
            if (dialogFragment.measureDialog.getText().toString().length > 1)
                dialogFragment.btnSave.setEnabled(true)
            else dialogFragment.btnSave.isEnabled = false
        }

        dialogFragment.timePicker.hour = currentItem.hour
        dialogFragment.timePicker.minute = currentItem.minute
        dialogFragment.measureDialog.setText(currentItem.measure.toString())

        dialogFragment.btnSave.setOnClickListener {
            alertDialog.dismiss()
            dialogFragment.timePicker.is24HourView
            val timeHour = dialogFragment.timePicker.hour
            val timeMinute = dialogFragment.timePicker.minute
            val measurePicf = dialogFragment.measureDialog.text.toString().toInt()

            holder.binding.hourText.text = com.example.asthmaapp.utils.timeConvert(timeHour)
            holder.binding.minuteText.text = com.example.asthmaapp.utils.timeConvert(timeMinute)
            holder.binding.measureText.text = measurePicf.toString()
            timeAndMeasureList[position].hour = timeHour
            timeAndMeasureList[position].minute = timeMinute
            timeAndMeasureList[position].measure = measurePicf
        }

        dialogFragment.cancelBtn.setOnClickListener {
            alertDialog.dismiss()
        }
    }

    private fun deleteData(timeAndMeasure: TimeAndMeasure) {
        timeAndMeasureList.remove(timeAndMeasure)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return timeAndMeasureList.size
    }

    fun getData(): MutableList<TimeAndMeasure> {
        return timeAndMeasureList

    }

    fun addData(timeAndMeasure: TimeAndMeasure) {
        timeAndMeasureList.add(timeAndMeasure)
        notifyDataSetChanged()
    }

    class UpdateMeasureViewHolder(val binding: UpdateMeasuresInnerItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}
