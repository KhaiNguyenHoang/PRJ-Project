package model;

import java.util.Date;

public class Borrowing {
    private int idBorrow;
    private int memberId;
    private int bookId;
    private Date borrowDate;
    private Date dueDate;
    private Date returnDate;
    private String status;

    public Borrowing() {
    }

    public Borrowing(int idBorrow, int memberId, int bookId, Date borrowDate, Date dueDate, Date returnDate, String status) {
        this.idBorrow = idBorrow;
        this.memberId = memberId;
        this.bookId = bookId;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.status = status;
    }

    // Getters and Setters
}
