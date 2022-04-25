package com.gdsc.lineup.interfaces

interface QRCodeFoundListener {
    fun onQRCodeFound(qrCode: String)
    fun qrCodeNotFound()
}