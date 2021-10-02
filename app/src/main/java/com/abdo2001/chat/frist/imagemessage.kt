package com.abdo2001.chat.frist

import java.util.*

class imagemessage(val image:String,
                   override val senderid:String
                   , override val recevierid: String
                   , override val sendername: String
                   , override val receviername: String
                   ,override val data: Date,override val type: String=image):messagetext {
    constructor():this("","","","","",Date(0))
}