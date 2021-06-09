package com.example.asthmaapp.view.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.asthmaapp.R
import com.example.asthmaapp.model.MeasureOfDay
import com.example.asthmaapp.databinding.CustomRowBinding
import com.example.asthmaapp.model.DayWithMeasures
import com.example.asthmaapp.model.TimeAndMeasure
import com.example.asthmaapp.view.fragments.ListFragmentDirections

class ListAdapter: RecyclerView.Adapter<ListAdapter.MyViewHolder>() {
    private var measureList  = emptyList<MeasureOfDay>()
    private var dayMeasureList  = emptyList<DayWithMeasures>()
    private var timeMeasureList  = emptyList<TimeAndMeasure>()

    class MyViewHolder(val binding : CustomRowBinding): RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = CustomRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
       //val currentItem = measureList[position]
      val daycurrentItem = dayMeasureList[position]

           // holder.binding.texDaytDate.text = currentItem.dayOfMeasure
            //holder.binding.firstTimeList.text = currentItem.doza.toString()

       //     holder.binding.firstTimeList.text = daycurrentItem.measures.toString()
            holder.binding.firstTimeList.text = daycurrentItem.day.nameMedicamentaion
            holder.binding.secondTimeList.text = daycurrentItem.measures.toString()
//            firstTimeList.text = currentItem.firstTime
//            secondTimeList.text = currentItem.secondTime
//            thirdTimeList.text = currentItem.thirdTime
//            measuteTextList.text = currentItem.measureOne.toString()
//            measure2TextList.text = currentItem.measureTw o.toString()
//            measure3TextList.text = currentItem.measureThree.toString()
//            if (currentItem.measureThree!! > 1) {
//                measure3TextList.setBackgroundColor(R.color.purple_200)
//            }


//        holder.binding.rowLayout.setOnClickListener{
//            val action = ListFragmentDirections.actionListFragmentToUpdateFragment(currentItem)
//            holder.itemView.findNavController().navigate(action)
//        }
    }

    override fun getItemCount(): Int {
        return measureList.size
    }

    fun setData(measure: List<MeasureOfDay>){
        this.measureList = measure
        notifyDataSetChanged()
    }

    fun setDayData(dayWithMeasures: List<DayWithMeasures>){
        this.dayMeasureList = dayWithMeasures
        notifyDataSetChanged()
    }

    fun setTimeMeasure(timeAndMeasure: List<TimeAndMeasure>){
        this.timeMeasureList = timeAndMeasure
        notifyDataSetChanged()
    }
}