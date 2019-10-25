package com.nus.ijuice.model;

import javax.persistence.*;

@Entity(name = "sys_config")
public class SystemConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)// auto increment
    @Column(name = "sys_config_id")
    private Integer id;

    @Column(name = "sys_name")
    private String configname;

    @Column(name = "sys_setting")
    private String configsetting;

    private String information;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getConfigname() {
        return configname;
    }

    public void setConfigname(String configname) {
        this.configname = configname;
    }

    public String getConfigsetting() {
        return configsetting;
    }

    public void setConfigsetting(String configsetting) {
        this.configsetting = configsetting;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

}