package com.example.asthmaapp.view.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.asthmaapp.databinding.UpdateMeasuresInnerItemBinding
import com.example.asthmaapp.model.Measure
import com.example.asthmaapp.utils.DateUtil.timestampToDisplayTime

class AddAndUpdateMeasureAdapter(val measuresList: List<Measure>, val listener: DeleteListener, val isInUpdateMode: Boolean = false) :
    RecyclerView.Adapter<AddAndUpdateMeasureAdapter.AddMeasureViewHolder>() {

    interface DeleteListener {
        fun onDeleteMeasureClick(measure: Measure)
        fun onUpdateMeasureClick(measure: Measure, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddMeasureViewHolder {
        val binding = UpdateMeasuresInnerItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AddMeasureViewHolder(binding)
    }

    @SuppressLint("ResourceAsColor", "SetTextI18n")
    override fun onBindViewHolder(holder: AddMeasureViewHolder, position: Int) {
        val currentItem = measuresList[position]
        with(holder.binding) {
            timeTextView.text =
                timestampToDisplayTime(currentItem.dateTimestamp)
            measureText.text = currentItem.value.toString()
            deleteIcon.setOnClickListener {
                listener.onDeleteMeasureClick(measuresList[position])
            }
            editImage.visibility = if(isInUpdateMode)
                View.VISIBLE
            else
                View.GONE
            editImage.setOnClickListener {
                listener.onUpdateMeasureClick(currentItem, position)
            }
        }
    }

    override fun getItemCount(): Int {
        return measuresList.size
    }

    class AddMeasureViewHolder(val binding: UpdateMeasuresInnerItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}