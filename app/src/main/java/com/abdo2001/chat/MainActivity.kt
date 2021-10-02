package com.abdo2001.chat

import com.abdo2001.chat.Fragment.ChatFragment
import com.abdo2001.chat.Fragment.FriendFragment
import com.abdo2001.chat.Fragment.MoreFragment
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.abdo2001.chat.frist.user
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() ,BottomNavigationView.OnNavigationItemSelectedListener{
    private val mauth:FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    private val chatfragmen=ChatFragment()
    private val morefragment=MoreFragment()
    private val friendfragment=FriendFragment()
    private val firecloud:FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }
    private val storge:FirebaseStorage by lazy {
        FirebaseStorage.getInstance()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        firecloud.collection("users")
                .document(FirebaseAuth.getInstance().currentUser?.uid.toString())
                .get()
                .addOnSuccessListener {
                    val user=it.toObject(user::class.java)
                    if (user!!.profileimage.isNotEmpty()){
                        Glide.with(this)
                                .load(storge.getReference(user.profileimage))
                                .placeholder(R.drawable.person)
                                .into(profile_image)
                    }else{
                        profile_image.setImageResource(R.drawable.person)
                    }

                }

        bottomNavigationView.setOnNavigationItemSelectedListener(this)
        setfragment(chatfragmen)
        setSupportActionBar(toolbar3)
        supportActionBar?.title=""
    }

    override fun onStart() {
        super.onStart()
        if (mauth?.currentUser==null){
            val i = Intent(this,Signup::class.java)
            startActivity(i)
        }else{
            Toast.makeText(this,"Welcome", Toast.LENGTH_SHORT).show()

        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbaricon,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        var id =item?.itemId
        if (id==R.id.camera){
        }
        if (id==R.id.edit){

        }
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.mchat1->{
                setfragment(chatfragmen)
                return true
            }
            R.id.mfriend->{
                setfragment(friendfragment)
                return true
            }
            R.id.mmore->{
                setfragment(morefragment)
                return true
            }
            else->return false
        }
    }

    private fun setfragment(fragment: Fragment) {
        val ft=supportFragmentManager.beginTransaction()
        ft.replace(R.id.constraintlayout,fragment)
        ft.commit()
    }
}