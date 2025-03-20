package model;

public class BookCopies {
    private int idCopy;
    private int bookId; // Liên kết với Books
    private int copyNumber;
    private String status; // 'Available', 'Borrowed', 'Reserved', 'Lost'

    // Constructors
    public BookCopies() {
    }

    public BookCopies(int idCopy, int bookId, int copyNumber, String status) {
        this.idCopy = idCopy;
        this.bookId = bookId;
        this.copyNumber = copyNumber;
        this.status = status;
    }

    public BookCopies(int bookId, int copyNumber, String status) {
        this.bookId = bookId;
        this.copyNumber = copyNumber;
        this.status = status;
    }

    // Getters and Setters
    public int getIdCopy() {
        return idCopy;
    }

    public void setIdCopy(int idCopy) {
        this.idCopy = idCopy;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getCopyNumber() {
        return copyNumber;
    }

    public void setCopyNumber(int copyNumber) {
        this.copyNumber = copyNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}