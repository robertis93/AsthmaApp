package com.example.asthmaapp.view.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.asthmaapp.databinding.AddFragmentMedicamentTimeItemBinding
import com.example.asthmaapp.model.TakeMedicamentTimeEntity
import com.example.asthmaapp.utils.DateUtil.dateTimeStampToSimpleDateFormatHourMinute

class AddMedicamentTimeAdapter(var measuresMedList: List<TakeMedicamentTimeEntity>, val onDeleteClickListener: DeleteClickListener) :
    RecyclerView.Adapter<AddMedicamentTimeAdapter.AddMedicamentViewHolder>() {

    interface DeleteClickListener {
        fun onDeleteTakeMedicamentTime(takeMedicamentTimeEntity: TakeMedicamentTimeEntity)
    }
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
                "${dateTimeStampToSimpleDateFormatHourMinute(currentItem.dateTimeStamp)}"
            deleteAlarmIcon.setOnClickListener {
                onDeleteClickListener.onDeleteTakeMedicamentTime(measuresMedList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return measuresMedList.size
    }

//    fun addData(takeMedicamentTimeEntity: TakeMedicamentTimeEntity) {
//        measuresMedList.add(takeMedicamentTimeEntity)
//        notifyDataSetChanged()
//    }
//
//    private fun deleteDate(takeMedicamentTimeEntity: TakeMedicamentTimeEntity) {
//        measuresMedList.remove(takeMedicamentTimeEntity)
//        notifyDataSetChanged()
//    }
//
//    fun getListMedicamentTime(): MutableList<TakeMedicamentTimeEntity> {
//        return measuresMedList
//    }

    class AddMedicamentViewHolder(val binding: AddFragmentMedicamentTimeItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}
