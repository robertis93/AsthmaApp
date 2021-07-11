package com.example.asthmaapp.view.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.asthmaapp.databinding.CustomRowBinding
import com.example.asthmaapp.model.Measure
import com.example.asthmaapp.model.MeasureWithTakeMedicamentTime
import com.example.asthmaapp.viewmodel.viewModels.TakeMedicamentTimeGroupByDate

class MeasureListAdapter : RecyclerView.Adapter<MeasureListAdapter.MeasureViewHolder>() {

    private var dayMedicamentList = listOf<MeasureWithTakeMedicamentTime>()
    private var timeMeasureList = emptyList<Measure>()
    private var emptytimeMeasureList = mutableListOf<TakeMedicamentTimeGroupByDate>()


    fun setData(dayWithMeasures: List<MeasureWithTakeMedicamentTime>) {
        this.dayMedicamentList = dayWithMeasures
        notifyDataSetChanged()
    }

    fun addMeasure(measure: List<Measure>) {
        this.timeMeasureList = measure
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

        holder.binding.dayTextView.text = dayCurrentItem.date
//        val adaaaa =dayCurrentItem.keys
//        holder.itemView.setOnClickListener {
//            val action =
//                MeasureListFragmentDirections.actionListFragmentToUpdateFragment(dayCurrentItem.values.size)
//            holder.binding.root.findNavController().navigate(action)
//        }

        val measureClickListener: MeasureAdapter.OnClickListener =
            object : MeasureAdapter.OnClickListener {
                override fun actionClick() {
//                    val action =
//                        MeasureListFragmentDirections.actionListFragmentToUpdateFragment(
//                            dayCurrentItem
//                        )
//                    holder.binding.root.findNavController().navigate(action)
                }
            }

        val medTimeTakeMedicamentClickListener: ListTimeTakeMedicamentAdapter.OnClickListener =
            object : ListTimeTakeMedicamentAdapter.OnClickListener {
                override fun actionClick() {
//                    val action =
//                        MeasureListFragmentDirections.actionListFragmentToUpdateFragment(
//                            dayCurrentItem
//                        )
                   // holder.binding.root.findNavController().navigate(action)
                }
            }

     //   val measuresAll = dayMedicamentList
        //val timeDrugs = dayCurrentItem.timeTakeMedicament
//        val timeDrugs = dayMedicamentList
//        for (time in timeDrugs){
//            if (time.dateTimeStamp == adaaaa){
//emptytimeMeasureList.add(time)
//            }
//        }

        val measureAdapter = MeasureAdapter(dayCurrentItem.measureList, timeMeasureList, measureClickListener)
        val recyclerView = holder.binding.rowRecyclerView
        recyclerView.adapter = measureAdapter
        recyclerView.layoutManager = GridLayoutManager(
            holder.binding.rowRecyclerView.context,
            1,
            LinearLayoutManager.VERTICAL,
            false
        )

        val medTimeAdapter =
            ListTimeTakeMedicamentAdapter(dayCurrentItem.takeMedicamentTimeList, medTimeTakeMedicamentClickListener)
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