package com.example.onemessagechat

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.onemessagechat.databinding.LoginActivityMainBinding

class LoginMainActivity:  AppCompatActivity() {
    private val amb: LoginActivityMainBinding by lazy {
        LoginActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)

        amb.loginButton.setOnClickListener {
            val email = amb.emailEditText.text.toString().trim()
            val password = amb.passwordEditText.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                showToast("Preencha todos os campos")
            } else {
                if (isValidCredentials(email, password)) {
                    showToast("Login bem-sucedido")
                    navigateToMenu()
                } else {
                    showToast("Falha no login: Credenciais inválidas")
                }
            }
        }
    }

    private fun isValidCredentials(email: String, password: String): Boolean {
        return true
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun navigateToMenu() {
        val intent = Intent(this, MenuMainActivity::class.java)
        println('a')
        startActivity(intent)
        finish()
    }
}
