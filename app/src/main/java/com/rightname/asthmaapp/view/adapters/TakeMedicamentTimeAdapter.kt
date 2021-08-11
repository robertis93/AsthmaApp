package com.rightname.asthmaapp.view.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rightname.asthmaapp.databinding.InnerItemMedtimeBinding
import com.rightname.asthmaapp.model.TakeMedicamentTime
import com.rightname.asthmaapp.utils.DateUtil.timestampToDisplayTime


class TakeMedicamentTimeAdapter(
    private var takeMedicamentTimeList: List<TakeMedicamentTime>,
    private var onClickListener: OnClickListener
) :
    RecyclerView.Adapter<TakeMedicamentTimeAdapter.TimeTakeMedicamentViewHolder>() {

    interface OnClickListener {
        fun actionClick()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TimeTakeMedicamentViewHolder {
        val binding =
            InnerItemMedtimeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TimeTakeMedicamentViewHolder(binding)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: TimeTakeMedicamentViewHolder, position: Int) {
        val currentItem = takeMedicamentTimeList[position]
        holder.binding.timeTextView.text = timestampToDisplayTime(currentItem.takeMedicamentTimeEntity.dateTimestamp)
        holder.itemView.setOnClickListener {
            onClickListener.actionClick()
        }
    }

    override fun getItemCount(): Int {
        return takeMedicamentTimeList.size
    }

    class TimeTakeMedicamentViewHolder(val binding: InnerItemMedtimeBinding) :
        RecyclerView.ViewHolder(binding.root)
}