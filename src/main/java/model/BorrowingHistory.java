package model;

import java.util.Date;

public class BorrowingHistory {
    private int idHistory;
    private int memberId;
    private int bookId;
    private Date borrowDate;
    private Date returnDate;

    public BorrowingHistory() {
    }

    public BorrowingHistory(int idHistory, int memberId, int bookId, Date borrowDate, Date returnDate) {
        this.idHistory = idHistory;
        this.memberId = memberId;
        this.bookId = bookId;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
    }

    // Getters and Setters
}

