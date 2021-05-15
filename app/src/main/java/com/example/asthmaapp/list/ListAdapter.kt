package com.example.asthmaapp.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.asthmaapp.model.MeasureOfDay
import com.example.asthmaapp.databinding.CustomRowBinding

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
        holder.binding.editTextDate.text = currentItem.id.toString()
        holder.binding.firstTimeList.text = currentItem.firstTime
        holder.binding.secondTimeList.text = currentItem.secondTime
        holder.binding.thirdTimeList.text = currentItem.thirdTime
        holder.binding.measuteTextList.text = currentItem.measureM.toString()
        holder.binding.measure2TextList.text = currentItem.measureD.toString()
        holder.binding.measure3TextList.text = currentItem.measureE.toString()

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