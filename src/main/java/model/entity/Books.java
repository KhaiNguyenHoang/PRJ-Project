package model.entity;

import java.util.Date;

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
    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;

    public Books() {
    }

    public Books(int idBook, Date deletedAt, Date updatedAt, Date createdAt, String filePath, String status, boolean isDigital, int copiesAvailable, int categoryId, int yearPublished, String publisher, String isbn, String author, String title) {
        this.idBook = idBook;
        this.deletedAt = deletedAt;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
        this.filePath = filePath;
        this.status = status;
        this.isDigital = isDigital;
        this.copiesAvailable = copiesAvailable;
        this.categoryId = categoryId;
        this.yearPublished = yearPublished;
        this.publisher = publisher;
        this.isbn = isbn;
        this.author = author;
        this.title = title;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getIdBook() {
        return idBook;
    }

    public void setIdBook(int idBook) {
        this.idBook = idBook;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isDigital() {
        return isDigital;
    }

    public void setDigital(boolean digital) {
        isDigital = digital;
    }

    public int getCopiesAvailable() {
        return copiesAvailable;
    }

    public void setCopiesAvailable(int copiesAvailable) {
        this.copiesAvailable = copiesAvailable;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getYearPublished() {
        return yearPublished;
    }

    public void setYearPublished(int yearPublished) {
        this.yearPublished = yearPublished;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
