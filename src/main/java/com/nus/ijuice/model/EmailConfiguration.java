package com.nus.ijuice.model;

public class EmailConfiguration {

    private String userName;
    private String password;
    private String host;
    private String port;
    private String schedulerDelay;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getSchedulerDelay() {
        return schedulerDelay;
    }

    public void setSchedulerDelay(String schedulerDelay) {
        this.schedulerDelay = schedulerDelay;
    }
}
