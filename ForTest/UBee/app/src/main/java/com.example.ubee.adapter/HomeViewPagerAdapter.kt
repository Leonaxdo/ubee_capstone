package com.example.ubee.adapter

import android.content.Intent
import android.media.metrics.Event
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.ubee.EventActivity
import com.example.ubee.R
import com.example.ubee.data.eventData

class HomeViewPagerAdapter: RecyclerView.Adapter<HomeViewPagerAdapter.ViewHolder>(){

    private val dataset = eventData.newEvent

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.event_textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context).inflate(R.layout.event_list_item, parent, false)

        return ViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataset[position]

        holder.itemView.setOnClickListener{
            val intent = Intent(holder.itemView?.context, EventActivity::class.java)
            ContextCompat.startActivity(holder.itemView.context, intent, null)
        }
        holder.title.text = item.title
    }

    override fun getItemCount(): Int {
        return dataset.size
    }
}