package com.example.asthmaapp.view.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.asthmaapp.R
import com.example.asthmaapp.databinding.InnerItemBinding
import com.example.asthmaapp.model.Measure
import com.example.asthmaapp.utils.DateUtil.dateTimeStampToSimpleDateFormatHourMinute

class MeasureAdapter(
    var measures: List<Measure>,
    measure: List<Measure>,
    var onClickListener: OnClickListener
) :
    RecyclerView.Adapter<MeasureAdapter.MeasureViewHolder>() {

    private val timeAndMeasureList = measure

    interface OnClickListener {
        fun actionClick()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeasureViewHolder {
        val binding = InnerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MeasureViewHolder(binding)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MeasureViewHolder, position: Int) {
        val currentItem = measures[position]
        var max = timeAndMeasureList[0].measure
        for (i in timeAndMeasureList.indices)
            if (timeAndMeasureList[i].measure > max)
                max = timeAndMeasureList[i].measure

        val controlValue = max * 0.85
        val controlHighValue = max * 0.75

        holder.binding.timeTextView.text =
            dateTimeStampToSimpleDateFormatHourMinute(currentItem.dateTimestamp)
        val measureNow = currentItem.measure
        if (measureNow < controlValue)
            holder.binding.measureText.setBackgroundColor(holder.itemView.context.getColor(R.color.yelow))
        if (measureNow < controlHighValue)
            holder.binding.measureText.setBackgroundColor(holder.itemView.context.getColor(R.color.red))
        holder.binding.measureText.text = currentItem.measure.toString()
        holder.itemView.setOnClickListener {
            onClickListener.actionClick()
        }
    }

    override fun getItemCount(): Int {
        return measures.size
    }

    class MeasureViewHolder(val binding: InnerItemBinding) : RecyclerView.ViewHolder(binding.root)
}
