package com.example.elderwatch.ui.contacts

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.example.elderwatch.R
import com.example.elderwatch.utils.UserManager

class ContactsFragment : Fragment() {

    companion object {
        fun newInstance() = ContactsFragment()
    }

    private lateinit var viewModel: ContactsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_contacts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val addButton = view.findViewById<Button>(R.id.addcontactbt)

        addButton.setOnClickListener {
            val dialogFragment = ContactsFormFragment()
            dialogFragment.show(parentFragmentManager, "ContactsFormFragment")
        }

        val dynamicContent = view.findViewById<LinearLayout>(R.id.contacts_layout)
        val contacts = UserManager.contacts

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

        /*viewModel = ViewModelProvider(this)[ContactsViewModel::class.java]
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