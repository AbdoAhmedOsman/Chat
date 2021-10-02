package com.abdo2001.chat.frist

data class user(val name:String,var phone:String, val password :String, val email:String,val profileimage:String){
    constructor():this("","","","",""){}

}