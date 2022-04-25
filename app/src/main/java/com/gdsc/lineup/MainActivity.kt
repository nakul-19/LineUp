package com.gdsc.lineup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gdsc.lineup.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {

        binding.bottomNavBar.apply {
            setOnItemSelectedListener {
                when(it.itemId) {
                    R.id.qr_screen -> {
                        binding.screen.text = "One"
                        //supportFragmentManager.beginTransaction().replace(binding.mainFrame.id, QRFragment())
                    }
                    R.id.scanner_screen -> {
                        binding.screen.text = "Two"
                        //supportFragmentManager.beginTransaction().replace(binding.mainFrame.id, ScannerFragment())
                    }
                    R.id.route_screen -> {
                        binding.screen.text = "Three"
                        //supportFragmentManager.beginTransaction().replace(binding.mainFrame.id, RouteFragment())
                    }
                    R.id.leaderboard -> {
                        binding.screen.text = "Four"
                        //supportFragmentManager.beginTransaction().replace(binding.mainFrame.id, LeaderboardFragment())
                    }
                }
                return@setOnItemSelectedListener true
            }
        }

    }

}