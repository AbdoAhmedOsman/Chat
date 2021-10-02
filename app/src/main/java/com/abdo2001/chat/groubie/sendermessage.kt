package com.abdo2001.chat.groubie

import android.content.Context
import android.text.format.DateFormat
import com.abdo2001.chat.R
import com.abdo2001.chat.frist.textmessage
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.itemmessager.*

class sendermessage(val textmessage: textmessage
                    , val messageid:String
                    , val context: Context):Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.tvmessageAbdo.text=textmessage.text
        viewHolder.tvtime.text=DateFormat.format("hh:mm a",textmessage.data)


    }

    override fun getLayout(): Int {
      return R.layout.itemmessager


    }
}