package com.example.asthmaapp.view.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.asthmaapp.databinding.CustomRowBinding
import com.example.asthmaapp.model.MedWithMeasuresAndMedicamentTime
import com.example.asthmaapp.model.TimeAndMeasure
import com.example.asthmaapp.view.fragments.ListFragmentDirections

class ListAdapter: RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    private var dayMeasureList  = emptyList<MedWithMeasuresAndMedicamentTime>()
    private var timeMeasureList  = emptyList<TimeAndMeasure>()

    class MyViewHolder(val binding : CustomRowBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = CustomRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
    val dayCurrentItem = dayMeasureList[position]

        holder.binding.texDaytDate.text = com.example.asthmaapp.utils.longToStringCalendar(dayCurrentItem.day.day)

        holder.binding.rowLayout.setOnClickListener {
            val action = ListFragmentDirections.actionListFragmentToUpdateFragment(dayCurrentItem)
            holder.binding.root.findNavController().navigate(action)
        }

        val measuresAll = dayCurrentItem.measures

        //recycler Measures
        val adapter = InnerAdapter(measuresAll, timeMeasureList)
        val recyclerView = holder.binding.reyclerRow
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(holder.binding.reyclerRow.context, 1, LinearLayoutManager.VERTICAL,false)

        val timeDrugs = dayCurrentItem.medicamentTime
        //recyclerTimeDrugs

        val medTimeAdapter = InnerMedTimeAdapter(timeDrugs)
        val recyclerViewMedTime = holder.binding.recyclerTimeMedical
        recyclerViewMedTime.adapter = medTimeAdapter
        recyclerViewMedTime.layoutManager = GridLayoutManager(holder.binding.recyclerTimeMedical.context, 1, LinearLayoutManager.HORIZONTAL,false)


    }

    override fun getItemCount(): Int {
        return dayMeasureList.size
    }

    fun setDayData(dayWithMeasures: List<MedWithMeasuresAndMedicamentTime>){
        this.dayMeasureList = dayWithMeasures
        notifyDataSetChanged()
    }

    fun addTimeAndMeasure(timeAndMeasure: List<TimeAndMeasure>){
        this.timeMeasureList = timeAndMeasure

    }
}