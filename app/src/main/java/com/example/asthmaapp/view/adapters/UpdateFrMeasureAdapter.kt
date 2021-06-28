package com.example.asthmaapp.view.adapters

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.example.asthmaapp.R
import com.example.asthmaapp.databinding.LayoutDialogAddFragmentBinding
import com.example.asthmaapp.databinding.UpdateMeasuresInnerItemBinding
import com.example.asthmaapp.model.TimeAndMeasure
import com.example.asthmaapp.utils.AddFragmentDialog


class UpdateFrMeasureAdapter(
    timeAndMeasure: List<TimeAndMeasure>,
    var onClickListener: OnClickListener
) :
    RecyclerView.Adapter<UpdateFrMeasureAdapter.MyViewHolder>() {

    interface OnClickListener {
        fun onDeleteClick(timeAndMeasure: TimeAndMeasure, position: Int)
    }

    val timeAndMeasureList = timeAndMeasure.toMutableList()

    class MyViewHolder(val binding: UpdateMeasuresInnerItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = UpdateMeasuresInnerItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = timeAndMeasureList[position]


        holder.binding.hourText.setText(currentItem.hour.toString())
        // holder.binding.hourText.doAfterTextChanged{  }
        holder.binding.minuteText.setText(com.example.asthmaapp.utils.timeConvert(currentItem.minute))
        holder.binding.measureText.setText(currentItem.measure.toString())
        val idMed = currentItem.idMed

        holder.binding.imageEditAlarm.setOnClickListener {
            val builder =
                androidx.appcompat.app.AlertDialog.Builder(holder.binding.imageEditAlarm.context)
            val layoutInflater = LayoutInflater.from(holder.binding.imageEditAlarm.context)
            val dialogFragment = LayoutDialogAddFragmentBinding.inflate(layoutInflater)
            dialogFragment.timePicker.is24HourView
            builder.setView(dialogFragment.root)
            builder.setTitle(R.string.measure_alarm_frag)

            //show dialog
            val mAlertDialog = builder.show()
            dialogFragment.timePicker.setIs24HourView(true)
            //listener EditText
            dialogFragment.measureDialog.doAfterTextChanged { it: Editable? ->
                if (dialogFragment.measureDialog.getText().toString().length > 1)
                    dialogFragment.btnSave.setEnabled(true)
                else dialogFragment.btnSave.isEnabled = false
            }

            dialogFragment.timePicker.hour = currentItem.hour
            dialogFragment.timePicker.minute = currentItem.minute
//            dialogFragment.measureDialog.setText(currentItem.measure)

            //сохраняем в бд замер
            dialogFragment.btnSave.setOnClickListener {
                mAlertDialog.dismiss()
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

            dialogFragment.btnCansel.setOnClickListener {
                mAlertDialog.dismiss()
            }
        }

        holder.binding.imageDeleteAlarm.setOnClickListener {
            deleteData(timeAndMeasureList[position])

            onClickListener?.onDeleteClick(currentItem, position)
        }
    }

    fun deleteData(timeAndMeasure: TimeAndMeasure) {
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

}
