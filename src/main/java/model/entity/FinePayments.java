package model.entity;

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

    public int getIdPayment() {
        return idPayment;
    }

    public void setIdPayment(int idPayment) {
        this.idPayment = idPayment;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public int getFineId() {
        return fineId;
    }

    public void setFineId(int fineId) {
        this.fineId = fineId;
    }
}
