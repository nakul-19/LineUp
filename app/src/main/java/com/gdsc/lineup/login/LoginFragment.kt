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
import com.gdsc.lineup.databinding.FragmentLoginBinding

class LoginFragment() : Fragment(), ActionEventListener {

    private lateinit var binding : FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onAttach(context: Context) {
        setActionEvent()
        super.onAttach(context)
    }

    private fun setActionEvent(){
        if (activity is LoginActivity){
            (activity as LoginActivity).setActionListener(this, getString(R.string.login))
        }
    }

    override fun onActionEvent() {
        if (validateInputs())
            loggingUser()
    }

    private fun loggingUser() {
        startActivity(Intent(requireContext().applicationContext, MainActivity::class.java))
        activity?.finishAffinity()
    }

    private fun validateInputs(): Boolean {
        if(binding.zealId.text.isNullOrBlank()) {
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