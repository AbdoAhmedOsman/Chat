package com.abdo2001.chat.groubie

import android.content.Context
import android.text.format.DateFormat
import com.abdo2001.chat.R
import com.abdo2001.chat.frist.imagemessage
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.senderphoto.*

class sendphoto(val imagemessage: imagemessage,val messageid:String,val context: Context):Item() {
    private val firesorge: FirebaseStorage by lazy {
        FirebaseStorage.getInstance()
    }
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.tvtime.text= DateFormat.format("hh:mm a",imagemessage.data)
        if (imagemessage.image.isNotEmpty()){
            Glide.with(context)
                    .load(firesorge.getReference(imagemessage.image))
                    .placeholder(R.drawable.person)
                    .into(viewHolder.ivmessageasbdo)
        }

    }

    override fun getLayout(): Int {
        return R.layout.senderphoto
    }
}