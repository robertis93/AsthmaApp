package com.example.asthmaapp.view.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.asthmaapp.databinding.UpdateMeasuresInnerItemBinding
import com.example.asthmaapp.model.Measure
import com.example.asthmaapp.utils.DateUtil.dateTimeStampToSimpleDateFormatHourMinute


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

        holder.binding.timeTextView.text = dateTimeStampToSimpleDateFormatHourMinute(currentItem.dateTimestamp)
        holder.binding.measureText.text = currentItem.measure.toString()

        holder.binding.editImage.setOnClickListener {
            //editTimeMeasure(holder, position, currentItem)
        }
        holder.binding.deleteIcon.setOnClickListener {
            deleteData(timeAndMeasureList[position])
            onClickListener.onDeleteClick(currentItem, position)
        }
    }

//    private fun editTimeMeasure(
//        holder: UpdateMeasureAdapter.UpdateMeasureViewHolder,
//        position: Int,
//        currentItem: Measure
//    ) {
//        val builder =
//            androidx.appcompat.app.AlertDialog.Builder(holder.binding.editImage.context)
//        val layoutInflater = LayoutInflater.from(holder.binding.editImage.context)
//        val dialogFragment = LayoutDialogAddFragmentBinding.inflate(layoutInflater)
//        dialogFragment.timePicker.is24HourView
//        builder.setView(dialogFragment.root)
//        builder.setTitle(R.string.measure_alarm_frag)
//
//        val alertDialog = builder.show()
//        dialogFragment.timePicker.setIs24HourView(true)
//
//        dialogFragment.measureDialog.doAfterTextChanged { it: Editable? ->
//            if (dialogFragment.measureDialog.getText().toString().length > 1)
//                dialogFragment.btnSave.setEnabled(true)
//            else dialogFragment.btnSave.isEnabled = false
//        }
//
//        dialogFragment.timePicker.hour = currentItem.hour
//        dialogFragment.timePicker.minute = currentItem.minute
//        dialogFragment.measureDialog.setText(currentItem.measure.toString())
//
//        dialogFragment.btnSave.setOnClickListener {
//            alertDialog.dismiss()
//            dialogFragment.timePicker.is24HourView
//            val timeHour = dialogFragment.timePicker.hour
//            val timeMinute = dialogFragment.timePicker.minute
//            val measurePeakFlowMeter = dialogFragment.measureDialog.text.toString().toInt()
//
//            holder.binding.hourText.text = com.example.asthmaapp.utils.timeConvert(timeHour)
//            holder.binding.minuteText.text = com.example.asthmaapp.utils.timeConvert(timeMinute)
//            holder.binding.measureText.text = measurePeakFlowMeter.toString()
//            timeAndMeasureList[position].hour = timeHour
//            timeAndMeasureList[position].minute = timeMinute
//            timeAndMeasureList[position].measure = measurePeakFlowMeter
//        }
//
//        dialogFragment.cancelBtn.setOnClickListener {
//            alertDialog.dismiss()
//        }
//    }

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
