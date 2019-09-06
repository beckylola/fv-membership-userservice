package com.nus.ijuice.repository;

import com.nus.ijuice.model.SystemConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SystemConfigRepository extends JpaRepository<SystemConfig, String> {
    public List<SystemConfig> findSystemConfigsByConfignameIn(List<String> key);

    public SystemConfig findSystemConfigByConfigname(String key);

    SystemConfig findSystemConfigById(int id);

    List<SystemConfig> findSystemConfigsByConfignameAndConfigsettingAndInformationAndEffectFrom(String type, String name, String value, String effectiveDate);
}