package com.example.asthmaapp.view.adapters

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.asthmaapp.databinding.UpdateItemMedtimeBinding
import com.example.asthmaapp.model.MedicamentTime
import com.example.asthmaapp.model.TimeAndMeasure

class UpdateFrTimeAdapter(
    var timesMedicament: List<MedicamentTime>,
    var onClickListener: OnClickListener
) :
    RecyclerView.Adapter<UpdateFrTimeAdapter.MyViewHolder>() {

    interface OnClickListener {
        fun onDeleteClick(medicamentTime: MedicamentTime, position: Int)
    }

    val timeList = timesMedicament.toMutableList()

    class MyViewHolder(val binding: UpdateItemMedtimeBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = UpdateItemMedtimeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = timeList[position]
        holder.binding.hourText.setText(currentItem.hour.toString())
        holder.binding.minuteText.setText(com.example.asthmaapp.utils.minuteShow(currentItem.minute))

        holder.binding.hourText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                try {
                    timeList[position].hour = s.toString().toInt()
                } catch (e: Exception) {
                    return
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        holder.binding.minuteText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                try {
                    timeList[position].minute = s.toString().toInt()
                } catch (e: Exception) {
                    return
                }
                //timeAndMeasureList.set(position, currentItem)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })


//        try {
//            holder.binding.minuteText.doAfterTextChanged { it: Editable? ->
//                timeAndMeasureList[position].minute =
//                    it?.toString()?.toInt() ?: 0// Do your stuff here
//            }
//        } catch (e: NumberFormatException) {
//            throw NumberFormatException()
//        }


        holder.binding.hourText.setOnClickListener {
            //  updateData(position, currentItem)
        }

        holder.binding.imageDeleteAlarm.setOnClickListener {
            deleteData(timeList[position])
//        holder.binding.imageDeleteAlarm.setOnClickListener {
//            // вызываем метод слушателя, передавая ему данные
            onClickListener?.onDeleteClick(currentItem, position)
        }
    }

    fun deleteData(medicamentTime: MedicamentTime) {
        // this.measuresMedList
        timeList.remove(medicamentTime)
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        return timeList.size
    }

//    fun updateData(position : Int, timeAndMeasure: TimeAndMeasure){
//        // this.measuresMedList
//        timeAndMeasureList.set(position, timeAndMeasure)
////        notifyDataSetChanged()
//    }

    fun getData(): MutableList<MedicamentTime> {
        return timeList

    }

    fun addData(medicamentTime: MedicamentTime) {
        // this.measuresMedList
        timeList.add(medicamentTime)
        notifyDataSetChanged()
    }

    fun addAllData(medicamentTime: List<MedicamentTime>) {
        // this.measuresMedList
        timeList.addAll(medicamentTime)
        notifyDataSetChanged()
    }

}


//class UpdateFrTimeAdapter(var timesMedicament: List<MedicamentTime>) :
//    RecyclerView.Adapter<UpdateFrTimeAdapter.MyViewHolder>() {
//
//
//    class MyViewHolder(val binding: UpdateItemMedtimeBinding) :
//        RecyclerView.ViewHolder(binding.root)
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
//        val binding =
//            UpdateItemMedtimeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return MyViewHolder(binding)
//    }
//
//    @SuppressLint("ResourceAsColor")
//    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//        val currentItem = timesMedicament[position]
//
//        holder.binding.hourText.setText(currentItem.hour.toString())
//        holder.binding.minuteText.setText(currentItem.minute.toString())
//
//    }
//
//    override fun getItemCount(): Int {
//        return timesMedicament.size
//    }
//}