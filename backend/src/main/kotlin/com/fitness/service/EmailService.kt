package com.fitness.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service

@Service
class EmailService(
    private val mailSender: JavaMailSender,
    @Value("\${app.frontend-url}") private val frontendUrl: String,
    @Value("\${app.mail.from}") private val fromEmail: String
) {

    fun sendPasswordResetEmail(toEmail: String, token: String) {
        val resetLink = "$frontendUrl/auth/reset-password?token=$token"

        val message = mailSender.createMimeMessage()
        val helper = MimeMessageHelper(message, true, "UTF-8")

        helper.setFrom(fromEmail)
        helper.setTo(toEmail)
        helper.setSubject("비밀번호 재설정 - Fitness")
        helper.setText(
            """
            <div style="max-width: 600px; margin: 0 auto; font-family: sans-serif;">
                <h2>비밀번호 재설정</h2>
                <p>아래 버튼을 클릭하여 비밀번호를 재설정하세요.</p>
                <p>이 링크는 15분간 유효합니다.</p>
                <a href="$resetLink"
                   style="display: inline-block; padding: 12px 24px; background-color: #18181b;
                          color: white; text-decoration: none; border-radius: 6px; margin: 16px 0;">
                    비밀번호 재설정
                </a>
                <p style="color: #666; font-size: 14px;">
                    본인이 요청하지 않았다면 이 이메일을 무시하세요.
                </p>
            </div>
            """.trimIndent(),
            true
        )

        mailSender.send(message)
    }
}
