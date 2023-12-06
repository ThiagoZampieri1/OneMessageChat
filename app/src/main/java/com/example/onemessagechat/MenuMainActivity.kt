package com.example.onemessagechat

import android.R
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.onemessagechat.databinding.MenuMainActivityBinding
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.database.*
import java.util.Locale

class MenuMainActivity:  AppCompatActivity() {
    private val amb: MenuMainActivityBinding by lazy {
        MenuMainActivityBinding.inflate(layoutInflater)
    }

    private lateinit var databaseReference: DatabaseReference
    private lateinit var messages: MutableList<String>
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)

        databaseReference = FirebaseDatabase.getInstance().getReference("conversas")

        messages = mutableListOf()
        adapter = ArrayAdapter(this, R.layout.simple_list_item_1, messages)
        amb.listViewMessages.adapter = adapter

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                messages.clear()

                for (messageSnapshot in dataSnapshot.children) {
                    val message = messageSnapshot.value
                    if (message != null) {
                        messages.add(message.toString())
                    }
                }

                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })

        amb.listViewMessages.setOnItemClickListener { _, _, position, _ ->
            val selectedMessage = adapter.getItem(position)
            showEditDialog(selectedMessage ?: "")
        }

        amb.logoutButton.setOnClickListener {
            finish()
        }

        amb.createConversationButton.setOnClickListener {
                val intent = Intent(this, CreateMainActivity::class.java)
                println('a')
                startActivity(intent)
                finish()
        }
    }

    private fun showEditDialog(existingMessage: String) {
        val editText = EditText(this)
        editText.setText(existingMessage)

        val dialog = AlertDialog.Builder(this)
            .setTitle("Editar Mensagem")
            .setView(editText)
            .setPositiveButton("Salvar") { _, _ ->
                val newMessage = editText.text.toString()
                updateMessage(existingMessage, newMessage)
            }
            .setNegativeButton("Cancelar", null)
            .create()

        dialog.show()
    }

    private fun getKeyForMessage(message: String, callback: (String?) -> Unit) {
        val messagesRef = databaseReference.child("conversas")

        messagesRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (messageSnapshot in dataSnapshot.children) {
                    val value = messageSnapshot.value
                    if (value == message) {
                        callback.invoke(messageSnapshot.key)
                        return
                    }
                }

                callback.invoke(null)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                callback.invoke(null)
            }
        })
    }

    private fun updateMessage(existingMessage: String, newMessage: String) {
        println(existingMessage)
        getKeyForMessage(existingMessage) { messageKey ->
            if (messageKey != null) {
                val messageReference = databaseReference.child("conversas").child(messageKey)
                messageReference.setValue(newMessage)
            }
        }
    }
}