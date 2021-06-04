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

class AddFragmentMeasureAdapter(var onClickMeasureListener: OnMeasureClickListener): RecyclerView.Adapter<AddFragmentMeasureAdapter.MyViewHolder>() {
    private var measuresList  = emptyList<TimeAndMeasure>()

    // определили интерфейс слушателя события нажатия
    interface OnMeasureClickListener {
        fun onDeleteMeasureClick(timeAndMeasure: TimeAndMeasure, position: Int)
    }

    class MyViewHolder(val binding : AddFragmentMeasureItemBinding): RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = AddFragmentMeasureItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
       val currentItem = measuresList[position]
        with(holder.binding) {
            timeMeasure.text =  "${currentItem.hour.toString()} : ${com.example.asthmaapp.utils.minuteShow(currentItem.minute)}"
            measure.text = currentItem.measure.toString()
            deleteAlarm.setOnClickListener {
                // вызываем метод слушателя, передавая ему данные
                onClickMeasureListener?.onDeleteMeasureClick(currentItem, position)
            }
          //  measure.text = currentItem.measure
        }

    }

    override fun getItemCount(): Int {
        return measuresList.size
    }

    fun setData(timeAndMeasure: List<TimeAndMeasure>){
        this.measuresList = timeAndMeasure
        notifyDataSetChanged()
    }
}