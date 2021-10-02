package com.abdo2001.chat

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.widget.Toast
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.abdo2001.chat.frist.user
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.activity_profileactivity.*
import java.io.ByteArrayOutputStream
import java.util.*

class profileactivity : AppCompatActivity() {
    companion object{
        val rcselectimage=2
    }
    private val mauth:FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    private lateinit var username:String
    private val firecloud:FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }
    private val documentref:DocumentReference
        get() = firecloud.document("users/${FirebaseAuth.getInstance().currentUser?.uid.toString()}")

    private val firestore:FirebaseStorage by lazy {
        FirebaseStorage.getInstance()
    }
    private val storageref:StorageReference
    get()=firestore.reference.child(FirebaseAuth.getInstance().currentUser?.uid.toString())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profileactivity)
        setSupportActionBar(toolbar)
        supportActionBar?.title="me"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)



        btlogout.setOnClickListener {
            mauth.signOut()
            val i=Intent(this,Signup::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(i)
        }
        getuser {user ->
            username= user.name
            tvusername.text=username
            if (user.profileimage.isNotEmpty()){
               Glide.with(this)
                    .load(firestore.getReference(user.profileimage))
                    .placeholder(R.drawable.person)
                    .into(profile_image)

            }


        }
        profile_image.setOnClickListener {
            val getimage=Intent().apply {
                type="image/*"
                action=Intent.ACTION_GET_CONTENT
                putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg","image/png"))
            }
            startActivityForResult(Intent.createChooser( getimage,"select image"), rcselectimage)
        }



    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode== rcselectimage&&resultCode== Activity.RESULT_OK&&
                data!=null&&data.data!=null){
                    progressBar.isVisible=true
            profile_image.setImageURI(data.data)

            val selectmessagepath=data.data
            val selectedimagbmp=MediaStore.Images.Media.getBitmap(this.contentResolver, selectmessagepath)
            val outputStream=ByteArrayOutputStream()
            selectedimagbmp.compress(Bitmap.CompressFormat.JPEG,20,outputStream)
            val selectedimagebytes=outputStream.toByteArray()
            uploadprofileimage(selectedimagebytes){ path->
                val userfilmap= mutableMapOf<String , Any>()
                userfilmap["name"]=username
              userfilmap["profileimage"]=path
                documentref.update(userfilmap)
            }

        }
    }

    private fun uploadprofileimage(selectedimagebytes: ByteArray ,onSuccess:(imagepath:String) -> Unit) {
       val ref = storageref.child("profilepicture/${UUID.nameUUIDFromBytes(selectedimagebytes)}")
        ref.putBytes(selectedimagebytes).addOnCompleteListener(this){
            if (it.isSuccessful){
                onSuccess(ref.path)
                progressBar.isVisible=false

            }else{
                Toast.makeText(this, it.exception?.message, Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
           android.R.id.home->{
               finish()
               return true
           }
        }
        return false
    }
    private fun getuser(onComplete:(user)->Unit){
        documentref.get().addOnSuccessListener {
          onComplete(it.toObject(user::class.java)!!)
        }
    }
}