package com.example.elderwatch.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.elderwatch.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SettingsFragment : Fragment(){
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?{
        val view = inflater.inflate(R.layout.fragment_settings,container,false)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val nameTextView = view.findViewById<TextView>(R.id.nameTextView)
        val emailTextView = view.findViewById<TextView>(R.id.emailTextView)

        val user = auth.currentUser
        user?.let {
            val userId = user.uid

            db.collection("users").document(userId).get().addOnSuccessListener { document ->
                val name = document.getString("name")
                val email = document.getString("email")
                nameTextView.text = name
                emailTextView.text = email
            }
        }

        val editProfileTextView = view.findViewById<TextView>(R.id.editProfileTextView)
        val editPasswordTextView = view.findViewById<TextView>(R.id.editPasswordTextView)

        editProfileTextView.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_editProfileFragment)
        }

        editPasswordTextView.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_editPasswordFragment)
        }

        return view
    }
}