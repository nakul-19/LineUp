package com.gdsc.lineup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.gdsc.lineup.databinding.ActivityWelcomeBinding
import com.gdsc.lineup.location.LocationService
import com.gdsc.lineup.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ContextCompat.startForegroundService(this, Intent(this, LocationService::class.java))
        setUpListners()
    }

    private fun setUpListners() {
        val intent = Intent(applicationContext, LoginActivity::class.java)
        binding.registerBtn.setOnClickListener {
            intent.putExtra(TARGET_FRAGMENT, "REGISTER")
            startActivity(intent)
        }
        binding.loginBtn.setOnClickListener {
            intent.putExtra(TARGET_FRAGMENT, "LOGIN")
            startActivity(intent)
        }
    }

    companion object {
        const val TARGET_FRAGMENT = "Target_fragment"
    }
}