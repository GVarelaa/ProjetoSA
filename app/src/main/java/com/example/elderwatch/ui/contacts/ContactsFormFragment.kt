package com.example.elderwatch.ui.contacts

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.DialogFragment
import com.example.elderwatch.R

class ContactsFormFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val inflater: LayoutInflater = requireActivity().layoutInflater
        val view: View = inflater.inflate(R.layout.fragment_contacts_form, null)

        // You can add any additional logic here if needed
        builder.setView(view)
            .setPositiveButton(
                "Adicionar"
            ) { dialog, which ->
                // Handle positive button click
            }
            .setNegativeButton(
                "Cancelar"
            ) { dialog, which ->
                // Handle negative button click
            }

        return builder.create()
    }
}