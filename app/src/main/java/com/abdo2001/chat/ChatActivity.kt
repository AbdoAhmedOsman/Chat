package com.abdo2001.chat

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import com.abdo2001.chat.frist.*
import com.abdo2001.chat.groubie.recivermessage
import com.abdo2001.chat.groubie.sendermessage
import com.abdo2001.chat.groubie.sendphoto
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.itemmessager.*
import java.io.ByteArrayOutputStream
import java.util.*

class ChatActivity : AppCompatActivity() {
    private lateinit var mcurrentchanal: String

    //firebase
    //firestore
    private val firestor:FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }
    private val documentref2:DocumentReference
   get() = firestor.document("users/${FirebaseAuth.getInstance().currentUser?.uid.toString()}")
    private val doucmenref=firestor.collection("chatchanal")
    private val userinforef:DocumentReference
    get() = firestor.document("users/")


    //firebase storge

    private val firesorge:FirebaseStorage by lazy {
            FirebaseStorage.getInstance()
        }
    private lateinit var ref:StorageReference
    private val strogref:StorageReference
    get() = firesorge.reference.child(FirebaseAuth.getInstance().currentUser?.uid.toString())
    //varse
    private lateinit var getphoto:String

    private val currentuser=FirebaseAuth.getInstance().currentUser!!.uid
   private var otheruid=""
    private val messageadapter by lazy { GroupAdapter<ViewHolder>() }
    companion object{
        val requestCode1=2
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        //Toolbar
        setSupportActionBar(toolbar2)
        supportActionBar?.title=""
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //Intent
        val name =  intent.getStringExtra("user")
        val image= intent.getStringExtra("profileimage")
         otheruid= intent.getStringExtra("uid")!!
        tvnamech.text=name
        if (image!!.isNotEmpty()){
            Glide.with(this)
                    .load(firesorge.getReference(image))
                    .into(profile_image2)
        }else{
            profile_image2.setBackgroundResource(R.drawable.person)
        }
        getuserinfo {

        }
        chatchanal{chanalid->
             mcurrentchanal = chanalid
            getmessage(chanalid)
                btsendmessagech.setOnClickListener{
                    val text=etmessagech.text.toString()
                    if (text.isNotEmpty()){
                    val textmessage= textmessage(text,currentuser,otheruid,"","",Calendar.getInstance().time )
                    sendmessage(chanalid,textmessage)
                    etmessagech.setText("")
                    }else{
                        Toast.makeText(this, "Empty", Toast.LENGTH_SHORT).show()
                    }
                }

           
        }

        rvshowch.apply {
            adapter=messageadapter
        }

        cimessage.setOnClickListener {
            val getimage=Intent().apply {
                type="image/*"
                action=Intent.ACTION_GET_CONTENT
                putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg","image/png"))
            }
            startActivityForResult(Intent.createChooser(getimage,"selectimage"), requestCode1)
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode==RESULT_OK &&requestCode== requestCode1
                &&data!=null&& data.data!=null){
            val imageuri=data.data
            val imagetobmp=MediaStore.Images.Media.getBitmap(this.contentResolver,imageuri)
            val outstreem=ByteArrayOutputStream()
            imagetobmp.compress(Bitmap.CompressFormat.JPEG,25,outstreem)
            val outtobyte=outstreem.toByteArray()
            uplaodsendimage(outtobyte)
            val imagemessage=imagemessage(ref.path,currentuser,otheruid
                    ,"","",
            Calendar.getInstance().time)
            doucmenref.document(mcurrentchanal).collection("message").add(imagemessage)

            sendmessage(mcurrentchanal,imagemessage)


        }

    }

    private fun uplaodsendimage(outtobyte: ByteArray) {

      ref=  strogref.child("${FirebaseAuth.getInstance().currentUser!!.uid}/images/${UUID.nameUUIDFromBytes(outtobyte)}")
               ref .putBytes(outtobyte).addOnCompleteListener(this) {
                    if (it.isSuccessful){
                        Toast.makeText(applicationContext, "Done", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(applicationContext, "False", Toast.LENGTH_SHORT).show()
                    }
                }
        ref.path
    }

    private fun sendmessage(chanalid: String,textmessage: messagetext) {
        doucmenref.document(chanalid).collection("message").add(textmessage)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbaricon2,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home->{ finish()
                return true}
            R.id.video->{ }
            R.id.info->{}

        }
        return false
    }
    private fun chatchanal(oncomplete:(chanalid:String)->Unit){
        firestor.collection("users")
            .document(FirebaseAuth.getInstance().currentUser!!.uid)
            .collection("chatchanal")
            .document(otheruid)
            .get()
            .addOnSuccessListener {
                if (it.exists()) {
                    oncomplete(it["chanalid"] as String)
                    return@addOnSuccessListener
                }


                val currentuser = FirebaseAuth.getInstance().currentUser?.uid
                val chatchanal1 = firestor.collection("users").document()
                firestor.collection("users")
                    .document(otheruid)
                    .collection("chatchanal")
                    .document(currentuser!!)
                    .set(mapOf("chanalid" to chatchanal1.id))
                firestor.collection("users")
                    .document(currentuser)
                    .collection("chatchanal")
                    .document(otheruid)
                    .set(mapOf("chanalid" to chatchanal1.id))
                oncomplete(chatchanal1.id)
            }
    }
    private fun getmessage(chanalid:String){
      val quary=  doucmenref.document(chanalid).collection("message")
          .orderBy("data",Query.Direction.DESCENDING)
        quary.addSnapshotListener { value, error ->
            messageadapter.clear()
            value!!.documents.forEach {
                if (it["type"]==typemessage.TEXT){
                    val texemessage=it.toObject(textmessage::class.java)
                    if (texemessage?.gsenderid==currentuser){
                        messageadapter.add(sendermessage
                        (it.toObject(textmessage::class.java)!!,it.id,this))
                    }else{
                        messageadapter.add(recivermessage
                        (it.toObject(textmessage::class.java)!!,it.id,this))
                    }
                }else{
                    val messageimage=it.toObject(imagemessage::class.java)
                    messageadapter.add(sendphoto(it.toObject(imagemessage::class.java)!!,it.id,this))

                }

            }
        }

    }
    private fun getuserinfo(oncomplete:(user)->Unit){
        documentref2.get().addOnSuccessListener {
            oncomplete(it.toObject(user::class.java)!!)
        }

    }


}