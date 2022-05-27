package com.digi.coveapp.listener

import android.view.View
import com.digi.coveapp.models.Event

interface OnEventItemClickListener {
    fun onEventCLick(view: View,event: Event)
}