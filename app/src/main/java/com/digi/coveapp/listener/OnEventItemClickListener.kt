package com.digi.coveapp.listener

import android.view.View
import com.digi.coveapp.models.Event

interface OnEventItemClickListener {
    fun onEventClick(view: View, event: Event)
}
