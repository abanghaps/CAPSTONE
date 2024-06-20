package com.dicoding.capstone.saiko.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.capstone.saiko.R
import com.dicoding.capstone.saiko.data.remote.Message

class ChatAdapter(private val messages: List<Message>) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    inner class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageText: TextView = itemView.findViewById(R.id.chat_last_message)
        val messageTime: TextView = itemView.findViewById(R.id.chat_time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val message = messages[position]
        holder.messageText.text = message.body
        holder.messageTime.text = android.text.format.DateFormat.format("HH:mm", message.timestamp)
    }

    override fun getItemCount(): Int {
        return messages.size
    }
}
