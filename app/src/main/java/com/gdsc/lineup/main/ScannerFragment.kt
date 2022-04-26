package com.gdsc.lineup.main

import android.Manifest
import android.app.Dialog
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.budiyev.android.codescanner.*
import com.gdsc.lineup.databinding.DialogTeamMemberFoundBinding
import com.gdsc.lineup.databinding.FragmentScannerBinding


class ScannerFragment : Fragment() {

    private lateinit var binding: FragmentScannerBinding
    private lateinit var scannedQR: String
    private lateinit var codeScanner: CodeScanner

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        isGranted: Boolean ->
        if (isGranted) {
            startCamera()
        } else {
            // TODO
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentScannerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCodeScanner()
        requestCamera()
    }

    private fun setupCodeScanner() {
        codeScanner = CodeScanner(requireContext(), binding.scannerView)
        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.TWO_DIMENSIONAL_FORMATS
            autoFocusMode = AutoFocusMode.SAFE
            isAutoFocusEnabled = true
            isFlashEnabled = false

            decodeCallback = DecodeCallback {
                Handler(Looper.getMainLooper()).post {
                    handleQR(it.text)
                }
            }

            errorCallback = ErrorCallback {
                Log.e("Code Scanner", it.message.toString())
            }
        }
    }

    // this function receives
    private fun handleQR(id: String) {

        // make api call to check if the user is in the same team or not
        // and show the dialog box accordingly showTeamMemberFoundDialog() or showNonTeamMemberFoundDialog()
        showTeamMemberFoundDialog()

    }

    private fun showTeamMemberFoundDialog() {

        val view = DialogTeamMemberFoundBinding.inflate(layoutInflater)
        val dialog = Dialog(requireContext())

        dialog.setContentView(view.root)
        view.cross.setOnClickListener { dialog.hide() }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.show()

    }

    private fun showNonTeamMemberFoundDialog() {
        // no designs for this case yet (Can show toast for now)
        Toast.makeText(requireContext(), "Different Team Member", Toast.LENGTH_LONG).show()
    }

    private fun requestCamera() {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) -> {
                startCamera()
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private fun startCamera() {
        codeScanner.startPreview()
    }

    override fun onPause() {
        super.onPause()
        codeScanner.releaseResources()
    }

}