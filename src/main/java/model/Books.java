package model;

public class Books {
    private int idBook;
    private String title;
    private String author;
    private String isbn;
    private String publisher;
    private int yearPublished;
    private int categoryId;
    private int copiesAvailable;
    private boolean isDigital;
    private String status;
    private String filePath;

    public Books() {
    }

    public Books(int idBook, String title, String author, String isbn, String publisher, int yearPublished, int categoryId, int copiesAvailable, boolean isDigital, String status, String filePath) {
        this.idBook = idBook;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publisher = publisher;
        this.yearPublished = yearPublished;
        this.categoryId = categoryId;
        this.copiesAvailable = copiesAvailable;
        this.isDigital = isDigital;
        this.status = status;
        this.filePath = filePath;
    }

    // Getters and Setters
}
