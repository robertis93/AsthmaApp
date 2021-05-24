package com.example.asthmaapp.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.asthmaapp.R
import com.example.asthmaapp.model.models.Alarm

class AlarmAdapter(private val alarmes: List<String>) : RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder>() {
        private var alarmList  = emptyList<Alarm>()


    class AlarmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var timeTextView: TextView? = null
        //var switchView: Switch? = null
//выполняются при инициализации объекта сразу же после вызова конструктора и снабжаются префиксом init.
        init {
            timeTextView = itemView.findViewById(R.id.item_txt)
          //  switchView = itemView.findViewById(R.id.item_switch)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_alarm, parent, false)
        return AlarmViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        val currentItem = alarmes[position]
        holder.timeTextView?.text = alarmes[position]
        //holder.timeTextView?.text = "${currentItem.minute.toString()} : ${currentItem.hour}"

    }

    override fun getItemCount(): Int {
        return alarmes.size
    }
}