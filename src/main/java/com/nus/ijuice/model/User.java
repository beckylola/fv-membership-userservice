package com.nus.ijuice.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity(name = "user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "user_id", columnDefinition = "VARCHAR(38)", updatable = false, nullable = false)
    private String userId;

    @Column(name = "user_name",length = 150)
    private String username;

    @Column(name = "password_hash", nullable = false, length = 500)
    private String password;
    @Column(name = "RamSal", length = 100)
    private byte[] salt;

    @Column(name = "OTP", length = 100)
    private String token;

    @Column(name = "emailid", unique = true, nullable = false, length = 100)
    private String email;
    @Column(name = "account_source", unique = false, nullable = false, length = 100)
    private String account_source;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date", length = 10, updatable = false)
    private Date createdOn;

    @Column(name = "is_active", nullable = false, columnDefinition = "TINYINT(1)")
    private Boolean isActive = true;

    @Column(name = "reset_password", nullable = false, columnDefinition = "TINYINT(1)")
    private Boolean resetPassword = true;

    @Column(name = "last_password_changedon")
    private Date lastPasswordChangedOn;


    /**
     *
     */
    public User() {
        super();
    }

    /**
     * @param userId
     * @param username
     * @param password
     * @param email
     * @param createdOn
     * @param isActive
     */
    public User(String userId, String username, String password,
                String email, Date createdOn, Boolean isActive) {
        super();
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.createdOn = createdOn;
        this.isActive = isActive;
    }

    /**
     * @param userId
     * @param username
     * @param email
     */
    public User(String userId, String username, String email) {
        super();
        this.userId = userId;
        this.username = username;
        this.email = email;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Boolean getResetPassword() {
        return resetPassword;
    }

    public void setResetPassword(Boolean resetPassword) {
        this.resetPassword = resetPassword;
    }

    public Date getLastPasswordChangedOn() {
        return lastPasswordChangedOn;
    }

    public void setLastPasswordChangedOn(Date lastPasswordChangedOn) {
        this.lastPasswordChangedOn = lastPasswordChangedOn;
    }

    public String getAccount_source() {
        return account_source;
    }

    public void setAccount_source(String account_source) {
        this.account_source = account_source;
    }

    public byte[] getSalt() {
        return salt;
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }
}
