package org.codenova.slseproject.service;


import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codenova.slseproject.entity.User;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class MailSend {

    private JavaMailSender mailSender;

    public boolean sendWelcomeEmail(User user) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(user.getEmail());
        message.setSubject("[MoneyLog] 환영함");
        message.setText("MoneyLog 에 가입해 주셔서 감사링~");

        boolean r = true;
        try {
            mailSender.send(message);
        } catch (Exception e) {
            System.out.println("에러" + e);
            r = false;
        }
        return r;
    }

    public boolean sendWelcomeHtmlMessage(User user) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, "utf-8");
            messageHelper.setTo(user.getEmail());
            messageHelper.setSubject("[MoneyLog] 환영합니다");

            String html = "<h2>안녕하세요, 머니로그입니다.</h2>";
            html += "<p><a href='http://192.168.10.152:8080'>머니로그</a>에 가입하신 것을 진심으로 환영합니다.</p>";
            html += "<p>앞으로 다양한 컨텐츠와 서비스를 제공해드리겠습니다.</p>";
            html += "<p><span style='color : gray'>팀 코드노바 올림</span></p>";
            messageHelper.setText(html, true);

            mailSender.send(message);


        } catch (Exception e) {
            System.out.println("error" + e);
            return false;
        }
        return true;
    }

    public boolean sendNewPassword(String email, String newPassword) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, "utf-8");
            messageHelper.setTo(email);
            messageHelper.setSubject("[MoneyLog] 비밀번호 변경");

            String html = "<h2>안녕하세요, <a href='http://192.168.10.152:8080'>머니로그</a>입니다.</h2>";
            html += "새로운 비밀번호입니다</p>";
            html += "<p>" + newPassword + "</p>";
            html += "<p>로그인 후 비밀번호를 변경해주세요</p>";
            messageHelper.setText(html, true);

            mailSender.send(message);

        } catch (Exception e) {
            System.out.println("error" + e);
            return false;
        }
        return true;
    }

    public boolean sendEmailVerify(String email, String token){
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, "utf-8");
            messageHelper.setTo(email);
            messageHelper.setSubject("[MoneyLog] 이메일 인증");

            String html = "<h2>안녕하세요, <a href='http://192.168.10.152:8080'>머니로그</a>입니다.</h2>";
            html += "<p><a href='http://192.168.10.152:8080/auth/email-verify/" + email + "/" + token + "'>인증하기</a></p>";
            messageHelper.setText(html, true);

            mailSender.send(message);

        } catch (Exception e) {
            System.out.println("error" + e);
            return false;
        }
        return true;
    }
}