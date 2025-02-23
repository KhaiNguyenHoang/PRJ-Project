package model;

public class BookCopies {
    private int idCopy;
    private int bookId;
    private int copyNumber;
    private String status;

    public BookCopies() {
    }

    public BookCopies(int idCopy, int bookId, int copyNumber, String status) {
        this.idCopy = idCopy;
        this.bookId = bookId;
        this.copyNumber = copyNumber;
        this.status = status;
    }

    // Getters and Setters
}
