package com.example.asthmaapp.view.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.asthmaapp.databinding.AddFragmentMedicamentTimeItemBinding
import com.example.asthmaapp.model.MedicamentTime
import com.example.asthmaapp.model.TimeAndMeasure
import com.example.asthmaapp.model.models.Alarm

class AddFragmentMedicamentTimeAdapter(var onClickAlarmListener: OnAlarmClickListener) :
    RecyclerView.Adapter<AddFragmentMedicamentTimeAdapter.MyViewHolder>() {
    private var measuresMedList = emptyList<MedicamentTime>()

    // определили интерфейс слушателя события нажатия
    interface OnAlarmClickListener {
        fun onDeleteAlarmClick(medicamentTime: MedicamentTime, position: Int)
    }

    class MyViewHolder(val binding: AddFragmentMedicamentTimeItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = AddFragmentMedicamentTimeItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = measuresMedList[position]
        with(holder.binding) {
            timeMeasure.text = "${com.example.asthmaapp.utils.minuteShow(currentItem.hour)} : ${
                com.example.asthmaapp.utils.minuteShow(currentItem.minute)
            }"
            checkBox2.isChecked = true
            checkBox2.isEnabled = false
            deleteAlarm.setOnClickListener {
                // вызываем метод слушателя, передавая ему данные
                onClickAlarmListener?.onDeleteAlarmClick(currentItem, position)
            }

        }

    }

    override fun getItemCount(): Int {
        return measuresMedList.size
    }

    fun setData(medicamentTime: List<MedicamentTime>) {
        this.measuresMedList = medicamentTime
        notifyDataSetChanged()
    }
}