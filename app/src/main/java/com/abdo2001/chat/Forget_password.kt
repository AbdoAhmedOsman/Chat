package com.abdo2001.chat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forget_password.*

class Forget_password : AppCompatActivity() {
    var mauth: FirebaseAuth?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)
        mauth= FirebaseAuth.getInstance()
        button.setOnClickListener {
            forgetpassword()
        }

    }
    private fun forgetpassword(){
        var email=etemailforget.text.toString()
        if (email.isNotEmpty()){


            mauth?.sendPasswordResetEmail(email)?.addOnCompleteListener {
                if (it.isSuccessful){
                    Toast.makeText(applicationContext, "تم ارسالة رسالة الي بريدك الالكنروني", Toast.LENGTH_SHORT).show()
                    var i=Intent(this,Signup::class.java)
                    startActivity(i)
                }
                else{
                    Toast.makeText(applicationContext, it.exception.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }else{
            Toast.makeText(applicationContext, "Empty", Toast.LENGTH_SHORT).show()
        }
    }
}