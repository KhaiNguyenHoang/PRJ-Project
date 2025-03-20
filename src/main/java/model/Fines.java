package model;

import java.util.Date;

public class Fines {
    private int idFine;
    private int memberId;
    private int borrowId;
    private double amount;
    private String status;
    private Date paidDate;
    private String paymentMethod;
    private Date createdAt;

    public Fines() {
    }

    public Fines(int idFine, int memberId, int borrowId, double amount, String status, Date paidDate, String paymentMethod, Date createdAt) {
        this.idFine = idFine;
        this.memberId = memberId;
        this.borrowId = borrowId;
        this.amount = amount;
        this.status = status;
        this.paidDate = paidDate;
        this.paymentMethod = paymentMethod;
        this.createdAt = createdAt;
    }

    public int getIdFine() {
        return idFine;
    }

    public void setIdFine(int idFine) {
        this.idFine = idFine;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Date getPaidDate() {
        return paidDate;
    }

    public void setPaidDate(Date paidDate) {
        this.paidDate = paidDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(int borrowId) {
        this.borrowId = borrowId;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }
}

