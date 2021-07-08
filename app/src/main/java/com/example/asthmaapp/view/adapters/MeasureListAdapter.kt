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
import com.example.asthmaapp.utils.longToStringCalendar
import com.example.asthmaapp.view.fragments.MeasureListFragmentDirections

class MeasureListAdapter : RecyclerView.Adapter<MeasureListAdapter.MeasureViewHolder>() {

    private var dayMeasureList = emptyList<MedWithMeasuresAndMedicamentTime>()
    private var timeMeasureList = emptyList<TimeAndMeasure>()

    fun setDayData(dayWithMeasures: List<MedWithMeasuresAndMedicamentTime>) {
        this.dayMeasureList = dayWithMeasures
        notifyDataSetChanged()
    }

    fun addTimeAndMeasure(timeAndMeasure: List<TimeAndMeasure>) {
        this.timeMeasureList = timeAndMeasure
    }

    override fun getItemCount(): Int {
        return dayMeasureList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeasureViewHolder {
        val binding = CustomRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MeasureViewHolder(binding)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MeasureViewHolder, position: Int) {
        val dayCurrentItem = dayMeasureList[position]

        holder.binding.dayTextView.text = longToStringCalendar(dayCurrentItem.day.day)
        holder.itemView.setOnClickListener {
            val action =
                MeasureListFragmentDirections.actionListFragmentToUpdateFragment(dayCurrentItem)
            holder.binding.root.findNavController().navigate(action)
        }

        val measureClickListener: MeasureAdapter.OnClickListener =
            object : MeasureAdapter.OnClickListener {
                override fun actionClick() {
                    val action =
                        MeasureListFragmentDirections.actionListFragmentToUpdateFragment(
                            dayCurrentItem
                        )
                    holder.binding.root.findNavController().navigate(action)
                }
            }

        val medTimeTakeMedicamentClickListener: ListTimeTakeMedicamentAdapter.OnClickListener =
            object : ListTimeTakeMedicamentAdapter.OnClickListener {
                override fun actionClick() {
                    val action =
                        MeasureListFragmentDirections.actionListFragmentToUpdateFragment(
                            dayCurrentItem
                        )
                    holder.binding.root.findNavController().navigate(action)
                }
            }

        val measuresAll = dayCurrentItem.measures
        val timeDrugs = dayCurrentItem.medicamentTime

        val measureAdapter = MeasureAdapter(measuresAll, timeMeasureList, measureClickListener)
        val recyclerView = holder.binding.rowRecyclerView
        recyclerView.adapter = measureAdapter
        recyclerView.layoutManager = GridLayoutManager(
            holder.binding.rowRecyclerView.context,
            1,
            LinearLayoutManager.VERTICAL,
            false
        )

        val medTimeAdapter =
            ListTimeTakeMedicamentAdapter(timeDrugs, medTimeTakeMedicamentClickListener)
        val recyclerViewMedTime = holder.binding.timeMedicalRecyclerView
        recyclerViewMedTime.adapter = medTimeAdapter
        recyclerViewMedTime.layoutManager = GridLayoutManager(
            holder.binding.timeMedicalRecyclerView.context,
            3,
            LinearLayoutManager.HORIZONTAL,
            false
        )
    }

    class MeasureViewHolder(val binding: CustomRowBinding) : RecyclerView.ViewHolder(binding.root)
}