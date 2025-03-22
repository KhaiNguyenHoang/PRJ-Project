package model;

import java.sql.Timestamp;

public class Borrowing {
    private int idBorrow;
    private int memberId;
    private int bookCopyId; // Chỉ sử dụng BookCopyId, không có BookId
    private Timestamp borrowDate;
    private Timestamp dueDate;
    private Timestamp returnDate;
    private String status;
    private Timestamp createdAt;

    // Constructors
    public Borrowing() {
    }

    public Borrowing(int memberId, int bookCopyId, Timestamp borrowDate, Timestamp dueDate, String status) {
        this.memberId = memberId;
        this.bookCopyId = bookCopyId;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.status = status;
    }

    public Borrowing(int idBorrow, int memberId, int bookCopyId, Timestamp borrowDate, Timestamp dueDate, String status) {
        this.idBorrow = idBorrow;
        this.memberId = memberId;
        this.bookCopyId = bookCopyId;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.status = status;
    }

    public Borrowing(int idBorrow, int memberId, int bookCopyId, Timestamp borrowDate, Timestamp dueDate,
                     Timestamp returnDate, String status, Timestamp createdAt) {
        this.idBorrow = idBorrow;
        this.memberId = memberId;
        this.bookCopyId = bookCopyId;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.status = status;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getIdBorrow() {
        return idBorrow;
    }

    public void setIdBorrow(int idBorrow) {
        this.idBorrow = idBorrow;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public int getBookCopyId() {
        return bookCopyId;
    }

    public void setBookCopyId(int bookCopyId) {
        this.bookCopyId = bookCopyId;
    }

    public Timestamp getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(Timestamp borrowDate) {
        this.borrowDate = borrowDate;
    }

    public Timestamp getDueDate() {
        return dueDate;
    }

    public void setDueDate(Timestamp dueDate) {
        this.dueDate = dueDate;
    }

    public Timestamp getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Timestamp returnDate) {
        this.returnDate = returnDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}