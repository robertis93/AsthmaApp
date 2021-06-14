package com.example.asthmaapp.view.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.asthmaapp.databinding.CustomRowBinding
import com.example.asthmaapp.model.MedWithMeasures

class ListAdapter: RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    private var dayMeasureList  = emptyList<MedWithMeasures>()

    class MyViewHolder(val binding : CustomRowBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = CustomRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
    val dayCurrentItem = dayMeasureList[position]

        holder.binding.texDaytDate.text = dayCurrentItem.day.day.toString()

        val student = dayCurrentItem.measures

        //recycler
        val adapter = InnerAdapter(student)
        val recyclerView = holder.binding.reyclerRow
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(holder.binding.reyclerRow.context, 2, LinearLayoutManager.HORIZONTAL,false)
    }

    override fun getItemCount(): Int {
        return dayMeasureList.size
    }

    fun setDayData(dayWithMeasures: List<MedWithMeasures>){
        this.dayMeasureList = dayWithMeasures
        notifyDataSetChanged()
    }
}