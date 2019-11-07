package com.nus.ijuice.service.imp;

import com.nus.ijuice.model.EmailConfiguration;
import com.nus.ijuice.model.SystemConfig;
import com.nus.ijuice.model.Template;
import com.nus.ijuice.model.User;
import com.nus.ijuice.repository.SystemConfigRepository;
import com.nus.ijuice.repository.TemplateRepository;
import com.nus.ijuice.service.EmailService;
import org.jasypt.util.text.BasicTextEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nus.ijuice.util.EmailAppSettingConstant;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


import java.util.*;

@Service
public class EmailUtil implements EmailService {
    final Logger logger = LoggerFactory.getLogger(EmailUtil.class);
    @Autowired
    TemplateRepository templateRepository;
    @Autowired
    SystemConfigRepository systemConfigRepository;

    @Override
    public EmailConfiguration findByKey(List<String> key) {
        List<SystemConfig> systemConfigList = systemConfigRepository.findSystemConfigsByConfignameIn(key);

        EmailConfiguration emailconfig = new EmailConfiguration();
        systemConfigList
                .forEach(item -> {
                    if (EmailAppSettingConstant.USERNAME.equals(item
                            .getConfigname())) {
                        emailconfig.setUserName(item.getConfigsetting());
                    } else if (EmailAppSettingConstant.PASSWORD.equals(item
                            .getConfigname())) {
                        emailconfig.setPassword(item.getConfigsetting());
                    } else if (EmailAppSettingConstant.HOST.equals(item
                            .getConfigname())) {
                        emailconfig.setHost(item.getConfigsetting());
                    } else if (EmailAppSettingConstant.PORT
                            .equalsIgnoreCase(item.getConfigname())) {
                        emailconfig.setPort(item.getConfigsetting());
                    }
                });
        return emailconfig;

    }

    @Override
    public Boolean sendSimpleMessage(String toEmail, String subject,
                                     String content, EmailConfiguration emailConfig) {
        Boolean result = true;
        // Sender's email ID needs to be mentioned
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", emailConfig.getHost());
        props.put("mail.smtp.port", emailConfig.getPort());

        // Get the Session object.
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(emailConfig
                                .getUserName(), emailConfig.getPassword());
                    }
                });
        try {
            // Create a default MimeMessage object.
            Message message = new MimeMessage(session);
            // Set From: header field of the header.
            message.setFrom(new InternetAddress(emailConfig.getUserName()));
            // Set To: header field of the header.
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(toEmail));
            // Set Subject: header field
            message.setSubject(subject);
            // Send the actual HTML message
            message.setContent(content, "text/html; charset=utf-8");
            // Send message
            Transport.send(message);
            logger.info("Email Sent successfully to...." + toEmail);
        } catch (javax.mail.MessagingException e) {
            result = false;
            e.printStackTrace();

            throw new RuntimeException(e);

        }
        return result;
    }

    @Override
    public Template loadTemplate(int templateId) {
        return templateRepository.findTemplateByTemplateId(templateId);

    }

    public String replaceEmailContentPlaceholder(User user, String emailContent, String password) {
        ArrayList<String> placeholderList = new ArrayList<String>();
        placeholderList.add("${object.recipient}");
        placeholderList.add("${object.password}");
        placeholderList.add("${object.username}");
        String content = emailContent;
        if (user.getUsername() != null && user.getUsername().length() > 0
                && !user.getUsername().isEmpty()) {
            content = content.replace(placeholderList.get(0).toString(),
                    user.getUsername());
        } else {
            content = content.replace(placeholderList.get(0).toString(), "");
        }
        if (user.getEmail() != null && user.getEmail().length() > 0 && !user.getEmail().isEmpty()) {
            content = content.replace(placeholderList.get(2), user.getEmail());
        }
        content = content.replace(placeholderList.get(1).toString(), password);
        return content;

    }

    public List<String> getEmailKeyList() {
        String[] keys = {"username", "password", "host", "port"};
        List<String> emailKeyList = new ArrayList<String>(Arrays.asList(keys));
        return emailKeyList;
    }

}