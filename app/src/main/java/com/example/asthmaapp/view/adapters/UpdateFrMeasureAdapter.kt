package com.example.asthmaapp.view.adapters

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.asthmaapp.databinding.UpdateMeasuresInnerItemBinding
import com.example.asthmaapp.model.TimeAndMeasure


class UpdateFrMeasureAdapter(
    timeAndMeasure: List<TimeAndMeasure>,
    var onClickListener: OnClickListener
) :
    RecyclerView.Adapter<UpdateFrMeasureAdapter.MyViewHolder>() {

    interface OnClickListener {
        fun onDeleteClick(timeAndMeasure: TimeAndMeasure, position: Int)
    }

    val timeAndMeasureList = timeAndMeasure.toMutableList()

    class MyViewHolder(val binding: UpdateMeasuresInnerItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = UpdateMeasuresInnerItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = timeAndMeasureList[position]


        holder.binding.hourText.setText(currentItem.hour.toString())
        // holder.binding.hourText.doAfterTextChanged{  }
        holder.binding.minuteText.setText(com.example.asthmaapp.utils.timeConvert(currentItem.minute))
        holder.binding.measureText.setText(currentItem.measure.toString())

        holder.binding.hourText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                try {
                    timeAndMeasureList[position].hour = s.toString().toInt()
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
                    timeAndMeasureList[position].minute = s.toString().toInt()
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

        holder.binding.measureText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                try {
                    timeAndMeasureList[position].measure = s.toString().toInt()
                } catch (e: Exception) {
                    return
                }
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
            deleteData(timeAndMeasureList[position])
//        holder.binding.imageDeleteAlarm.setOnClickListener {
//            // вызываем метод слушателя, передавая ему данные
            onClickListener?.onDeleteClick(currentItem, position)
        }
    }

    fun deleteData(timeAndMeasure: TimeAndMeasure) {
        // this.measuresMedList
        timeAndMeasureList.remove(timeAndMeasure)
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        return timeAndMeasureList.size
    }

//    fun updateData(position : Int, timeAndMeasure: TimeAndMeasure){
//        // this.measuresMedList
//        timeAndMeasureList.set(position, timeAndMeasure)
////        notifyDataSetChanged()
//    }

    fun getData(): MutableList<TimeAndMeasure> {
        return timeAndMeasureList

    }

    fun addData(timeAndMeasure: TimeAndMeasure) {
        // this.measuresMedList
        timeAndMeasureList.add(timeAndMeasure)
        notifyDataSetChanged()
    }

    fun addAllData(timeAndMeasure: List<TimeAndMeasure>) {
        // this.measuresMedList
        timeAndMeasureList.addAll(timeAndMeasure)
        notifyDataSetChanged()
    }

}
