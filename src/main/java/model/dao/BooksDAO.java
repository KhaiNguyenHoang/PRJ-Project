package model.dao;

import model.entity.Books;
import model.utils.LibraryContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BooksDAO extends LibraryContext {

    public BooksDAO() {
        super(); // Kết nối tới cơ sở dữ liệu thông qua lớp cha LibraryContext
    }

    public static void main(String[] args) {
        BooksDAO booksDAO = new BooksDAO();
        booksDAO.addBook(new Books("Don Quixote", "Miguel de Cervantes", "9780142437230", "Penguin Classics", 2003, 1, 1, false, "don_quixote.pdf", "Available"));
    }

    // Thêm sách mới
    public boolean addBook(Books book) {
        String insertBookQuery = "INSERT INTO Books (Title, Author, ISBN, Publisher, YearPublished, CategoryID, CopiesAvailable, IsDigital, FilePath, Status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(insertBookQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setString(3, book.getIsbn());
            ps.setString(4, book.getPublisher());
            ps.setInt(5, book.getYearPublished());
            ps.setInt(6, book.getCategoryId());
            ps.setInt(7, book.getCopiesAvailable());
            ps.setBoolean(8, book.isDigital());
            ps.setString(9, book.getFilePath());
            ps.setString(10, book.getStatus());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                // Lấy IdBook của sách mới được thêm vào
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int bookId = generatedKeys.getInt(1);

                        // Thêm bản sao vào bảng BookCopies
                        BookCopiesDAO bookCopiesDAO = new BookCopiesDAO();
                        for (int i = 1; i <= book.getCopiesAvailable(); i++) {
                            bookCopiesDAO.addBookCopy(bookId, i, "Available");
                        }

                        // Cập nhật lại trạng thái sách
                        updateBookStatusBasedOnCopies(bookId);
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Cập nhật sách
    public boolean updateBook(Books book) {
        String updateBookQuery = "UPDATE Books SET Title = ?, Author = ?, ISBN = ?, Publisher = ?, YearPublished = ?, CategoryID = ?, CopiesAvailable = ?, " +
                "IsDigital = ?, FilePath = ?, Status = ? WHERE IdBook = ?";
        try (PreparedStatement ps = conn.prepareStatement(updateBookQuery)) {
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setString(3, book.getIsbn());
            ps.setString(4, book.getPublisher());
            ps.setInt(5, book.getYearPublished());
            ps.setInt(6, book.getCategoryId());
            ps.setInt(7, book.getCopiesAvailable());
            ps.setBoolean(8, book.isDigital());
            ps.setString(9, book.getFilePath());
            ps.setString(10, book.getStatus());
            ps.setInt(11, book.getIdBook());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                // Cập nhật số lượng bản sao nếu có sự thay đổi
                BookCopiesDAO bookCopiesDAO = new BookCopiesDAO();
                bookCopiesDAO.syncBookCopies(book.getIdBook(), book.getCopiesAvailable());

                // Cập nhật trạng thái sách dựa trên số lượng bản sao có sẵn
                updateBookStatusBasedOnCopies(book.getIdBook());
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Cập nhật trạng thái sách dựa trên số lượng bản sao có sẵn
    public boolean updateBookStatusBasedOnCopies(int bookId) {
        String query = "SELECT COUNT(*) AS CopiesAvailable, " +
                "SUM(CASE WHEN Status = 'Borrowed' THEN 1 ELSE 0 END) AS BorrowedCount, " +
                "SUM(CASE WHEN Status = 'Reserved' THEN 1 ELSE 0 END) AS ReservedCount, " +
                "SUM(CASE WHEN Status = 'Lost' THEN 1 ELSE 0 END) AS LostCount " +
                "FROM BookCopies WHERE BookID = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, bookId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int copiesAvailable = rs.getInt("CopiesAvailable");
                    int borrowedCount = rs.getInt("BorrowedCount");
                    int reservedCount = rs.getInt("ReservedCount");
                    int lostCount = rs.getInt("LostCount");

                    // Tính toán trạng thái sách dựa trên số lượng bản sao
                    String status = "Available";
                    if (lostCount == copiesAvailable) {
                        status = "Lost";
                    } else if (borrowedCount > 0) {
                        status = "Borrowed";
                    } else if (reservedCount > 0) {
                        status = "Reserved";
                    }

                    // Cập nhật trạng thái sách
                    updateBookStatus(bookId, status);

                    // Cập nhật lại số lượng bản sao có sẵn trong sách
                    updateCopiesAvailable(bookId, copiesAvailable - borrowedCount - reservedCount - lostCount);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Cập nhật số lượng bản sao có sẵn trong sách
    private boolean updateCopiesAvailable(int bookId, int copiesAvailable) {
        String updateCopiesAvailableQuery = "UPDATE Books SET CopiesAvailable = ? WHERE IdBook = ?";
        try (PreparedStatement ps = conn.prepareStatement(updateCopiesAvailableQuery)) {
            ps.setInt(1, copiesAvailable);
            ps.setInt(2, bookId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Cập nhật trạng thái sách
    private boolean updateBookStatus(int bookId, String status) {
        String updateStatusQuery = "UPDATE Books SET Status = ? WHERE IdBook = ?";
        try (PreparedStatement ps = conn.prepareStatement(updateStatusQuery)) {
            ps.setString(1, status);
            ps.setInt(2, bookId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
