package model.entity;

import java.util.Date;

public class Borrowing {
    private int idBorrow;
    private int memberId; // Liên kết với Members
    private int bookId;   // Liên kết với Books
    private int bookCopyId; // Liên kết với BookCopies
    private Date borrowDate;
    private Date dueDate;
    private Date returnDate;
    private String status; // 'Borrowed', 'Returned', 'Overdue'
    private Date createdAt;

    // Constructors
    public Borrowing() {
    }

    public Borrowing(int idBorrow, int memberId, int bookId, int bookCopyId, Date borrowDate, Date dueDate, String status, Date createdAt) {
        this.idBorrow = idBorrow;
        this.memberId = memberId;
        this.bookId = bookId;
        this.bookCopyId = bookCopyId;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Borrowing(int idBorrow, int memberId, int bookId, int bookCopyId, Date borrowDate, Date dueDate, String status) {
        this.idBorrow = idBorrow;
        this.memberId = memberId;
        this.bookId = bookId;
        this.bookCopyId = bookCopyId;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.status = status;
    }

    public Borrowing(int idBorrow, int memberId, int bookId, int bookCopyId, Date borrowDate, Date dueDate, Date returnDate, String status, Date createdAt) {
        this.idBorrow = idBorrow;
        this.memberId = memberId;
        this.bookId = bookId;
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

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getBookCopyId() {
        return bookCopyId;
    }

    public void setBookCopyId(int bookCopyId) {
        this.bookCopyId = bookCopyId;
    }

    public Date getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(Date borrowDate) {
        this.borrowDate = borrowDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}