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

    @Column(name = "approveby")
    private String approveBy;

    @Column(name = "approveon")
    private String approveOn;

    @Column(name = "effective_from")
    private String effectFrom;

    @Column(name = "createby")
    private String createBy;

    @Column(name = "createon")
    private String createOn;

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

    public String getApproveBy() {
        return approveBy;
    }

    public void setApproveBy(String approveBy) {
        this.approveBy = approveBy;
    }

    public String getApproveOn() {
        return approveOn;
    }

    public void setApproveOn(String approveOn) {
        this.approveOn = approveOn;
    }

    public String getEffectFrom() {
        return effectFrom;
    }

    public void setEffectFrom(String effectFrom) {
        this.effectFrom = effectFrom;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreateOn() {
        return createOn;
    }

    public void setCreateOn(String createOn) {
        this.createOn = createOn;
    }
}