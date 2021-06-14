package com.example.asthmaapp.view.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.asthmaapp.databinding.InnerItemBinding
import com.example.asthmaapp.model.TimeAndMeasure


class InnerAdapter(var measures: List<TimeAndMeasure>) :
    RecyclerView.Adapter<InnerAdapter.MyViewHolder>() {


    class MyViewHolder(val binding: InnerItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = InnerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = measures[position]

        holder.binding.hourText.text = currentItem.hour.toString()
        holder.binding.minuteText.text = currentItem.minute.toString()
        holder.binding.measureText.text = currentItem.measure.toString()
    }

    override fun getItemCount(): Int {
        return measures.size
    }
}

// val currentItem = measures[position]
//        var measureList: MutableList<Int> = mutableListOf()
//        var summa : Int = 0
//        for (i in 0..measures.size-1){
//            summa += currentItem.measure
//           //measureList.add(currentItem.measure)
//        }
//        var control = summa / measures.size
//
//        holder.binding.hourText.text = currentItem.hour.toString()
//        holder.binding.minuteText.text = currentItem.minute.toString()
//        if (currentItem.measure < control)