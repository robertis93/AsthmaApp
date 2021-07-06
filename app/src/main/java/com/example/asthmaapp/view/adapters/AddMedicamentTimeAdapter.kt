package com.example.asthmaapp.view.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.asthmaapp.databinding.AddFragmentMedicamentTimeItemBinding
import com.example.asthmaapp.model.MedicamentTime

class AddMedicamentTimeAdapter(val measuresMedList: List<MedicamentTime>, var onClickDeleteListener: OnDeleteClickListener) :
    RecyclerView.Adapter<AddMedicamentTimeAdapter.MyViewHolder>() {

    // определили интерфейс слушателя события нажатия
    interface OnDeleteClickListener {
        fun onDeleteAlarmClick(medicamentTime: MedicamentTime)
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
                onClickDeleteListener?.onDeleteAlarmClick(measuresMedList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return measuresMedList.size
    }
}
