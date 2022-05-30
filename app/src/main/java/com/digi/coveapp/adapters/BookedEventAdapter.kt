package com.digi.coveapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.digi.coveapp.R
import com.digi.coveapp.databinding.ListItemBinding
import com.digi.coveapp.listener.OnBookedEventItemClickListener
import com.digi.coveapp.models.Transaction

class BookedEventAdapter(
    private val transactionList: ArrayList<Transaction>,
    private val listener: OnBookedEventItemClickListener
) : RecyclerView.Adapter<BookedEventAdapter.Holder>() {
    class Holder(iv: View) : RecyclerView.ViewHolder(iv) {
        val binding = ListItemBinding.bind(iv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val trans = transactionList[position]
        holder.binding.tvHeading.text = trans.eventName
        holder.binding.container.tag = trans
        holder.binding.container.setOnClickListener {
            listener.onEventCLick(it, trans)
        }
    }

    override fun getItemCount(): Int {
        return transactionList.size
    }
}