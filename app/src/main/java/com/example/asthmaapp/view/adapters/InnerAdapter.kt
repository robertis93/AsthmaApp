package com.example.asthmaapp.view.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.asthmaapp.R
import com.example.asthmaapp.databinding.InnerItemBinding
import com.example.asthmaapp.model.TimeAndMeasure


class InnerAdapter(var measures: List<TimeAndMeasure>, timeAndMeasure: List<TimeAndMeasure>) :
    RecyclerView.Adapter<InnerAdapter.MyViewHolder>() {

    val timeAndMeasureList = timeAndMeasure

    class MyViewHolder(val binding: InnerItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = InnerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = measures[position]
        var max = timeAndMeasureList[0].measure
        for (i in timeAndMeasureList .indices)
            if (timeAndMeasureList [i].measure > max)
                max = timeAndMeasureList[i].measure

        var control = max *0.85
        val controlHigh = max *0.75

        holder.binding.hourText.text = com.example.asthmaapp.utils.timeConvert(currentItem.hour)
        holder.binding.minuteText.text = com.example.asthmaapp.utils.timeConvert(currentItem.minute)
        val measurenow = currentItem.measure
        if (measurenow < control)
            holder.binding.measureText.setBackgroundColor(holder.itemView.context.getColor(R.color.yelow))
        if (measurenow < controlHigh)
            holder.binding.measureText.setBackgroundColor(holder.itemView.context.getColor(R.color.red))
            holder.binding.measureText.text = currentItem.measure.toString()
    }

    override fun getItemCount(): Int {
        return measures.size
    }
}
