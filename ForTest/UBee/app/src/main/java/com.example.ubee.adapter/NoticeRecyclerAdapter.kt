package com.example.ubee.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ubee.R
import com.example.ubee.data.noticeData
import kotlinx.coroutines.NonDisposableHandle.parent

class NoticeRecyclerAdapter : RecyclerView.Adapter<NoticeRecyclerAdapter.BoardListViewHolder>() {

    private val dataset = noticeData.newNotice

    class BoardListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val boardTitle: TextView = view.findViewById(R.id.board_title)
        val boardContent: TextView = view.findViewById(R.id.board_content)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardListViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.eventlist_layout, parent, false)

        return BoardListViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: BoardListViewHolder, position: Int) {
        val item = dataset[position]

        holder.boardTitle.text = item.title
        holder.boardContent.text = item.content
    }

    override fun getItemCount() = dataset.size
}