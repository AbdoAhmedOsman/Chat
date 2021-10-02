package com.abdo2001.chat.groubie

import android.content.Context
import com.abdo2001.chat.R
import com.abdo2001.chat.frist.user
import com.bumptech.glide.Glide
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.recycler_view_item.*

class groubie(val uid:String, val user:user,
              val context:Context):Item() {
    private val databaseinstance:FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }
    private val doucmenref:DocumentReference
    get()=databaseinstance.document("users/$uid")
    private val firestore: FirebaseStorage by lazy {
        FirebaseStorage.getInstance()
    }
    override fun bind(viewHolder: ViewHolder, position: Int) {

        viewHolder.textView2.text="lastmessage"
        viewHolder.textView3.text="time"

        getinf{user->
            viewHolder.textView.text=user.name
            if (user.profileimage.isNotEmpty()){
                Glide.with(context)
                        .load(firestore.getReference(user.profileimage))
                        .placeholder(R.drawable.person)
                        .into(viewHolder.imageView2)
            }else{
                viewHolder.imageView2.setImageResource(R.drawable.person)
            }
        }

    }

    private fun getinf(function: (user) -> Unit) {
        doucmenref.get().addOnSuccessListener {
            function(it.toObject(user::class.java)!!)
        }

    }

    override fun getLayout(): Int {
        return R.layout.recycler_view_item
    }
}