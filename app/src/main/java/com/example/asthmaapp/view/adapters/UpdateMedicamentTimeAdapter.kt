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

class UpdateMedicamentTimeAdapter(
    var timesTakeMedicamentTimeEntity: List<TakeMedicamentTime>,
    var onClickListener: OnClickListener
) :
    RecyclerView.Adapter<UpdateMedicamentTimeAdapter.UpdateMedicamentTimeViewHolder>() {

    private val timeList = timesTakeMedicamentTimeEntity.toMutableList()
    lateinit var timesTakeMedicamentTime: MutableList<TakeMedicamentTimeEntity>


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
        holder.binding.hourText.text = com.example.asthmaapp.utils.timeConvert(currentItem.takeMedicamentTime.timeHour)
        holder.binding.minuteText.text = com.example.asthmaapp.utils.timeConvert(currentItem.takeMedicamentTime.timeMinute)

        holder.binding.editAlarmImage.setOnClickListener {
            editTimeAlarmMedicament(holder, position, currentItem.takeMedicamentTime)
        }

        holder.binding.deleteImage.setOnClickListener {
            deleteData(currentItem.takeMedicamentTime)
            onClickListener?.onDeleteClick(currentItem.takeMedicamentTime, position)
        }
    }

    private fun editTimeAlarmMedicament(
        holder: UpdateMedicamentTimeViewHolder,
        position: Int,
        currentItem: TakeMedicamentTimeEntity
    ) {
        val builder =
            androidx.appcompat.app.AlertDialog.Builder(holder.binding.editAlarmImage.context)
        val layoutInflater = LayoutInflater.from(holder.binding.editAlarmImage.context)
        val dialogFragment = LayoutDialogMedicalAddFragmentBinding.inflate(layoutInflater)
        dialogFragment.timePicker.is24HourView
        builder.setView(dialogFragment.root)
        builder.setTitle(R.string.measure_alarm_frag)

        val alertDialog = builder.show()
        dialogFragment.timePicker.setIs24HourView(true)

        dialogFragment.timePicker.hour = currentItem.timeHour
        dialogFragment.timePicker.minute = currentItem.timeMinute

        dialogFragment.btnSave.setOnClickListener {
            alertDialog.dismiss()
            dialogFragment.timePicker.is24HourView
            val timeHour = dialogFragment.timePicker.hour
            val timeMinute = dialogFragment.timePicker.minute

            holder.binding.hourText.text = com.example.asthmaapp.utils.timeConvert(timeHour)
            holder.binding.minuteText.text = com.example.asthmaapp.utils.timeConvert(timeMinute)
            timeList[position].takeMedicamentTime.timeHour = timeHour
            timeList[position].takeMedicamentTime.timeMinute = timeMinute
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


    fun getData(): MutableList<TakeMedicamentTime> {
        return timeList
    }

    fun addData(takeMedicamentTimeEntity: TakeMedicamentTimeEntity) {
        timesTakeMedicamentTime.add(takeMedicamentTimeEntity)
        notifyDataSetChanged()
    }

    class UpdateMedicamentTimeViewHolder(val binding: UpdateItemMedtimeBinding) :
        RecyclerView.ViewHolder(binding.root)
}
