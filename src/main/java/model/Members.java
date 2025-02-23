package model;

import java.util.Date;

public class Members {
    private int idMember;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private String passwordHash;
    private Date membershipDate;
    private String status;
    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;

    public Members() {
    }

    public Members(int idMember, String fullName, String email, String phone, String address, String passwordHash, Date membershipDate, String status, Date createdAt, Date updatedAt, Date deletedAt) {
        this.idMember = idMember;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.passwordHash = passwordHash;
        this.membershipDate = membershipDate;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    // Getters and Setters
}
