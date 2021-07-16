package com.example.asthmaapp.view.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.asthmaapp.databinding.AddFragmentMeasureItemBinding
import com.example.asthmaapp.model.Measure
import com.example.asthmaapp.utils.DateUtil.dateTimeStampToSimpleDateFormatHourMinute

class AddMeasureAdapter() : RecyclerView.Adapter<AddMeasureAdapter.AddMeasureViewHolder>() {
    private var measuresList = mutableListOf<Measure>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddMeasureViewHolder {
        val binding = AddFragmentMeasureItemBinding.inflate(
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
            timeMeasureText.text =
                dateTimeStampToSimpleDateFormatHourMinute(currentItem.dateTimestamp)
            addMeasureText.text = currentItem.measure.toString()
            deleteAlarmIcon.setOnClickListener {
                deleteData(measuresList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return measuresList.size
    }

    fun addMeasure(measure: Measure) {
        measuresList.add(measure)
        notifyDataSetChanged()
    }

    fun getListMeasure(): MutableList<Measure> {
        return measuresList
    }

    private fun deleteData(measure: Measure) {
        measuresList.remove(measure)
        notifyDataSetChanged()
    }

    class AddMeasureViewHolder(val binding: AddFragmentMeasureItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}