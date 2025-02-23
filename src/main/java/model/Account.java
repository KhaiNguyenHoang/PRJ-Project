package model;

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

    public Account() {
    }

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

    // Getters and Setters
}
