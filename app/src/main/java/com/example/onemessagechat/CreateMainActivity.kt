package com.example.onemessagechat

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.onemessagechat.databinding.CreateMessageMainActivityBinding
import com.google.firebase.database.FirebaseDatabase

class CreateMainActivity:  AppCompatActivity() {
    private val amb: CreateMessageMainActivityBinding by lazy {
        CreateMessageMainActivityBinding.inflate(layoutInflater)
    }

    private lateinit var databaseReference: FirebaseDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)

        databaseReference = FirebaseDatabase.getInstance()

        amb.subscribeButton.setOnClickListener {
            val conversationId = amb.conversationIdEditText.text.toString().trim()
            val initialMessage = amb.initialMessageEditText.text.toString().trim()

            if (conversationId.isEmpty() || initialMessage.isEmpty()) {
                showToast("Preencha todos os campos")
            } else {
                subscribeToConversation(conversationId, initialMessage)
            }
        }
    }

    private fun subscribeToConversation(conversationId: String, initialMessage: String) {
        val conversationsRef = databaseReference.reference.child("conversas")
        val newConversationRef = conversationsRef.push()
        newConversationRef.setValue(initialMessage)

        showToast("Inscrição na conversa realizada com sucesso")
        val intent = Intent(this, MenuMainActivity::class.java)
        println('a')
        startActivity(intent)
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}