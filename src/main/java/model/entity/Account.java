package model.entity;

import java.util.Date;

public class Account {
    private int idAccount;
    private String fullName;
    private String emails;
    private String username;
    private String passwordHash;
    private int roleId;
    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;
    public Account(int idAccount, String fullName, String emails, String username, String passwordHash, int roleId, Date createdAt, Date updatedAt, Date deletedAt) {
        this.idAccount = idAccount;
        this.fullName = fullName;
        this.emails = emails;
        this.username = username;
        this.passwordHash = passwordHash;
        this.roleId = roleId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public Account() {
    }

    public Account(int idAccount, String fullName, String emails, String username, String passwordHash, int roleId) {
        this.idAccount = idAccount;
        this.fullName = fullName;
        this.emails = emails;
        this.username = username;
        this.passwordHash = passwordHash;
        this.roleId = roleId;
    }

    public int getIdAccount() {
        return idAccount;
    }

    public void setIdAccount(int idAccount) {
        this.idAccount = idAccount;
    }

    public Date getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmails() {
        return emails;
    }

    public void setEmails(String emails) {
        this.emails = emails;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
