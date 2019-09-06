package com.nus.ijuice.service;

import com.nus.ijuice.model.EmailConfiguration;
import com.nus.ijuice.model.Template;

import java.util.List;

public interface EmailService {
    public EmailConfiguration findByKey(List<String> key);

    public Boolean sendSimpleMessage(String to, String subject, String text, EmailConfiguration emailConfig);

    public Template loadTemplate(int templateId);

}
