package com.abdo2001.chat.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.abdo2001.chat.ChatActivity
import com.abdo2001.chat.R
import com.abdo2001.chat.frist.user
import com.abdo2001.chat.groubie.groubie
import com.abdo2001.chat.profileactivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.OnItemClickListener
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.fragment_chat.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ChatFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChatFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val firestore:FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }
    private val mauth:FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    private lateinit var chatsection:Section
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val textview=activity?.findViewById<TextView>(R.id.titletextview)
        textview!!.text="Chat"
        val imageview=activity?.findViewById<ImageView>(R.id.profile_image)
        imageview?.setOnClickListener{

            startActivity(Intent(activity,profileactivity::class.java))
        }

       if (mauth.currentUser!=null) {
        addchatlistner(::intorecycleview)
       }


        return inflater.inflate(R.layout.fragment_chat, container, false)
    }
    private fun addchatlistner(onlisten:(List<Item>)->Unit):ListenerRegistration {
        return firestore.collection("users")
               .document(FirebaseAuth.getInstance().currentUser!!.uid)
               .collection("chatchanal")
               .addSnapshotListener { value, error ->
            if (error!=null){
                return@addSnapshotListener
            }else{

            }
           val item= mutableListOf<Item>()
           value!!.documents.forEach {
               if (it.exists()){
                   item.add(groubie(it.id,it.toObject(user::class.java)!!,requireActivity()) )

               }

           }
           onlisten(item)
        }

    }
    private fun intorecycleview(item:List<Item>){
        rvchat.apply {
            layoutManager=LinearLayoutManager(activity)
            adapter=GroupAdapter<ViewHolder>().apply {
               chatsection= Section(item)
                add(chatsection)
                setOnItemClickListener(onItemclik)

            }
        }
    }
 val onItemclik=OnItemClickListener{ item, view ->
    if(item is groubie){
        val intenttochatactivity=Intent(activity, ChatActivity::class.java)
        intenttochatactivity.putExtra("user",item.user.name)
        intenttochatactivity.putExtra("profileimage",item.user.profileimage)
        intenttochatactivity.putExtra("uid",item.uid)
        activity?.startActivity(intenttochatactivity)

    }
 }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ChatFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ChatFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

