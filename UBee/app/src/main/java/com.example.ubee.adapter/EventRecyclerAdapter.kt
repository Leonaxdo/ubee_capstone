package com.example.ubee.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ubee.R
import com.example.ubee.data.eventData


class EventRecyclerAdapter : RecyclerView.Adapter<EventRecyclerAdapter.BoardListViewHolder>() {

    private val dataset = eventData.newEvent

    /**
     * Initialize view elements
     */
    class BoardListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val boardTitle: TextView = view.findViewById(R.id.board_title)
        val boardContent: TextView = view.findViewById(R.id.board_content)
    }

    /**
     * Create new views
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardListViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.eventlist_layout, parent, false)

        return BoardListViewHolder(adapterLayout)
    }

    /**
     * Replace the contents of a view
     */
    override fun onBindViewHolder(holder: BoardListViewHolder, position: Int) {
        val item = dataset[position]

        holder.boardTitle.text = item.title         // 글 제목
        holder.boardContent.text = item.content     // 글 내용
    }

    override fun getItemCount() = dataset.size
}