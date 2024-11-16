package org.nevertouchgrass.smartpertzalka.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl

@Configuration
class JavaMailSenderConfig {


    @Bean
    fun getJavaMailSender(): JavaMailSender {
        val mailSender = JavaMailSenderImpl()
        mailSender.host = "smtp.mailersend.net"
        mailSender.port = 587

        mailSender.username = "MS_udp3TY@trial-z3m5jgrw00zldpyo.mlsender.net"
        mailSender.password = "MbdLrUT3NIq3pVnT"

        val props = mailSender.javaMailProperties
        props["mail.transport.protocol"] = "smtp"
        props["mail.smtp.auth"] = "true"
        props["mail.smtp.starttls.enable"] = "true"
        props["mail.debug"] = "false"

        return mailSender
    }
}