package com.example.asthmaapp.view.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.asthmaapp.R
import com.example.asthmaapp.databinding.LayoutDialogMedicalAddFragmentBinding
import com.example.asthmaapp.databinding.UpdateItemMedtimeBinding
import com.example.asthmaapp.model.TakeMedicamentTime
import com.example.asthmaapp.model.TakeMedicamentTimeEntity
import com.example.asthmaapp.utils.DateUtil.dateTimeStampToSimpleDateFormatHour
import com.example.asthmaapp.utils.DateUtil.timestampToDisplayTime

class UpdateMedicamentTimeAdapter(
    var timesTakeMedicamentTimeEntity: List<TakeMedicamentTime>,
    var onClickListener: OnClickListener
) :
    RecyclerView.Adapter<UpdateMedicamentTimeAdapter.UpdateMedicamentTimeViewHolder>() {

    private val timeList = timesTakeMedicamentTimeEntity.map { it.takeMedicamentTimeEntity }.toMutableList()

    interface OnClickListener {
        fun onDeleteClick(takeMedicamentTimeEntity: TakeMedicamentTimeEntity, position: Int)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UpdateMedicamentTimeViewHolder {
        val binding = UpdateItemMedtimeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return UpdateMedicamentTimeViewHolder(binding)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: UpdateMedicamentTimeViewHolder, position: Int) {
        val currentItem = timeList[position]
        holder.binding.timeTextView.text = timestampToDisplayTime(currentItem.dateTimeStamp)
        holder.binding.editImage.setOnClickListener {
            editTimeAlarmMedicament(holder, position, currentItem)
        }

        holder.binding.deleteImage.setOnClickListener {
            deleteData(currentItem)
            onClickListener.onDeleteClick(currentItem, position)
        }
    }

    private fun editTimeAlarmMedicament(
        holder: UpdateMedicamentTimeViewHolder,
        position: Int,
        currentItem: TakeMedicamentTimeEntity
    ) {
        val builder =
            androidx.appcompat.app.AlertDialog.Builder(holder.binding.editImage.context)
        val layoutInflater = LayoutInflater.from(holder.binding.editImage.context)
        val dialogFragment = LayoutDialogMedicalAddFragmentBinding.inflate(layoutInflater)
        dialogFragment.timePicker.is24HourView
        builder.setView(dialogFragment.root)
        builder.setTitle(R.string.measure_alarm_frag)

        val alertDialog = builder.show()
        dialogFragment.timePicker.setIs24HourView(true)

        dialogFragment.timePicker.hour = dateTimeStampToSimpleDateFormatHour(currentItem.dateTimeStamp).toInt()
        dialogFragment.timePicker.minute = timestampToDisplayTime(currentItem.dateTimeStamp).toInt()

        dialogFragment.btnSave.setOnClickListener {
            alertDialog.dismiss()
            dialogFragment.timePicker.is24HourView
            val timeHour = dialogFragment.timePicker.hour
            val timeMinute = dialogFragment.timePicker.minute

            holder.binding.timeTextView.text = "${timeHour} : ${timeMinute}"
          //  timeList[position].dateTimeStamp = timeHour
          //  timeList[position].minute = timeMinute
        }

        dialogFragment.cancelBtn.setOnClickListener {
            alertDialog.dismiss()
        }
    }

    private fun deleteData(takeMedicamentTimeEntity: TakeMedicamentTimeEntity) {
        timeList.remove(takeMedicamentTimeEntity)
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        return timeList.size
    }

    fun getData(): MutableList<TakeMedicamentTimeEntity> {
        return timeList
    }

    fun addData(takeMedicamentTimeEntity: TakeMedicamentTimeEntity) {
        timeList.add(takeMedicamentTimeEntity)
        notifyDataSetChanged()
    }

    class UpdateMedicamentTimeViewHolder(val binding: UpdateItemMedtimeBinding) :
        RecyclerView.ViewHolder(binding.root)
}
