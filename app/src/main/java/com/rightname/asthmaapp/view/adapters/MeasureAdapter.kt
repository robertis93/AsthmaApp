package com.rightname.asthmaapp.view.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rightname.asthmaapp.databinding.InnerItemBinding
import com.rightname.asthmaapp.model.Measure
import com.rightname.asthmaapp.utils.DateUtil.timestampToDisplayTime
import com.rightname.asthmaapp.viewmodel.viewModels.InformationListViewModel

class MeasureAdapter(
    private var measureList: List<Measure>,
    val viewModel: InformationListViewModel,
    private var onClickListener: OnClickListener
) :
    RecyclerView.Adapter<MeasureAdapter.MeasureViewHolder>() {

    interface OnClickListener {
        fun actionClick()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeasureViewHolder {
        val binding = InnerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MeasureViewHolder(binding)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MeasureViewHolder, position: Int) {
        val currentItem = measureList[position]
        holder.binding.timeTextView.text =
            timestampToDisplayTime(currentItem.dateTimestamp)
        val color = viewModel.getColorForMeasure(currentItem)
        if (color != null)
            holder.binding.measuresTextView.setBackgroundColor(holder.itemView.context.getColor(color))
        holder.binding.measuresTextView.text = currentItem.value.toString()
        holder.itemView.setOnClickListener {
            onClickListener.actionClick()
        }
    }

    override fun getItemCount(): Int {
        return measureList.size
    }

    class MeasureViewHolder(val binding: InnerItemBinding) : RecyclerView.ViewHolder(binding.root)
}
