package com.digi.coveapp.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.digi.coveapp.R
import com.digi.coveapp.listener.OnEventItemClickListener
import com.digi.coveapp.models.Event

class EventViewerAdapter(
    private val newList: ArrayList<Event>,
    private val listener: OnEventItemClickListener
) :
    RecyclerView.Adapter<EventViewerAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.card_event_viewer, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentItem: Event = newList[position]
        holder.tvHeading.text = currentItem.eventName
        Glide.with(holder.img).load(currentItem.banner).into(holder.img)
        holder.container.tag = currentItem
        holder.btnView.tag = currentItem
        holder.container.setOnClickListener {
            val event: Event = it.tag as Event
            listener.onEventClick(it, event)
        }
        holder.btnView.setOnClickListener {
            val event: Event = it.tag as Event
            listener.onEventClick(it, event)
        }
    }

    override fun getItemCount(): Int {
        return newList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvHeading: TextView = itemView.findViewById(R.id.textEvent)
        val container: ViewGroup = itemView.findViewById(R.id.container)
        val img: ImageView = itemView.findViewById(R.id.imgBanner)
        val btnView: Button = itemView.findViewById(R.id.btnView)
    }
}

