package org.nevertouchgrass.smartpertzalka.service

import com.google.zxing.BarcodeFormat
import com.google.zxing.client.j2se.MatrixToImageWriter
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import org.springframework.stereotype.Service
import java.io.ByteArrayOutputStream
import java.nio.file.FileSystems
import java.nio.file.Path

@Service
class QrCodeService {
    fun generateQRCode(text: String, width: Int, height: Int): ByteArray {
        val qrCodeWriter = QRCodeWriter()
        val bitMatrix: BitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height)

        ByteArrayOutputStream().use { stream ->
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", stream)
            return stream.toByteArray()
        }
    }

    fun saveQRCodeToFile(text: String, width: Int, height: Int, filePath: String) {
        val qrCodeWriter = QRCodeWriter()
        val bitMatrix: BitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height)
        val path: Path = FileSystems.getDefault().getPath(filePath)
        println("Saving QR code to $path")
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path)
    }
}

fun main() {
    val qrCodeService = QrCodeService()
    qrCodeService.saveQRCodeToFile("Hello!", 350, 350, "qr-code.png")
}