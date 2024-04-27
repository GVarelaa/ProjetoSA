package com.example.elderwatch.ui.contacts

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.elderwatch.R
import com.example.elderwatch.utils.UserManager
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class ContactsFormFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val inflater: LayoutInflater = requireActivity().layoutInflater
        val view: View = inflater.inflate(R.layout.fragment_contacts_form, null)
        val db = FirebaseFirestore.getInstance()

        // You can add any additional logic here if needed
        builder.setView(view)
            .setPositiveButton(
                "Adicionar"
            ) { dialog, which ->
                // Handle positive button click
                val email = view.findViewById<EditText>(R.id.emailEditText)

                db.collection("users")
                    .whereEqualTo("email", email.text.toString())
                    .get()
                    .addOnSuccessListener { documents ->
                        if (documents.isEmpty) {
                            Log.d("Contacts", "Invalid email")

                            // METER O TOAST
                            //Toast.makeText(requireActivity(), "Email inválido", Toast.LENGTH_SHORT).show()
                        } else {
                            val uid = documents.first().id

                            UserManager.contacts?.add(uid)

                            UserManager.uid?.let {
                                db.collection("users")
                                    .document(it)
                                    .update("contacts", FieldValue.arrayUnion(uid))
                                    .addOnSuccessListener {
                                        Log.d("Contacts", "Email added")

                                        //Toast.makeText(requireActivity(), "Email adicionado com sucesso", Toast.LENGTH_SHORT).show()
                                    }
                                    .addOnFailureListener { e ->
                                        Log.d("Contacts", "Invalid Email")

                                        //Toast.makeText(requireActivity(), "Error ao adicionar", Toast.LENGTH_SHORT).show()
                                    }
                            }
                        }
                    }
                    .addOnFailureListener { exception ->
                        Toast.makeText(requireActivity(), "Email inválido", Toast.LENGTH_SHORT).show()
                    }
            }
            .setNegativeButton(
                "Cancelar"
            ) { dialog, which ->
                // Handle negative button click
            }

        return builder.create()
    }
}