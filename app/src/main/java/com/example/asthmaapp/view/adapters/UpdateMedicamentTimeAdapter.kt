package com.example.asthmaapp.view.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.asthmaapp.R
import com.example.asthmaapp.databinding.LayoutDialogMedicalAddFragmentBinding
import com.example.asthmaapp.databinding.UpdateItemMedtimeBinding
import com.example.asthmaapp.model.MedicamentTime

class UpdateMedicamentTimeAdapter(
    var timesMedicament: List<MedicamentTime>,
    var onClickListener: OnClickListener
) :
    RecyclerView.Adapter<UpdateMedicamentTimeAdapter.UpdateMedicamentTimeViewHolder>() {

    private val timeList = timesMedicament.toMutableList()

    interface OnClickListener {
        fun onDeleteClick(medicamentTime: MedicamentTime, position: Int)
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
        holder.binding.hourText.setText(com.example.asthmaapp.utils.timeConvert(currentItem.hour))
        holder.binding.minuteText.setText(com.example.asthmaapp.utils.timeConvert(currentItem.minute))

        holder.binding.editAlarmImage.setOnClickListener {
            editTimeAlarmMedicament(holder, position, currentItem)
        }

        holder.binding.deleteImage.setOnClickListener {
            deleteData(timeList[position])
            onClickListener?.onDeleteClick(currentItem, position)
        }
    }

    private fun editTimeAlarmMedicament(
        holder: UpdateMedicamentTimeViewHolder,
        position: Int,
        currentItem: MedicamentTime
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

        dialogFragment.timePicker.hour = currentItem.hour
        dialogFragment.timePicker.minute = currentItem.minute

        dialogFragment.btnSave.setOnClickListener {
            alertDialog.dismiss()
            dialogFragment.timePicker.is24HourView
            val timeHour = dialogFragment.timePicker.hour
            val timeMinute = dialogFragment.timePicker.minute

            holder.binding.hourText.text = com.example.asthmaapp.utils.timeConvert(timeHour)
            holder.binding.minuteText.text = com.example.asthmaapp.utils.timeConvert(timeMinute)
            timeList[position].hour = timeHour
            timeList[position].minute = timeMinute
        }

        dialogFragment.cancelBtn.setOnClickListener {
            alertDialog.dismiss()
        }
    }

    private fun deleteData(medicamentTime: MedicamentTime) {
        timeList.remove(medicamentTime)
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        return timeList.size
    }


    fun getData(): MutableList<MedicamentTime> {
        return timeList
    }

    fun addData(medicamentTime: MedicamentTime) {
        timeList.add(medicamentTime)
        notifyDataSetChanged()
    }

    class UpdateMedicamentTimeViewHolder(val binding: UpdateItemMedtimeBinding) :
        RecyclerView.ViewHolder(binding.root)
}
