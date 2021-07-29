package com.example.asthmaapp.view.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.asthmaapp.databinding.UpdateItemMedtimeBinding
import com.example.asthmaapp.model.TakeMedicamentTimeEntity
import com.example.asthmaapp.utils.DateUtil.timestampToDisplayTime

class EditTakeMedicamentTimeAdapter(
    private var measuresMedList: List<TakeMedicamentTimeEntity>,
    private val onclickListener: ClickListener,
    private val isInUpdateMode: Boolean = false
) :
    RecyclerView.Adapter<EditTakeMedicamentTimeAdapter.AddMedicamentViewHolder>() {

    interface ClickListener {
        fun onDeleteTakeMedicamentTime(takeMedicamentTimeEntity: TakeMedicamentTimeEntity)
        fun onUpdateTakeMedicamentTime(takeMedicamentTimeEntity: TakeMedicamentTimeEntity, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddMedicamentViewHolder {
        val binding = UpdateItemMedtimeBinding.inflate(
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
            timeTextView.text =
                timestampToDisplayTime(currentItem.dateTimestamp)
            deleteImage.setOnClickListener {
                onclickListener.onDeleteTakeMedicamentTime(measuresMedList[position])
            }
            editImage.visibility = if (isInUpdateMode)
                View.VISIBLE
            else
                View.GONE
            editImage.setOnClickListener {
                onclickListener.onUpdateTakeMedicamentTime(currentItem, position)
            }
        }
    }

    override fun getItemCount(): Int {
        return measuresMedList.size
    }

    class AddMedicamentViewHolder(val binding: UpdateItemMedtimeBinding) :
        RecyclerView.ViewHolder(binding.root)
}
