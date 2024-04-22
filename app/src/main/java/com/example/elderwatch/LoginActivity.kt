package com.example.elderwatch

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth

        val loginButton = findViewById<Button>(R.id.loginbtn)

        loginButton.setOnClickListener {
            val email = findViewById<EditText>(R.id.username).text.toString()
            val password = findViewById<EditText>(R.id.password).text.toString()

            Log.d("LOGIN", "${email} e ${password}")

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Login sucesso, atualize a UI com as informações do usuário logado
                        val user = auth.currentUser
                        // Redirecionar para outra Activity ou atualizar a UI
                        Log.d("LOGIN", "Sucesso!")

                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        // Se falhar, mostrar uma mensagem para o usuário
                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}