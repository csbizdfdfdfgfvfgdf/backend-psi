package com.notepad.service;

import java.nio.charset.StandardCharsets;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.notepad.entity.User;

@Service
public class MailService {
	
	private final Logger log = LoggerFactory.getLogger(MailService.class);
	
	private static final String USER = "user";

    private static final String URL = "url";
    
    @Value("${mail.from}")
    private String mailFrom;
    
    @Autowired
    private MessageSource messageSource;

    @Autowired
    private SpringTemplateEngine templateEngine;
    
    @Autowired
    private JavaMailSender javaMailSender;

	@Async
    public void sendPasswordResetMail(User user, String passwordResetURL) {
        log.info("Sending password reset email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, passwordResetURL, "mail/passwordResetEmail", "email.reset.title");
    }

	@Async
	private void sendEmailFromTemplate(User user, String passwordResetURL, String templateName, String titleKey) {
		if (user.getEmail() == null) {
            log.info("Email doesn't exist for user '{}'", user.getEmail());
            return;
        }
        Context context = new Context();
        context.setVariable(USER, user);
        context.setVariable(URL, passwordResetURL);
        String content = templateEngine.process(templateName, context);
        String subject = messageSource.getMessage(titleKey, null, null);
        
        sendEmail(user.getEmail(), subject, content, false, true);
		
	}
	
	@Async
    public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        log.info("Send email[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
            isMultipart, isHtml, to, subject, content);

        // Prepare message using a Spring helper
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
            message.setTo(to);
            message.setFrom(mailFrom);
            message.setSubject(subject);
            message.setText(content, isHtml);
            javaMailSender.send(mimeMessage);
            log.info("Sent email to User '{}'", to);
        }  catch (MailException | MessagingException e) {
            log.warn("Email could not be sent to user '{}'", to, e);
        }
    }
	
}
