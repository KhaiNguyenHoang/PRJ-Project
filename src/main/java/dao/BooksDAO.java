package dao;

import model.BookDetail;
import model.Books;
import utils.LibraryContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BooksDAO extends LibraryContext {

    public BooksDAO() {
        super(); // Kết nối tới cơ sở dữ liệu thông qua lớp cha LibraryContext
    }

    // Thêm sách mới
    public boolean addBook(Books book, String Description, String PdfPath) {
        String insertBookQuery = "INSERT INTO Books (Title, Author, ISBN, Publisher, YearPublished, CategoryID, CopiesAvailable, IsDigital, FilePath, Status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(insertBookQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
            // Thêm thông tin sách vào bảng Books
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
                        BookCopiesDAO bookCopiesDAO = new BookCopiesDAO();  // Dùng kết nối chung
                        for (int i = 1; i <= book.getCopiesAvailable(); i++) {
                            bookCopiesDAO.addBookCopy(bookId, i, "Available");
                        }

                        // Cập nhật lại trạng thái sách
                        updateBookStatusBasedOnCopies(bookId);

                        // Thêm chi tiết sách vào bảng BooksDetail
                        BookDetailDAO bookDetailDAO = new BookDetailDAO();  // Dùng kết nối chung
                        bookDetailDAO.addBookDetail(new BookDetail(bookId, Description, PdfPath));
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

    public List<Books> searchBooks(String keyword) {
        String searchQuery = "SELECT * " + "FROM Books WHERE Title LIKE ? OR Author LIKE ? OR ISBN LIKE ?";
        try (PreparedStatement ps = conn.prepareStatement(searchQuery)) {
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            ps.setString(3, "%" + keyword + "%");

            try (ResultSet rs = ps.executeQuery()) {
                return mapResultSetToBooksList(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<Books> mapResultSetToBooksList(ResultSet rs) {
        List<Books> booksList = new ArrayList<>();
        try {
            while (rs.next()) {
                booksList.add(mapResultSetToBooks(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return booksList;
    }

    private Books mapResultSetToBooks(ResultSet rs) {
        Books book = new Books();
        try {
            book.setIdBook(rs.getInt("IdBook"));
            book.setTitle(rs.getString("Title"));
            book.setAuthor(rs.getString("Author"));
            book.setIsbn(rs.getString("ISBN"));
            book.setPublisher(rs.getString("Publisher"));
            book.setYearPublished(rs.getInt("YearPublished"));
            book.setCategoryId(rs.getInt("CategoryID"));
            book.setCopiesAvailable(rs.getInt("CopiesAvailable"));
            book.setDigital(rs.getBoolean("IsDigital"));
            book.setFilePath(rs.getString("FilePath"));
            book.setStatus(rs.getString("Status"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return book;
    }

    public Books getBookById(int bookId) {
        String query = "SELECT * " + "FROM Books WHERE IdBook = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, bookId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToBooks(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getTotalBooks() throws SQLException {
        String query = "SELECT COUNT(*) AS total FROM Books";
        try (PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        }
        return 0;
    }
}
