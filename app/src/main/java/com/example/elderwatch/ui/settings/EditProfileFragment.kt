package com.example.elderwatch.ui.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.example.elderwatch.R

class EditProfileFragment : Fragment() {

    companion object {
        fun newInstance() = EditProfileFragment()
    }

    private lateinit var viewModel: EditProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit_profile, container, false)

        val emailEditText = view.findViewById<EditText>(R.id.email)
        val nameEditText = view.findViewById<EditText>(R.id.name)
        val phoneEditText = view.findViewById<EditText>(R.id.phone)

        viewModel.userProfile.observe(viewLifecycleOwner) { data ->
            data["error"]?.let { error ->
                Toast.makeText(context, error, Toast.LENGTH_LONG).show()
            } ?: run {
                emailEditText.setText(data["email"])
                nameEditText.setText(data["name"])
                phoneEditText.setText(data["phone"])
            }
        }

        // Make the email field non-editable
        emailEditText.isEnabled = false

        return view
    }

}