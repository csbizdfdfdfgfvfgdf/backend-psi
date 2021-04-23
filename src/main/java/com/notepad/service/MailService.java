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

/**
* The MailService to get mail configurations and send mail
*
* @author  Zohaib Ali
* @version 1.0
* @since   2021-04-22 
*/
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

    /**
	 * Async method to send password reset mail.
	 *
	 * @param user to send mail
	 * @param passwordResetUrl to send mail at
	 */
	@Async
    public void sendPasswordResetMail(User user, String passwordResetURL) {
        log.info("Sending password reset email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, passwordResetURL, "mail/passwordResetEmail", "email.reset.title");
    }

	/**
	 * Async method to send email from provided template.
	 *
	 * @param user to send mail
	 * @param passwordResetUrl to send mail at
	 * @param templateName template of the mail
	 * @param titleKey to get subject of the mail
	 */
	@Async
	private void sendEmailFromTemplate(User user, String passwordResetURL, String templateName, String titleKey) {
		if (user.getEmail() == null) {
            log.info("Email doesn't exist for user '{}'", user.getEmail());
            return;
        }
		
		// create mail context to set values in template
        Context context = new Context();
        context.setVariable(USER, user);
        context.setVariable(URL, passwordResetURL);
        
        // set values in template using context
        String content = templateEngine.process(templateName, context);
        String subject = messageSource.getMessage(titleKey, null, null);
        
        // send email
        sendEmail(user.getEmail(), subject, content, false, true);
		
	}
	
	/**
	 * Async method to send email.
	 *
	 * @param to : to which mail is to sent
	 * @param subject : subject of the mail
	 * @param content : content of the mail
	 * @param isMultipart : is any file or not
	 * @param isHtml : is html or plain text
	 */
	@Async
    public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        log.info("Send email[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
            isMultipart, isHtml, to, subject, content);

        // Prepare message using a Spring helper
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
        	
        	// build message to set mail
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
            message.setTo(to);
            message.setFrom(mailFrom);
            message.setSubject(subject);
            message.setText(content, isHtml);
            
            // send email
            javaMailSender.send(mimeMessage);
            log.info("Sent email to User '{}'", to);
        }  catch (MailException | MessagingException e) {
            log.warn("Email could not be sent to user '{}'", to, e);
        }
    }
	
}
