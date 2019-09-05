package com.nus.ijuice.repository;

import com.nus.ijuice.model.Template;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemplateRepository extends JpaRepository<Template, String> {
    public Template findTemplateByTemplateId(int templateId);
}