package com.nus.ijuice.repository;

import com.nus.ijuice.model.Template;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TemplateRepository extends JpaRepository<Template, String> {
    public Template findTemplateByTemplateId(int templateId);
}