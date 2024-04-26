package com.example.elderwatch.ui.contacts

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.elderwatch.R

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

        // Find the "Adicionar Contacto" button by its ID
        val addButton = view.findViewById<Button>(R.id.addcontactbt)

        // Set OnClickListener for the button
        addButton.setOnClickListener {
            // Create an instance of MyDialogFragment
            val dialogFragment = MyDialogFragment()

            // Show the dialog fragment
            dialogFragment.show(parentFragmentManager, "MyDialogFragment")
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ContactsViewModel::class.java)
        // TODO: Use the ViewModel
    }
}