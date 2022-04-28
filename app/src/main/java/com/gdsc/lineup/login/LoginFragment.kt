package com.gdsc.lineup.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.gdsc.lineup.*
import com.gdsc.lineup.databinding.FragmentLoginBinding
import com.gdsc.lineup.models.ResultHandler
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class LoginFragment() : Fragment(), ActionEventListener {

    private lateinit var binding : FragmentLoginBinding
    private val viewModel: LoginViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        setObserver()
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun setObserver() {
         viewModel.loginRes.observe(viewLifecycleOwner){
             Timber.d("Result $it")
             when(it){
                 is ResultHandler.Loading -> {
                     binding.pb.visibility = View.VISIBLE
                 }
                 is ResultHandler.Success -> {
                     binding.pb.visibility = View.INVISIBLE
                         startActivity(Intent(requireContext().applicationContext, MainActivity::class.java))
                         activity?.finishAffinity()
                         Toast.makeText(requireContext(), "Login successfully", Toast.LENGTH_SHORT).show()
                 }
                 is ResultHandler.Failure -> {
                     binding.pb.visibility = View.INVISIBLE
                     Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                 }
             }
         }
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
        val loginBody = LoginBody(
            binding.zealId.text.toString(),
            binding.password.text.toString()
        )
        Timber.d("Check $loginBody")
        viewModel.loginUser(loginBody)
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