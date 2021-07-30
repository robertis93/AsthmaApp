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
import com.example.asthmaapp.view.fragments.InformationListFragmentDirections
import com.example.asthmaapp.viewmodel.viewModels.InformationListViewModel

class MedicamentAnalysesAdapter(val viewModel: InformationListViewModel) : RecyclerView.Adapter<MedicamentAnalysesAdapter.MeasureViewHolder>() {

    private var dayMeasureAndMedicamentList = listOf<MeasureWithTakeMedicamentTime>()
    private var measureList = emptyList<Measure>()

    fun setData(dayWithMeasures: List<MeasureWithTakeMedicamentTime>) {
        this.dayMeasureAndMedicamentList = dayWithMeasures
        notifyDataSetChanged()
    }

    fun addMeasures(measures: List<Measure>) {
        this.measureList = measures
    }

    override fun getItemCount(): Int {
        return dayMeasureAndMedicamentList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeasureViewHolder {
        val binding = CustomRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MeasureViewHolder(binding)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MeasureViewHolder, position: Int) {
        val dayCurrentItem = dayMeasureAndMedicamentList[position]

        holder.binding.dayTextView.text =
            dayCurrentItem.date
        holder.itemView.setOnClickListener {
            val action =
                InformationListFragmentDirections.actionListFragmentToAddFragment(dayCurrentItem)
            holder.binding.root.findNavController().navigate(action)
        }

        val toUpdateMeasureFragmentFromMeasureClickListener: MeasureAdapter.OnClickListener =
            object : MeasureAdapter.OnClickListener {
                override fun actionClick() {
                    val action =
                        InformationListFragmentDirections.actionListFragmentToAddFragment(dayCurrentItem)
                    holder.binding.root.findNavController().navigate(action)
                }
            }
        val toUpdateMeasureFragmentFromTakeMedicamentClickListenerTime: TakeMedicamentTimeAdapter.OnClickListener =
            object : TakeMedicamentTimeAdapter.OnClickListener {
                override fun actionClick() {
                    val action =
                        InformationListFragmentDirections.actionListFragmentToAddFragment(
                            dayCurrentItem
                        )
                    holder.binding.root.findNavController().navigate(action)
                }
            }

        val measureAdapter =
            MeasureAdapter(dayCurrentItem.measureList, viewModel, toUpdateMeasureFragmentFromMeasureClickListener)
        val recyclerView = holder.binding.rowRecyclerView
        recyclerView.adapter = measureAdapter
        recyclerView.layoutManager = GridLayoutManager(
            holder.binding.rowRecyclerView.context,
            1,
            LinearLayoutManager.VERTICAL,
            false
        )

        val listTakeMedicamentTimeAdapter =
            TakeMedicamentTimeAdapter(
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