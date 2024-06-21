package com.dicoding.capstone.saiko.view.main.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.capstone.saiko.R
import com.dicoding.capstone.saiko.data.remote.Message
import com.dicoding.capstone.saiko.view.adapter.ChatAdapter
import com.google.firebase.database.*

class ChatFragment : Fragment() {

    private lateinit var chatAdapter: ChatAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var database: DatabaseReference
    private val messages = mutableListOf<Message>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chat, container, false)

        recyclerView = view.findViewById(R.id.rv_chat)
        recyclerView.layoutManager = LinearLayoutManager(context)
        chatAdapter = ChatAdapter(messages)
        recyclerView.adapter = chatAdapter

        database = FirebaseDatabase.getInstance().getReference("chats")

        loadMessages()

        return view
    }

    private fun loadMessages() {
        database.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                messages.clear()
                for (dataSnapshot in snapshot.children) {
                    val message = dataSnapshot.getValue(Message::class.java)
                    if (message != null) {
                        messages.add(message)
                    }
                }
                chatAdapter.notifyDataSetChanged()
                recyclerView.scrollToPosition(messages.size - 1)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle possible errors
            }
        })
    }
}
