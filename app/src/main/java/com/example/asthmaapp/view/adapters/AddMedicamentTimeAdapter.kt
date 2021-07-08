package com.example.asthmaapp.view.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.asthmaapp.databinding.AddFragmentMedicamentTimeItemBinding
import com.example.asthmaapp.model.MedicamentTime

class AddMedicamentTimeAdapter() :
    RecyclerView.Adapter<AddMedicamentTimeAdapter.AddMedicamentViewHolder>() {
    private var measuresMedList = mutableListOf<MedicamentTime>()

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
                "${com.example.asthmaapp.utils.timeConvert(currentItem.hour)} : ${
                    com.example.asthmaapp.utils.timeConvert(currentItem.minute)
                }"
            deleteAlarmIcon.setOnClickListener {
                deleteDate(measuresMedList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return measuresMedList.size
    }

    fun addData(medicamentTime: MedicamentTime) {
        measuresMedList.add(medicamentTime)
        notifyDataSetChanged()
    }

    private fun deleteDate(medicamentTime: MedicamentTime) {
        measuresMedList.remove(medicamentTime)
        notifyDataSetChanged()
    }

    fun getDataMedTime(): MutableList<MedicamentTime> {
        return measuresMedList
    }

    class AddMedicamentViewHolder(val binding: AddFragmentMedicamentTimeItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}
