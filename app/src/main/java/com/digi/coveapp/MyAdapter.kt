package com.digi.coveapp


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.digi.coveapp.models.Event
import com.google.android.material.imageview.ShapeableImageView

class MyAdapter(private val newList: ArrayList<Event>) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapter.MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyAdapter.MyViewHolder, position: Int) {

        val currentItem = newList[position]
        holder.tvHeading.text = currentItem.eventName
        Glide.with(holder.titleImage).load(currentItem.banner).into(holder.titleImage)
        holder.conatiner.tag = currentItem
        holder.conatiner.setOnClickListener {
            val event:Event = it.tag as Event
//            todo navigate to event detail
        }
    }

    override fun getItemCount(): Int {

        return newList.size

    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleImage: ShapeableImageView = itemView.findViewById(R.id.title_image)
        val tvHeading: TextView = itemView.findViewById(R.id.tvHeading)
        val conatiner:ViewGroup = itemView.findViewById(R.id.container)


    }
}

