package com.rightname.asthmaapp.view.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rightname.asthmaapp.databinding.UpdateMeasuresInnerItemBinding
import com.rightname.asthmaapp.model.Measure
import com.rightname.asthmaapp.utils.DateUtil.timestampToDisplayTime

class EditMeasureAdapter(
    private val listener: ClickListener,
    private val isInUpdateMode: Boolean = false
) :
    RecyclerView.Adapter<EditMeasureAdapter.AddMeasureViewHolder>() {

    private var measureList = emptyList<Measure>()

    interface ClickListener {
        fun onDeleteMeasureClick(measure: Measure)
        fun onUpdateMeasureClick(measure: Measure, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddMeasureViewHolder {
        val binding = UpdateMeasuresInnerItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AddMeasureViewHolder(binding)
    }

    @SuppressLint("ResourceAsColor", "SetTextI18n")
    override fun onBindViewHolder(holder: AddMeasureViewHolder, position: Int) {
        val currentItem = measureList[position]
        with(holder.binding) {
            timeTextView.text =
                timestampToDisplayTime(currentItem.dateTimestamp)
            measureText.text = currentItem.value.toString()
            deleteIcon.setOnClickListener {
                listener.onDeleteMeasureClick(measureList[position])
            }
            editIcon.visibility = if (isInUpdateMode)
                View.VISIBLE
            else
                View.GONE
            editIcon.setOnClickListener {
                listener.onUpdateMeasureClick(currentItem, position)
            }
        }
    }

    override fun getItemCount(): Int {
        return measureList.size
    }

    fun setData(measures: List<Measure>) {
        this.measureList = measures
        notifyDataSetChanged()
    }

    class AddMeasureViewHolder(val binding: UpdateMeasuresInnerItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}