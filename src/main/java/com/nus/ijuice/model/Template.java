package com.nus.ijuice.model;

import javax.persistence.*;
import java.util.Date;

@Entity(name="Template")
public class Template {
    @Id
    @Column(name="template_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int templateId;

    @Column(name="subject")
    private String subject;

    @Column(name="emailcontent", columnDefinition = "NVARCHAR2(1000)")
    private String emailContent;

    @Column(name="created_on")
    private Date createdOn;

    @Column(name="created_by")
    private String createdBy;

    public int getTemplateId() {
        return templateId;
    }

    public void setTemplateId(int templateId) {
        this.templateId = templateId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getEmailContent() {
        return emailContent;
    }

    public void setEmailContent(String emailContent) {
        this.emailContent = emailContent;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}

