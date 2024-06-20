package com.dicoding.capstone.saiko.view.chat

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.capstone.saiko.data.remote.Message
import com.dicoding.capstone.saiko.view.adapter.ChatAdapter
import com.google.firebase.database.*

class ChatDetailActivity : AppCompatActivity() {

    private lateinit var chatAdapter: ChatAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var database: DatabaseReference
    private val messages = mutableListOf<Message>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.dicoding.capstone.saiko.R.layout.activity_chat_detail)

        recyclerView = findViewById(com.dicoding.capstone.saiko.R.id.rv_chat_messages)
        chatAdapter = ChatAdapter(messages)
        recyclerView.adapter = chatAdapter

        database = FirebaseDatabase.getInstance().getReference("chats")

        loadMessages()
        setupSendButton()
    }

    private fun loadMessages() {
        database.addValueEventListener(object : ValueEventListener {
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

    private fun setupSendButton() {
        val sendButton: ImageButton = findViewById(com.dicoding.capstone.saiko.R.id.send_button)
        val messageInput: EditText = findViewById(com.dicoding.capstone.saiko.R.id.message_input)

        sendButton.setOnClickListener {
            val messageText = messageInput.text.toString().trim()
            if (messageText.isNotEmpty()) {
                val messageId = database.push().key
                if (messageId != null) {
                    val message = Message(
                        id = messageId,
                        body = messageText,
                        isSent = true,
                        timestamp = System.currentTimeMillis()
                    )
                    database.child(messageId).setValue(message).addOnCompleteListener {
                        if (it.isSuccessful) {
                            messageInput.text.clear()
                        } else {
                            // Handle the error
                        }
                    }
                }
            }
        }
    }
}
