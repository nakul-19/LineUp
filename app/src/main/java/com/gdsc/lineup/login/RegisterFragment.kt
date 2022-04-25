package com.gdsc.lineup.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gdsc.lineup.ActionEventListener
import com.gdsc.lineup.MainActivity
import com.gdsc.lineup.R
import com.gdsc.lineup.databinding.FragmentRegisterBinding


class RegisterFragment() : Fragment(), ActionEventListener {

    private lateinit var binding: FragmentRegisterBinding
    private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        setActionEvent()
        super.onAttach(context)
    }

    private fun setActionEvent() {
        if (activity is LoginActivity) {
            (activity as LoginActivity).setActionListener(this, getString(R.string.register))
        }
    }

    override fun onActionEvent() {
        if (validateInputs()){
            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.containerSignIn, ChooseAvatarFragment())?.commit()
        }

    }

    private fun validateInputs(): Boolean {
        if (binding.name.text.isNullOrBlank()) {
            binding.name.error = "Required"
            return false
        } else if (binding.email.text.isNullOrBlank()) {
            binding.email.error = "Required"
            return false
        } else if (!binding.email.text.toString().matches(emailPattern.toRegex())) {
            binding.email.error = "Invalid"
            return false
        } else if (binding.zealId.text.isNullOrBlank()) {
            binding.zealId.error = "Required"
            return false
        } else if (binding.zealId.text.toString().length != 4) {
            binding.zealId.error = "Invalid"
            return false
        } else if (binding.password.text.isNullOrBlank()) {
            binding.password.error = "Required"
            return false
        } else if (binding.password.text.toString().length < 6) {
            binding.password.error = "password is too short"
            return false
        }
        return true
    }

}