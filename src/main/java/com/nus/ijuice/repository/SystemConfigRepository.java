package com.nus.ijuice.repository;

import com.nus.ijuice.model.SystemConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SystemConfigRepository extends JpaRepository<SystemConfig, String> {
    public List<SystemConfig> findSystemConfigsByConfignameIn(List<String> key);

    public SystemConfig findSystemConfigByConfigname(String key);

    public SystemConfig findSystemConfigById(int id);

}