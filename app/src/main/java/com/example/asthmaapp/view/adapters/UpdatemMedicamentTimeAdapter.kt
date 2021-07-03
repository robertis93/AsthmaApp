package com.example.asthmaapp.view.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.asthmaapp.R
import com.example.asthmaapp.databinding.LayoutDialogMedicalAddFragmentBinding
import com.example.asthmaapp.databinding.UpdateItemMedtimeBinding
import com.example.asthmaapp.model.MedicamentTime

class UpdatemMedicamentTimeAdapter(
    var timesMedicament: List<MedicamentTime>,
    var onClickListener: OnClickListener
) :
    RecyclerView.Adapter<UpdatemMedicamentTimeAdapter.MyViewHolder>() {

    interface OnClickListener {
        fun onDeleteClick(medicamentTime: MedicamentTime, position: Int)
    }

    val timeList = timesMedicament.toMutableList()

    class MyViewHolder(val binding: UpdateItemMedtimeBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = UpdateItemMedtimeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = timeList[position]
        holder.binding.hourText.setText(com.example.asthmaapp.utils.timeConvert(currentItem.hour))
        holder.binding.minuteText.setText(com.example.asthmaapp.utils.timeConvert(currentItem.minute))

        holder.binding.editAlarmImage.setOnClickListener {
            val builder =
                androidx.appcompat.app.AlertDialog.Builder(holder.binding.editAlarmImage.context)
            val layoutInflater = LayoutInflater.from(holder.binding.editAlarmImage.context)
            val dialogFragment = LayoutDialogMedicalAddFragmentBinding.inflate(layoutInflater)
            dialogFragment.timePicker.is24HourView
            builder.setView(dialogFragment.root)
            builder.setTitle(R.string.measure_alarm_frag)

            //show dialog
            val mAlertDialog = builder.show()
            dialogFragment.timePicker.setIs24HourView(true)

            dialogFragment.timePicker.hour = currentItem.hour
            dialogFragment.timePicker.minute = currentItem.minute

            //сохраняем в бд замер
            dialogFragment.btnSave.setOnClickListener {
                mAlertDialog.dismiss()
                dialogFragment.timePicker.is24HourView
                val timeHour = dialogFragment.timePicker.hour
                val timeMinute = dialogFragment.timePicker.minute

                holder.binding.hourText.text = com.example.asthmaapp.utils.timeConvert(timeHour)
                holder.binding.minuteText.text = com.example.asthmaapp.utils.timeConvert(timeMinute)
                timeList[position].hour = timeHour
                timeList[position].minute = timeMinute
            }

            dialogFragment.btnCansel.setOnClickListener {
                mAlertDialog.dismiss()
            }
        }

        holder.binding.deleteImage.setOnClickListener {
            deleteData(timeList[position])
            onClickListener?.onDeleteClick(currentItem, position)
        }
    }

    fun deleteData(medicamentTime: MedicamentTime) {
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
        // this.measuresMedList
        timeList.add(medicamentTime)
        notifyDataSetChanged()
    }

}
