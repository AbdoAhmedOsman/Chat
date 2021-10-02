package com.abdo2001.chat.groubie

import android.content.Context
import com.abdo2001.chat.R
import com.abdo2001.chat.frist.textmessage
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.itemmessager.*
import kotlinx.android.synthetic.main.itemmessager.tvtime
import kotlinx.android.synthetic.main.itmemessagerrecivers.*

class recivermessage (val textmessage: textmessage
                      , val messageid:String
                      , val context: Context
): Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.tvmessageAsha.text=textmessage.text
        viewHolder.tvtime.text= android.text.format.DateFormat.format("hh:mm a",textmessage.data)


    }

    override fun getLayout(): Int {
        return R.layout.itmemessagerrecivers


    }
}