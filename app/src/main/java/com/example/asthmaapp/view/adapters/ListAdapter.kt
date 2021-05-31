package com.example.asthmaapp.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.asthmaapp.model.MeasureOfDay
import com.example.asthmaapp.databinding.CustomRowBinding
import com.example.asthmaapp.view.fragments.ListFragmentDirections

class ListAdapter: RecyclerView.Adapter<ListAdapter.MyViewHolder>() {
    private var measureList  = emptyList<MeasureOfDay>()

    class MyViewHolder(val binding : CustomRowBinding): RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = CustomRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
       val currentItem = measureList[position]
        with(holder.binding) {

            texDaytDate.text = currentItem.dayOfMeasure
            firstTimeList.text = currentItem.firstTime
            secondTimeList.text = currentItem.secondTime
            thirdTimeList.text = currentItem.thirdTime
            measuteTextList.text = currentItem.measureM.toString()
            measure2TextList.text = currentItem.measureD.toString()
            measure3TextList.text = currentItem.measureE.toString()
        }


        holder.binding.rowLayout.setOnClickListener{
            val action = ListFragmentDirections.actionListFragmentToUpdateFragment(currentItem)
            holder.itemView.findNavController().navigate(action)
        }

    }

    override fun getItemCount(): Int {
        return measureList.size
    }

    fun setData(measure: List<MeasureOfDay>){
        this.measureList = measure
        notifyDataSetChanged()
    }
}