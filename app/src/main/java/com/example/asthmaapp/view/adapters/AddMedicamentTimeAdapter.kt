package com.example.asthmaapp.view.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.asthmaapp.databinding.AddFragmentMedicamentTimeItemBinding
import com.example.asthmaapp.model.TakeMedicamentTimeEntity

class AddMedicamentTimeAdapter() :
    RecyclerView.Adapter<AddMedicamentTimeAdapter.AddMedicamentViewHolder>() {
    private var measuresMedList = mutableListOf<TakeMedicamentTimeEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddMedicamentViewHolder {
        val binding = AddFragmentMedicamentTimeItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AddMedicamentViewHolder(binding)
    }

    @SuppressLint("ResourceAsColor", "SetTextI18n")
    override fun onBindViewHolder(holder: AddMedicamentViewHolder, position: Int) {
        val currentItem = measuresMedList[position]
        with(holder.binding) {
            timeMeasureText.text =
                "${com.example.asthmaapp.utils.timeConvert(currentItem.timeHour)} : ${
                    com.example.asthmaapp.utils.timeConvert(currentItem.timeMinute)
                }"
            deleteAlarmIcon.setOnClickListener {
                deleteDate(measuresMedList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return measuresMedList.size
    }

    fun addData(takeMedicamentTimeEntity: TakeMedicamentTimeEntity) {
        measuresMedList.add(takeMedicamentTimeEntity)
        notifyDataSetChanged()
    }

    private fun deleteDate(takeMedicamentTimeEntity: TakeMedicamentTimeEntity) {
        measuresMedList.remove(takeMedicamentTimeEntity)
        notifyDataSetChanged()
    }

    fun getListMedicamentTime(): MutableList<TakeMedicamentTimeEntity> {
        return measuresMedList
    }

    class AddMedicamentViewHolder(val binding: AddFragmentMedicamentTimeItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}
