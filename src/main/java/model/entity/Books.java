package model.entity;

import java.util.Date;

public class Books {
    private int idBook;
    private String title;
    private String author;
    private String isbn;
    private String publisher;
    private int yearPublished;
    private int categoryId; // Liên kết với BookCategories
    private int copiesAvailable;
    private boolean isDigital;
    private String filePath;
    private String status; // 'Available', 'Borrowed', 'Reserved', 'Lost'
    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;

    // Constructors
    public Books() {
    }

    public Books(String title, String author, String isbn, String publisher, int yearPublished, int categoryId, int copiesAvailable, boolean isDigital, String filePath, String status) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publisher = publisher;
        this.yearPublished = yearPublished;
        this.categoryId = categoryId;
        this.copiesAvailable = copiesAvailable;
        this.isDigital = isDigital;
        this.filePath = filePath;
        this.status = status;
    }

    public Books(int idBook, String title, String author, String isbn, String publisher, int yearPublished, int categoryId,
                 int copiesAvailable, boolean isDigital, String status, String filePath, Date createdAt, Date updatedAt, Date deletedAt) {
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
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    // Getters and Setters
    public int getIdBook() {
        return idBook;
    }

    public void setIdBook(int idBook) {
        this.idBook = idBook;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getYearPublished() {
        return yearPublished;
    }

    public void setYearPublished(int yearPublished) {
        this.yearPublished = yearPublished;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getCopiesAvailable() {
        return copiesAvailable;
    }

    public void setCopiesAvailable(int copiesAvailable) {
        this.copiesAvailable = copiesAvailable;
    }

    public boolean isDigital() {
        return isDigital;
    }

    public void setDigital(boolean isDigital) {
        this.isDigital = isDigital;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }
}