package com.example.asthmaapp.view.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.asthmaapp.R
import com.example.asthmaapp.databinding.AddFragmentMeasureItemBinding
import com.example.asthmaapp.model.MeasureOfDay
import com.example.asthmaapp.model.MedicamentTime
import com.example.asthmaapp.model.TimeAndMeasure
import com.example.asthmaapp.view.fragments.ListFragmentDirections

class AlarmActivitytMeasureAdapter() : RecyclerView.Adapter<AlarmActivitytMeasureAdapter.MyViewHolder>() {
    private var measuresList = mutableListOf<String>()

    // определили интерфейс слушателя события нажатия
    interface OnMeasureClickListener {
        fun onDeleteMeasureClick(timeAndMeasure: TimeAndMeasure, position: Int)
    }

    class MyViewHolder(val binding: AddFragmentMeasureItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = AddFragmentMeasureItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = measuresList[position]
        with(holder.binding) {
            timeMeasure.text = currentItem.toString()
//            measure.text = currentItem.measure.toString()
//            deleteAlarm.setOnClickListener {
//                deleteData(measuresList[position])
//            }

        }

    }

    override fun getItemCount(): Int {
        return measuresList.size
    }

//    fun setData(timeAndMeasure: MutableList<TimeAndMeasure>) {
//        this.measuresList = timeAndMeasure
//        notifyDataSetChanged()
//    }

    fun addData(time : String) {
        // this.measuresMedList
        measuresList.add(time)
        notifyDataSetChanged()
    }

//    fun getData(): MutableList<TimeAndMeasure> {
//        return measuresList
//    }

    fun deleteData(timeAndMeasure: TimeAndMeasure) {
        // this.measuresMedList
        measuresList.remove(timeAndMeasure)
        notifyDataSetChanged()
    }


//    fun getDataTest() : TimeAndMeasure {
//        val time  = TimeAndMeasure(0, 22,33,200)
//        return time
//        notifyDataSetChanged()
//    }


}