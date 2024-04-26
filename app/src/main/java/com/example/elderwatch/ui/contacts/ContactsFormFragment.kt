package com.example.elderwatch.ui.contacts

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.example.elderwatch.R

class MyDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val inflater: LayoutInflater = requireActivity().layoutInflater
        val view: View = inflater.inflate(R.layout.fragment_contacts_form, null)

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

        // Create the AlertDialog object
        val alertDialog = builder.create()

        // Set text color for the buttons
        alertDialog.setOnShowListener {
            val positiveButton = alertDialog.getButton(Dialog.BUTTON_POSITIVE)
            val negativeButton = alertDialog.getButton(Dialog.BUTTON_NEGATIVE)

            positiveButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
            negativeButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
        }

        return alertDialog
    }
}