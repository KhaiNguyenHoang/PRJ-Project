package model;

public class BookDetail {
    private int idBooksDetail;
    private int bookID;
    private String description;
    private String pdfPath;

    public BookDetail() {
    }

    public BookDetail(int bookID, String description, String pdfPath) {
        this.bookID = bookID;
        this.description = description;
        this.pdfPath = pdfPath;
    }

    // Getters and Setters
    public int getIdBooksDetail() {
        return idBooksDetail;
    }

    public void setIdBooksDetail(int idBooksDetail) {
        this.idBooksDetail = idBooksDetail;
    }

    public int getBookID() {
        return bookID;
    }

    public void setBookID(int bookID) {
        this.bookID = bookID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPdfPath() {
        return pdfPath;
    }

    public void setPdfPath(String pdfPath) {
        this.pdfPath = pdfPath;
    }
}
