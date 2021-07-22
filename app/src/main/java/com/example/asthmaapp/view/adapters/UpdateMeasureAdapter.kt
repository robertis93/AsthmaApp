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
import com.example.asthmaapp.model.Measure
import com.example.asthmaapp.utils.DateUtil.dayTimeStampWithNewTime
import com.example.asthmaapp.utils.DateUtil.timeCorrectDisplay
import com.example.asthmaapp.utils.DateUtil.timestampToDisplayHour
import com.example.asthmaapp.utils.DateUtil.timestampToDisplayMinute
import com.example.asthmaapp.utils.DateUtil.timestampToDisplayTime


class UpdateMeasureAdapter(
    measure: List<Measure>,
    var onClickListener: OnClickListener
) :
    RecyclerView.Adapter<UpdateMeasureAdapter.UpdateMeasureViewHolder>() {

    private val timeAndMeasureList = measure.toMutableList()

    interface OnClickListener {
        fun onDeleteClick(measure: Measure, position: Int)
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

        holder.binding.timeTextView.text = timestampToDisplayTime(currentItem.dateTimestamp)
        holder.binding.measureText.text = currentItem.value.toString()

        holder.binding.editImage.setOnClickListener {
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
        currentItem: Measure
    ) {
        val builder =
            androidx.appcompat.app.AlertDialog.Builder(holder.binding.editImage.context)
        val layoutInflater = LayoutInflater.from(holder.binding.editImage.context)
        val dialogFragment = LayoutDialogAddFragmentBinding.inflate(layoutInflater)
        dialogFragment.timePicker.is24HourView
        builder.setView(dialogFragment.root)
        builder.setTitle(R.string.measure_alarm_frag)

        val alertDialog = builder.show()
        dialogFragment.timePicker.setIs24HourView(true)

        dialogFragment.measureDialog.doAfterTextChanged { it: Editable? ->
            dialogFragment.btnSave.isEnabled = dialogFragment.measureDialog.getText().toString().length > 1
        }

        dialogFragment.timePicker.hour = timestampToDisplayHour(currentItem.dateTimestamp).toInt()
        dialogFragment.timePicker.minute = timestampToDisplayMinute(currentItem.dateTimestamp).toInt()
        dialogFragment.measureDialog.setText(currentItem.value.toString())

        dialogFragment.btnSave.setOnClickListener {
            alertDialog.dismiss()
            dialogFragment.timePicker.is24HourView
            val timeHour = dialogFragment.timePicker.hour
            val timeMinute = dialogFragment.timePicker.minute
            val measurePeakFlowMeter = dialogFragment.measureDialog.text.toString().toInt()

            holder.binding.timeTextView.text = " ${ timeCorrectDisplay(timeHour)} : ${timeCorrectDisplay(timeMinute)} "
            holder.binding.measureText.text = measurePeakFlowMeter.toString()
            timeAndMeasureList[position].dateTimestamp = dayTimeStampWithNewTime(currentItem.dateTimestamp,timeHour, timeMinute)
            timeAndMeasureList[position].value = measurePeakFlowMeter
        }

        dialogFragment.cancelBtn.setOnClickListener {
            alertDialog.dismiss()
        }
    }

    private fun deleteData(measure: Measure) {
        timeAndMeasureList.remove(measure)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return timeAndMeasureList.size
    }

    fun getAllMeasure(): MutableList<Measure> {
        return timeAndMeasureList

    }

    fun addMeasure(measure: Measure) {
        timeAndMeasureList.add(measure)
        notifyDataSetChanged()
    }

    class UpdateMeasureViewHolder(val binding: UpdateMeasuresInnerItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}
