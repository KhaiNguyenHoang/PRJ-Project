package model.entity;

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

    public int getIdCopy() {
        return idCopy;
    }

    public void setIdCopy(int idCopy) {
        this.idCopy = idCopy;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCopyNumber() {
        return copyNumber;
    }

    public void setCopyNumber(int copyNumber) {
        this.copyNumber = copyNumber;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }
}
