package com.example.asthmaapp.view.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.asthmaapp.databinding.InnerItemBinding
import com.example.asthmaapp.model.TimeAndMeasure
import com.example.asthmaapp.model.models.MedicalInfo


class AlarmActivityTestAdapter: RecyclerView.Adapter<AlarmActivityTestAdapter.MyViewHolder>() {

   private var medicalInfo = mutableListOf<MedicalInfo>()
    private var timeMeasureList  = emptyList<TimeAndMeasure>()

    class MyViewHolder(val binding : InnerItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = InnerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
    val dayCurrentItem = timeMeasureList[position]

        holder.binding.measureText.text = dayCurrentItem.measure.toString()



    }

    override fun getItemCount(): Int {
        return timeMeasureList.size
    }


    fun addTimeAndMeasure(timeAndMeasure: List<TimeAndMeasure>){
        this.timeMeasureList = timeAndMeasure
    }

    fun addMedicalInfo(medicament: List<MedicalInfo>){
        medicalInfo.addAll(medicament)
    }

    fun getMedical() : List<MedicalInfo>{
        return medicalInfo
    }


}