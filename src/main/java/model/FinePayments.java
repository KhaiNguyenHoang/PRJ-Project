package model;

import java.util.Date;

public class FinePayments {
    private int idPayment;
    private int fineId;
    private double amountPaid;
    private Date paymentDate;
    private String paymentMethod;

    public FinePayments() {
    }

    public FinePayments(int idPayment, int fineId, double amountPaid, Date paymentDate, String paymentMethod) {
        this.idPayment = idPayment;
        this.fineId = fineId;
        this.amountPaid = amountPaid;
        this.paymentDate = paymentDate;
        this.paymentMethod = paymentMethod;
    }

    // Getters and Setters
}
