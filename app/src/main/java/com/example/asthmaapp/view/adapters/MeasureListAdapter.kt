package com.example.asthmaapp.view.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.asthmaapp.databinding.CustomRowBinding
import com.example.asthmaapp.model.Measure
import com.example.asthmaapp.model.MeasureWithTakeMedicamentTime
import com.example.asthmaapp.view.fragments.MeasureListFragmentDirections

class MeasureListAdapter : RecyclerView.Adapter<MeasureListAdapter.MeasureViewHolder>() {

    private var dayMedicamentList = listOf<MeasureWithTakeMedicamentTime>()
    private var listMeasure = emptyList<Measure>()

    fun setData(dayWithMeasures: List<MeasureWithTakeMedicamentTime>) {
        this.dayMedicamentList = dayWithMeasures
        notifyDataSetChanged()
    }

    fun addMeasure(measure: List<Measure>) {
        this.listMeasure = measure
    }

    override fun getItemCount(): Int {
        return dayMedicamentList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeasureViewHolder {
        val binding = CustomRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MeasureViewHolder(binding)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MeasureViewHolder, position: Int) {
        val dayCurrentItem = dayMedicamentList[position]

        holder.binding.dayTextView.text =
            com.example.asthmaapp.utils.millisecondsToStringDateDayMonthYear(dayCurrentItem.dateTimestamp)
        holder.itemView.setOnClickListener {
            val action =
                MeasureListFragmentDirections.actionListFragmentToUpdateFragment(dayCurrentItem)
            holder.binding.root.findNavController().navigate(action)
        }

        val toUpdateMeasureFragmentFromMeasureClickListener: MeasureAdapter.OnClickListener =
            object : MeasureAdapter.OnClickListener {
                override fun actionClick() {
                    val action =
                        MeasureListFragmentDirections.actionListFragmentToUpdateFragment(
                            dayCurrentItem
                        )
                    holder.binding.root.findNavController().navigate(action)
                }
            }
        val toUpdateMeasureFragmentFromTakeMedicamentClickListenerTime: ListTakeMedicamentTimeAdapter.OnClickListener =
            object : ListTakeMedicamentTimeAdapter.OnClickListener {
                override fun actionClick() {
                    val action =
                        MeasureListFragmentDirections.actionListFragmentToUpdateFragment(
                            dayCurrentItem
                        )
                    holder.binding.root.findNavController().navigate(action)
                }
            }

        val measureAdapter =
            MeasureAdapter(dayCurrentItem.measureList, listMeasure, toUpdateMeasureFragmentFromMeasureClickListener)
        val recyclerView = holder.binding.rowRecyclerView
        recyclerView.adapter = measureAdapter
        recyclerView.layoutManager = GridLayoutManager(
            holder.binding.rowRecyclerView.context,
            1,
            LinearLayoutManager.VERTICAL,
            false
        )

        val listTakeMedicamentTimeAdapter =
            ListTakeMedicamentTimeAdapter(
                dayCurrentItem.takeMedicamentTimeList,
                toUpdateMeasureFragmentFromTakeMedicamentClickListenerTime
            )
        val recyclerViewMedTime = holder.binding.timeMedicalRecyclerView
        recyclerViewMedTime.adapter = listTakeMedicamentTimeAdapter
        recyclerViewMedTime.layoutManager = GridLayoutManager(
            holder.binding.timeMedicalRecyclerView.context,
            3,
            LinearLayoutManager.HORIZONTAL,
            false
        )
    }

    class MeasureViewHolder(val binding: CustomRowBinding) : RecyclerView.ViewHolder(binding.root)
}