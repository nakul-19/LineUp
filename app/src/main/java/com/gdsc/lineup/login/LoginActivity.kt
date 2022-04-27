package com.gdsc.lineup.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gdsc.lineup.ActionEventListener
import com.gdsc.lineup.R
import com.gdsc.lineup.databinding.ActivityLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity() : AppCompatActivity() {

    //by default
    private var target: String = "LOGIN"

    private lateinit var binding : ActivityLoginBinding
    private lateinit var actionEventListener: ActionEventListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        target = intent.getStringExtra("Target_fragment")?:"LOGIN"
        setContentView(binding.root)
        setUpFragment()
        setUpListener()
    }

    fun setActionListener(context: ActionEventListener, label: String){
        binding.btnLabel.text = label
        actionEventListener = context
    }

    private fun setUpFragment() {
        when(target){
            "REGISTER" -> {
                binding.btnLabel.text = getString(R.string.register)
                supportFragmentManager.beginTransaction().replace(R.id.containerSignIn, RegisterFragment()).commit()
            }
            else -> {
                binding.btnLabel.text = getString(R.string.login)
                supportFragmentManager.beginTransaction().replace(R.id.containerSignIn, LoginFragment()).commit()
            }
        }
    }

    private fun setUpListener() {
        binding.actionNext.setOnClickListener { actionEventListener.onActionEvent() }
    }

}