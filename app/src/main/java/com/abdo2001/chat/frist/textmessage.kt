package com.abdo2001.chat.frist

import java.util.*

open class textmessage(val text:String, override val senderid:String

                       , override val recevierid:String
                       , override val sendername: String
                       , override val receviername: String
                       , override val data: Date
                       , override val type: String=typemessage.TEXT):messagetext{
    constructor():this("","","","","",Date())
}