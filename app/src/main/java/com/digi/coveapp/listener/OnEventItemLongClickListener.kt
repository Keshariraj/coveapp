package com.digi.coveapp.listener

import android.view.View
import com.digi.coveapp.models.Event

interface OnEventItemLongClickListener {
    fun onEventLongCLick(view: View,event: Event)
}