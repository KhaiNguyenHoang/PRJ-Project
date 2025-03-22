package model;

import java.sql.Timestamp;

public class BorrowingHistory {
    private int idHistory;
    private int memberId;
    private int bookCopyId;
    private Timestamp borrowDate;
    private Timestamp returnDate;
    private transient String bookTitle; // Transient để không ánh xạ trực tiếp vào cơ sở dữ liệu

    // Constructors
    public BorrowingHistory() {
    }

    public BorrowingHistory(int memberId, int bookCopyId, Timestamp borrowDate, Timestamp returnDate) {
        this.memberId = memberId;
        this.bookCopyId = bookCopyId;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
    }

    // Getters and Setters
    public int getIdHistory() {
        return idHistory;
    }

    public void setIdHistory(int idHistory) {
        this.idHistory = idHistory;
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

    public Timestamp getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Timestamp returnDate) {
        this.returnDate = returnDate;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }
}