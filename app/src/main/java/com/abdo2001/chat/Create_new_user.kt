package com.abdo2001.chat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.abdo2001.chat.frist.user
import com.google.android.gms.ads.*

import kotlinx.android.synthetic.main.activity_create_new_user.*

class Create_new_user : AppCompatActivity()  {
    private val mauth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    private val firebasestore:FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }
    private val currenuserdata:DocumentReference
    get()=firebasestore.document("users/${FirebaseAuth.getInstance().currentUser?.uid.toString()}")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_new_user)
        btcreatenewaccount1.setOnClickListener {
           cratenewuser()
        }

    }

    private fun cratenewuser(){

        var email=etemailnewuser.text.toString()
        var password=etpasswordnewuser.text.toString()
        var name=etname.text.toString()
        var phone=etphone.text.toString()
       var profileimage=""

        if(name.isEmpty()){
            etname.error="name is empty"
            etname.requestFocus()
            return
        }
        if (phone.isEmpty()){
            etphone.error="phone is empty"
            etphone.requestFocus()
            return
        }
        if (email.isEmpty()){
            etemailnewuser.error="email is empty"
            etemailnewuser.requestFocus()
            return
        }
        if (password.isEmpty()){
            etpasswordnewuser.error="password is empty"
            etpasswordnewuser.requestFocus()
            return
        }
        if(password.length<=6){
            etpasswordnewuser.error="lenght of password gone to hell"
            etpasswordnewuser.requestFocus()
            return
        }

            mauth?.createUserWithEmailAndPassword(email,password)?.addOnCompleteListener(this) {

                if (it.isSuccessful)
                {


                    val newuser= user(name,phone,password,email,profileimage)
                    currenuserdata.set(newuser)
                    verifycode()

                }else{
                    Toast.makeText(this, it.exception.toString(), Toast.LENGTH_LONG).show()
                }
            }
    }
    private fun verifycode(){

        val user=mauth?.currentUser
            user?.sendEmailVerification()?.addOnCompleteListener {
                if (it.isSuccessful){
                    Toast.makeText(applicationContext, "تم ارسال رسالة التاكيد الي حسابك يرجي التاكيد للدخول", Toast.LENGTH_SHORT).show()
                    val i=Intent(this,Signup::class.java)
                    startActivity(i)
                }else{
                    Toast.makeText(applicationContext, it.exception.toString(), Toast.LENGTH_SHORT).show()
                }
            }

    }
}