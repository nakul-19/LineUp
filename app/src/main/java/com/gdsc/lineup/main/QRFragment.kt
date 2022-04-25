package com.gdsc.lineup.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.gdsc.lineup.databinding.FragmentQRBinding
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder


class QRFragment : Fragment() {

    private lateinit var binding: FragmentQRBinding
    private lateinit var userId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQRBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getUserId()

    }

    private fun getUserId() {
        userId = "r1f81eokj1ued0193eg1i3denfo8"
        setQRCode()
    }

    private fun setQRCode() {
        val barcodeEncoder = BarcodeEncoder()
        val bitmap = barcodeEncoder.encodeBitmap(userId, BarcodeFormat.QR_CODE, 512, 512)
        binding.qrImage.setImageBitmap(bitmap)
    }

}