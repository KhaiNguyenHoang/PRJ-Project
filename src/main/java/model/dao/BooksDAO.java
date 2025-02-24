package model.dao;

import model.entity.Books;
import model.utils.LibraryContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BooksDAO extends LibraryContext {
    public BooksDAO() {
        super();
    }

    public boolean addBook(String title, String author, String isbn, String publisher, int yearPublished, int categoryId, int copiesAvailable, boolean isDigital, String status, String filePath) {
        try {
            String query = "INSERT INTO books (title, author, isbn, publisher, yearPublished, categoryId, copiesAvailable, isDigital, status, filePath) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, author);
            preparedStatement.setString(3, isbn);
            preparedStatement.setString(4, publisher);
            preparedStatement.setInt(5, yearPublished);
            preparedStatement.setInt(6, categoryId);
            preparedStatement.setInt(7, copiesAvailable);
            preparedStatement.setBoolean(8, isDigital);
            preparedStatement.setString(9, status);
            preparedStatement.setString(10, filePath);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateBook(String title, String author, String isbn, String publisher, int yearPublished, int categoryId, int copiesAvailable, boolean isDigital, String status, String filePath) {
        try {
            String query = "UPDATE books SET title = ?, author = ?, publisher = ?, yearPublished = ?, categoryId = ?, copiesAvailable = ?, isDigital = ?, status = ?, filePath = ? WHERE isbn = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, author);
            preparedStatement.setString(3, publisher);
            preparedStatement.setInt(4, yearPublished);
            preparedStatement.setInt(5, categoryId);
            preparedStatement.setInt(6, copiesAvailable);
            preparedStatement.setBoolean(7, isDigital);
            preparedStatement.setString(8, status);
            preparedStatement.setString(9, filePath);
            preparedStatement.setString(10, isbn);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteBook(String isbn) {
        try {
            String query = "DELETE FROM books WHERE isbn = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, isbn);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteBookByTitle(String title) {
        try {
            String query = "DELETE FROM books WHERE title = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, title);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Books> getAllBooks() {
        List<Books> books = new ArrayList<>();
        try {
            String query = "SELECT * FROM books";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Books book = new Books();
                book.setIdBook(resultSet.getInt("idBook"));
                book.setTitle(resultSet.getString("title"));
                book.setAuthor(resultSet.getString("author"));
                book.setIsbn(resultSet.getString("isbn"));
                book.setPublisher(resultSet.getString("publisher"));
                book.setYearPublished(resultSet.getInt("yearPublished"));
                book.setCategoryId(resultSet.getInt("categoryId"));
                book.setCopiesAvailable(resultSet.getInt("copiesAvailable"));
                book.setDigital(resultSet.getBoolean("isDigital"));
                book.setStatus(resultSet.getString("status"));
                book.setFilePath(resultSet.getString("filePath"));
                book.setCreatedAt(resultSet.getDate("createdAt"));
                book.setDeletedAt(resultSet.getDate("deletedAt"));
                book.setUpdatedAt(resultSet.getDate("updatedAt"));
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }
}
