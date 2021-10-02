package com.abdo2001.chat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.signup.*


class Signup : AppCompatActivity() , TextWatcher{
    private val firebasestore: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }
    var mauth:FirebaseAuth?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup)
        mauth= FirebaseAuth.getInstance()
        btlogin.setOnClickListener {
            login()
        }
        btcreatenewaccount.setOnClickListener {
            var i= Intent(this,Create_new_user::class.java)
                startActivity(i)
            }
        tvforgetpassword.setOnClickListener{
            var i=Intent(this,Forget_password::class.java)
            startActivity(i)
        }

        etemaillogin.addTextChangedListener(this@Signup)
       etpasswordlogin.addTextChangedListener(this@Signup)
        }
    private fun login(){
        var email=etemaillogin.text.toString()
        var password=etpasswordlogin.text.toString()
        if (email.isNotEmpty()&&password.isNotEmpty()){
            mauth?.signInWithEmailAndPassword(email,password)?.addOnCompleteListener{
                if (it.isSuccessful){
                    
                    Toast.makeText(this, "Welcome", Toast.LENGTH_SHORT).show()
                   verify()
                    var intent=Intent(this,MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)

                }else{
                    Toast.makeText(this, it.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }

        }else{
            Toast.makeText(this, "Empty", Toast.LENGTH_SHORT).show()

        }
    }

   private fun verify(){
       val user=mauth?.currentUser
       if (user!!.isEmailVerified){
           var intent=Intent(this,MainActivity::class.java)
           intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
           startActivity(intent)
       }else{
           Toast.makeText(this, "verify your email", Toast.LENGTH_SHORT).show()
       }
   }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        btlogin.isEnabled=etemaillogin.text.trim().isNotBlank()&&etpasswordlogin.text.trim().isNotEmpty()
    }

    override fun afterTextChanged(s: Editable?) {

    }


}



