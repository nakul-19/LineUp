package com.gdsc.lineup

import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import com.gdsc.lineup.databinding.ActivityWelcomeBinding
import com.gdsc.lineup.location.LocationService
import com.gdsc.lineup.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding

    @Inject lateinit var sp: SharedPreferences

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
            isGranted: Boolean ->
        if (!isGranted) {
//            finishAffinity()
        }
    }

    private fun requestLocationPermissions() {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) -> { }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    private fun requestBackgroundLocationPermissions() {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) -> { }
            else -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    requestPermissionLauncher.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        if (!sp.getString("zealId","").isNullOrBlank())
            startActivity(Intent(this,MainActivity::class.java).apply { addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) })
        setContentView(binding.root)
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

    override fun onStart() {
        super.onStart()
        setupPermissions()
    }



    private fun setupPermissions(): Boolean {
        val p1 = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)

        if (p1 != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                200
            )
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (!(grantResults.isNotEmpty() && !grantResults.contains(PackageManager.PERMISSION_DENIED) && requestCode==200)) {

            val alertDialogBuilder: android.app.AlertDialog.Builder =
                android.app.AlertDialog.Builder(this)

            alertDialogBuilder.setTitle("Permission needed")
            alertDialogBuilder.setMessage("Location permission is necessary for game updates")
            alertDialogBuilder.setPositiveButton(
                "Open Settings"
            ) { _, _ ->
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                val uri = Uri.fromParts("package", this.packageName, null)
                intent.data = uri
                this.startActivity(intent)
            }
            alertDialogBuilder.setNegativeButton(
                "Cancel"
            ) { _, _ -> }

            val dialog: android.app.AlertDialog? = alertDialogBuilder.create()
            dialog?.show()

        }
    }

    companion object {
        const val TARGET_FRAGMENT = "Target_fragment"
    }
}