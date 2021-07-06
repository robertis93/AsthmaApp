package com.example.asthmaapp.view.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.asthmaapp.databinding.AddFragmentMedicamentTimeItemBinding
import com.example.asthmaapp.model.MedicamentTime

class AddMedicamentTimeAdapter(var onClickDeleteListener: OnDeleteClickListener) :
    RecyclerView.Adapter<AddMedicamentTimeAdapter.MyViewHolder>() {
    private var measuresMedList = mutableListOf<MedicamentTime>()

    // определили интерфейс слушателя события нажатия
    interface OnDeleteClickListener {
        fun onDeleteAlarmClick()
    }

    class MyViewHolder(val binding: AddFragmentMedicamentTimeItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = AddFragmentMedicamentTimeItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    @SuppressLint("ResourceAsColor", "SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = measuresMedList[position]
        with(holder.binding) {
            timeMeasureText.text =
                "${com.example.asthmaapp.utils.timeConvert(currentItem.hour)} : ${
                    com.example.asthmaapp.utils.timeConvert(currentItem.minute)
                }"

            deleteAlarmIcon.setOnClickListener {
                deleteDate(measuresMedList[position])
                onClickDeleteListener?.onDeleteAlarmClick()
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

    fun deleteDate(medicamentTime: MedicamentTime) {
        // this.measuresMedList
        measuresMedList.remove(medicamentTime)
        notifyDataSetChanged()
    }


    fun getDataMedTime(): MutableList<MedicamentTime> {
        return measuresMedList
    }
}
