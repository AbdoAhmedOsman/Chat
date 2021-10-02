package com.abdo2001.chat.groubie

import android.content.Context
import android.text.format.DateFormat
import com.abdo2001.chat.R
import com.abdo2001.chat.frist.imagemessage
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.senderphoto.*

class receviephoto(val imagemessage: imagemessage,context: Context):Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.tvtime.text= DateFormat.format("hh:mm a",imagemessage.data)


    }

    override fun getLayout(): Int {
        return R.layout.reciverphoto
    }
}