package com.example.elderwatch.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.elderwatch.MainActivity
import com.example.elderwatch.R
import com.example.elderwatch.utils.UserManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class LoginActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth
        db = FirebaseFirestore.getInstance()

        val loginButton = findViewById<Button>(R.id.loginbtn)
        val backButton = findViewById<ImageButton>(R.id.backButton)

        loginButton.setOnClickListener {
            val email = findViewById<EditText>(R.id.email).text.toString()
            val password = findViewById<EditText>(R.id.password).text.toString()

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser

                        if (user != null) {
                            db.collection("users")
                                .document(user.uid)
                                .get()
                                .addOnSuccessListener { document ->
                                    if (document != null) {
                                        val contacts = document.get("contacts") as MutableList<*>

                                        UserManager.uid = user.uid
                                        UserManager.email = user.email
                                        UserManager.contacts = contacts

                                        val intent = Intent(this, MainActivity::class.java)
                                        startActivity(intent)
                                    } else {
                                        Toast.makeText(baseContext, "Erro no login", Toast.LENGTH_SHORT).show()
                                    }
                                }
                        }
                    } else {
                        Toast.makeText(baseContext, "Erro no login", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        backButton.setOnClickListener {
            val intent = Intent(this, AuthenticationActivity::class.java)
            startActivity(intent)
        }
    }
}