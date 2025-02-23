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

    // Getters and Setters
}

