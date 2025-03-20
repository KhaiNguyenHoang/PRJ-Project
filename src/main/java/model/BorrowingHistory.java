package model;

import java.util.Date;

public class BorrowingHistory {
    private int idHistory;
    private int memberId; // Liên kết với Members
    private int bookId;   // Liên kết với Books
    private int bookCopyId; // Liên kết với BookCopies
    private Date borrowDate;
    private Date returnDate;

    // Constructors
    public BorrowingHistory() {
    }

    public BorrowingHistory(int idHistory, int memberId, int bookId, int bookCopyId, Date borrowDate, Date returnDate) {
        this.idHistory = idHistory;
        this.memberId = memberId;
        this.bookId = bookId;
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

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }
}