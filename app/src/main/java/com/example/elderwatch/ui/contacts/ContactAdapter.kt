package com.example.elderwatch.ui.contacts

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.elderwatch.R
import com.example.elderwatch.utils.Contact

class ContactAdapter(private val context: Context, private val contacts: List<Contact>) :
    RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val contactName: TextView = itemView.findViewById(R.id.contact_name)
        val contactEmail: TextView = itemView.findViewById(R.id.contact_email)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.contact_item, parent, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contacts[position]
        holder.contactName.text = contact.name
        holder.contactEmail.text = contact.email

        // Set click listener for the contact item
        holder.itemView.setOnClickListener {
            // Handle click event here
        }
    }

    override fun getItemCount(): Int {
        return contacts.size
    }
}