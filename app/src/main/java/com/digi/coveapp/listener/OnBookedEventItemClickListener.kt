package com.digi.coveapp.listener

import android.view.View
import com.digi.coveapp.models.Transaction

interface OnBookedEventItemClickListener {
    fun onEventCLick(view: View, transaction: Transaction)
}