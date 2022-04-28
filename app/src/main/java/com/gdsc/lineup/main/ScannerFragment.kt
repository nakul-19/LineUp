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
import androidx.fragment.app.activityViewModels
import com.budiyev.android.codescanner.*
import com.gdsc.lineup.MainViewModel
import com.gdsc.lineup.databinding.DialogTeamMemberFoundBinding
import com.gdsc.lineup.databinding.FragmentScannerBinding
import com.gdsc.lineup.models.ResultHandler
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class ScannerFragment : Fragment() {

    private lateinit var binding: FragmentScannerBinding
    private lateinit var scannedQR: String
    private var codeScanner: CodeScanner? = null

    private val viewModel: MainViewModel by activityViewModels()

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        isGranted: Boolean ->
        if (isGranted) {
            setupCodeScanner()
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
        requestCamera()
        setupObserver()
    }

    private fun setupCodeScanner() {
        codeScanner = CodeScanner(requireContext(), binding.scannerView)
        codeScanner?.apply {
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
                Timber.e(it.message.toString())
            }
        }
    }

    private fun handleQR(scannedId: String) {
//        viewModel.updateScore(scannedId, TODO(Add self user id here))
    }

    private fun setupObserver() {

        viewModel.scanResponse.observe(viewLifecycleOwner) {
            when (it) {
                is ResultHandler.Loading -> {

                }
                is ResultHandler.Success -> {
                    showTeamMemberFoundCase()
                }
                is ResultHandler.Failure -> {
                    showNonTeamMemberFoundCase()
                    startCamera()
                }
            }
        }

    }

    private fun showTeamMemberFoundCase() {

        val view = DialogTeamMemberFoundBinding.inflate(layoutInflater)
        val dialog = Dialog(requireContext())

        dialog.setContentView(view.root)
        view.cross.setOnClickListener { dialog.hide() }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.show()

    }

    private fun showNonTeamMemberFoundCase() {
        Toast.makeText(requireContext(), "NOT FROM SAME TEAM!!", Toast.LENGTH_LONG).show()
    }

    private fun requestCamera() {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) -> {
                setupCodeScanner()
                startCamera()
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private fun startCamera() {
        codeScanner?.startPreview()
    }

    override fun onPause() {
        super.onPause()
        codeScanner?.releaseResources()
    }

}