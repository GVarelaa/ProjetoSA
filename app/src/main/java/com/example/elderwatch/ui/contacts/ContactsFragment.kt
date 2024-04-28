package com.example.elderwatch.ui.contacts

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.elderwatch.R
import com.example.elderwatch.utils.UserManager
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class ContactsFragment : Fragment() {

    companion object {
        fun newInstance() = ContactsFragment()
    }

    private lateinit var viewModel: ContactsViewModel
    private lateinit var contactAdapter: ContactAdapter
    private var db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_contacts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val contactList = UserManager.contacts

        contactAdapter = contactList?.let {
            ContactAdapter(requireContext(), it)
        }!!

        view.findViewById<RecyclerView>(R.id.contact_list).apply {
            adapter = contactAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        //view.findViewById<RecyclerView>(R.id.contact_list).adapter = contactAdapter
        /*val addButton = view.findViewById<Button>(R.id.addcontactbt)

        addButton.setOnClickListener {
            val email = view.findViewById<EditText>(R.id.contact_email)

            db.collection("users")
                .whereEqualTo("email", email.text.toString())
                .get()
                .addOnSuccessListener { documents ->
                    if (documents.isEmpty) {
                        Toast.makeText(requireContext(), "Email inválido", Toast.LENGTH_SHORT).show()
                    } else {
                        val uid = documents.first().id

                        viewModel.addContact(uid)

                        UserManager.uid?.let {
                            db.collection("users")
                                .document(it)
                                .update("contacts", FieldValue.arrayUnion(uid))
                                .addOnSuccessListener {
                                    Toast.makeText(requireActivity(), "Email adicionado com sucesso", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(requireActivity(), "Error ao adicionar", Toast.LENGTH_SHORT).show()
                                }
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(requireActivity(), "Email inválido", Toast.LENGTH_SHORT).show()
                }
        }

        val dynamicContent = view.findViewById<LinearLayout>(R.id.contacts_layout)

        viewModel = ViewModelProvider(this)[ContactsViewModel::class.java]
        viewModel.contacts.observe(viewLifecycleOwner) { contacts ->
            //dynamicContent.removeAllViews()

            contacts?.forEach { contact ->
                val contactItemLayout = LayoutInflater.from(context).inflate(
                    R.layout.contact_item,
                    dynamicContent,
                    false
                ) as RelativeLayout

                // Set contact data to layout elements
                val contactName = contactItemLayout.findViewById<TextView>(R.id.contact_name)
                contactName.text = contact.toString()

                val contactEmail = contactItemLayout.findViewById<TextView>(R.id.contact_email)
                contactEmail.text = contact.toString()

                dynamicContent.addView(contactItemLayout)
            }
        }*/
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ContactsViewModel::class.java)
        // TODO: Use the ViewModel
    }
}