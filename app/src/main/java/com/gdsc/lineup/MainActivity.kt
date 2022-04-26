package com.gdsc.lineup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gdsc.lineup.databinding.ActivityMainBinding
import com.gdsc.lineup.leaderBoard.LeaderBoardFragment
import com.gdsc.lineup.main.QRFragment
import com.gdsc.lineup.main.ScannerFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        supportFragmentManager.beginTransaction().replace(binding.mainFrame.id, QRFragment()).commit()
        binding.bottomNavBar.apply {
            setOnItemSelectedListener {
                when(it.itemId) {
                    R.id.qr_screen -> {
                        supportFragmentManager.beginTransaction().replace(binding.mainFrame.id, QRFragment()).commit()
                    }
                    R.id.scanner_screen -> {
                        supportFragmentManager.beginTransaction().replace(binding.mainFrame.id, ScannerFragment()).commit()
                    }
                    R.id.route_screen -> {
                        //supportFragmentManager.beginTransaction().replace(binding.mainFrame.id, RouteFragment())
                    }
                    R.id.leaderboard -> {
                        supportFragmentManager.beginTransaction().replace(binding.mainFrame.id, LeaderBoardFragment()).commit()
                    }
                }
                return@setOnItemSelectedListener true
            }
        }

    }

}