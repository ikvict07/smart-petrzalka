package org.nevertouchgrass.smartpertzalka.service

import jakarta.mail.internet.MimeMessage
import jakarta.mail.util.ByteArrayDataSource
import org.springframework.core.io.FileSystemResource
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service


@Service
class EmailService(private val mailSender: JavaMailSender) {

    fun sendEmail(to: String, subject: String, body: String) {
        val mimeMessage: MimeMessage = mailSender.createMimeMessage()
        val helper = MimeMessageHelper(mimeMessage, true)

        helper.setFrom("MS_udp3TY@trial-z3m5jgrw00zldpyo.mlsender.net")
        helper.setTo(to)
        helper.setSubject(subject)
        helper.setText(body, true)

        mailSender.send(mimeMessage)
    }

    fun sendMessageWithAttachment(
        to: String, subject: String, text: String, attachment: ByteArray, attachmentName: String
    ) {
        val message: MimeMessage = mailSender.createMimeMessage()

        val helper = MimeMessageHelper(message, true)

        helper.setFrom("MS_udp3TY@trial-z3m5jgrw00zldpyo.mlsender.net")
        helper.setTo(to)
        helper.setSubject(subject)
        helper.setText(text)

        val dataSource = ByteArrayDataSource(attachment, "application/pdf")
        helper.addAttachment(attachmentName, dataSource)

        mailSender.send(message)
    }
}