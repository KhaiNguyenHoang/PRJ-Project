package dao;

import model.BookDetail;
import model.Books;
import utils.LibraryContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BooksDAO extends LibraryContext {

    public BooksDAO() {
        super();
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

    // Cập nhật trạng thái sách dựa trên bản sao
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
                    int totalCopies = rs.getInt("CopiesAvailable");
                    int borrowedCount = rs.getInt("BorrowedCount");
                    int reservedCount = rs.getInt("ReservedCount");
                    int lostCount = rs.getInt("LostCount");

                    String status = "Available";
                    if (lostCount == totalCopies) {
                        status = "Lost";
                    } else if (borrowedCount > 0) {
                        status = "Borrowed";
                    } else if (reservedCount > 0) {
                        status = "Reserved";
                    }

                    updateBookStatus(bookId, status);
                    updateCopiesAvailable(bookId, totalCopies - borrowedCount - reservedCount - lostCount);
                    return true;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error updating book status: " + e.getMessage(), e);
        }
        return false;
    }

    // Cập nhật số lượng bản sao có sẵn
    private boolean updateCopiesAvailable(int bookId, int copiesAvailable) {
        String query = "UPDATE Books SET CopiesAvailable = ? WHERE IdBook = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, copiesAvailable);
            ps.setInt(2, bookId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating copies available: " + e.getMessage(), e);
        }
    }

    // Cập nhật trạng thái sách
    private boolean updateBookStatus(int bookId, String status) {
        String query = "UPDATE Books SET Status = ? WHERE IdBook = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, status);
            ps.setInt(2, bookId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating book status: " + e.getMessage(), e);
        }
    }

    // Lấy danh sách tất cả sách
    public List<Books> getAllBooks() {
        String sql = "SELECT b.*, bd.Description, bd.PdfPath " +
                "FROM Books b LEFT JOIN BooksDetail bd ON b.IdBook = bd.BookID " +
                "WHERE b.DeletedAt IS NULL";
        List<Books> books = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Books book = mapResultSetToBooks(rs);
                BookDetail detail = new BookDetail();
                detail.setBookID(rs.getInt("IdBook"));
                detail.setDescription(rs.getString("Description"));
                detail.setPdfPath(rs.getString("PdfPath"));
                book.setBookDetail(detail);
                books.add(book);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving books: " + e.getMessage(), e);
        }
        return books;
    }

    // Lấy sách theo Id
    public Books getBookById(int id) {
        String sql = "SELECT b.*, bd.Description, bd.PdfPath " +
                "FROM Books b LEFT JOIN BooksDetail bd ON b.IdBook = bd.BookID " +
                "WHERE b.IdBook = ? AND b.DeletedAt IS NULL";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Books book = mapResultSetToBooks(rs);
                    BookDetail detail = new BookDetail();
                    detail.setBookID(rs.getInt("IdBook"));
                    detail.setDescription(rs.getString("Description"));
                    detail.setPdfPath(rs.getString("PdfPath"));
                    book.setBookDetail(detail);
                    return book;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving book by id: " + e.getMessage(), e);
        }
        return null;
    }

    // Cập nhật sách (đã sửa)
    public boolean updateBook(Books book, String description, String pdfPath) {
        String updateBookSQL = "UPDATE Books SET Title = ?, Author = ?, Publisher = ?, YearPublished = ?, CategoryId = ?, " +
                "IsDigital = ?, FilePath = ?, Status = ?, UpdatedAt = ? WHERE IdBook = ?";
        String checkDetailSQL = "SELECT COUNT(*) FROM BooksDetail WHERE BookID = ?";
        String updateDetailSQL = "UPDATE BooksDetail SET Description = ?, PdfPath = ? WHERE BookID = ?";
        String insertDetailSQL = "INSERT INTO BooksDetail (BookID, Description, PdfPath) VALUES (?, ?, ?)";

        try {
            conn.setAutoCommit(false); // Bắt đầu giao dịch

            // Cập nhật bảng Books
            try (PreparedStatement psBook = conn.prepareStatement(updateBookSQL)) {
                psBook.setString(1, book.getTitle());
                psBook.setString(2, book.getAuthor());
                psBook.setString(3, book.getPublisher());
                psBook.setInt(4, book.getYearPublished());
                psBook.setInt(5, book.getCategoryId());
                psBook.setBoolean(6, book.isDigital());
                psBook.setString(7, book.getFilePath());
                psBook.setString(8, book.getStatus());
                psBook.setTimestamp(9, new java.sql.Timestamp(new Date().getTime()));
                psBook.setInt(10, book.getIdBook());
                int rowsAffected = psBook.executeUpdate();

                if (rowsAffected > 0) {
                    // Kiểm tra xem bản ghi trong BooksDetail đã tồn tại chưa
                    try (PreparedStatement psCheck = conn.prepareStatement(checkDetailSQL)) {
                        psCheck.setInt(1, book.getIdBook());
                        try (ResultSet rs = psCheck.executeQuery()) {
                            if (rs.next() && rs.getInt(1) > 0) {
                                // Nếu đã tồn tại, cập nhật BooksDetail
                                try (PreparedStatement psDetail = conn.prepareStatement(updateDetailSQL)) {
                                    psDetail.setString(1, description);
                                    psDetail.setString(2, pdfPath);
                                    psDetail.setInt(3, book.getIdBook());
                                    psDetail.executeUpdate();
                                }
                            } else {
                                // Nếu chưa tồn tại, thêm mới vào BooksDetail
                                try (PreparedStatement psDetail = conn.prepareStatement(insertDetailSQL)) {
                                    psDetail.setInt(1, book.getIdBook());
                                    psDetail.setString(2, description);
                                    psDetail.setString(3, pdfPath);
                                    psDetail.executeUpdate();
                                }
                            }
                        }
                    }
                    conn.commit();
                    return true;
                }
            }
            conn.rollback();
            return false;
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            throw new RuntimeException("Error updating book: " + e.getMessage(), e);
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Các phương thức khác
    public List<Books> searchBooks(String keyword) {
        String searchQuery = "SELECT b.*, bd.Description, bd.PdfPath " +
                "FROM Books b LEFT JOIN BooksDetail bd ON b.IdBook = bd.BookID " +
                "WHERE (Title LIKE ? OR Author LIKE ? OR ISBN LIKE ?) AND b.DeletedAt IS NULL";
        try (PreparedStatement ps = conn.prepareStatement(searchQuery)) {
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            ps.setString(3, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                return mapResultSetToBooksList(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error searching books: " + e.getMessage(), e);
        }
    }

    private List<Books> mapResultSetToBooksList(ResultSet rs) throws SQLException {
        List<Books> booksList = new ArrayList<>();
        while (rs.next()) {
            Books book = mapResultSetToBooks(rs);
            BookDetail detail = new BookDetail();
            detail.setBookID(rs.getInt("IdBook"));
            detail.setDescription(rs.getString("Description"));
            detail.setPdfPath(rs.getString("PdfPath"));
            book.setBookDetail(detail);
            booksList.add(book);
        }
        return booksList;
    }

    private Books mapResultSetToBooks(ResultSet rs) throws SQLException {
        Books book = new Books();
        book.setIdBook(rs.getInt("IdBook"));
        book.setTitle(rs.getString("Title"));
        book.setAuthor(rs.getString("Author"));
        book.setIsbn(rs.getString("ISBN"));
        book.setPublisher(rs.getString("Publisher"));
        book.setYearPublished(rs.getInt("YearPublished"));
        book.setCategoryId(rs.getInt("CategoryId"));
        book.setCopiesAvailable(rs.getInt("CopiesAvailable"));
        book.setDigital(rs.getBoolean("IsDigital"));
        book.setFilePath(rs.getString("FilePath"));
        book.setStatus(rs.getString("Status"));
        book.setCreatedAt(rs.getTimestamp("CreatedAt"));
        book.setUpdatedAt(rs.getTimestamp("UpdatedAt"));
        book.setDeletedAt(rs.getTimestamp("DeletedAt"));
        return book;
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

    public boolean deleteBook(int idBook) {
        try {
            // Bắt đầu giao dịch
            conn.setAutoCommit(false);

            // 1. Xóa các bản ghi trong Fines liên quan đến Borrowing của sách (vì ON DELETE NO ACTION)
            String deleteFinesQuery = "DELETE FROM Fines WHERE BorrowID IN (SELECT IdBorrow FROM Borrowing WHERE BookCopyId IN (SELECT IdCopy FROM BookCopies WHERE BookID = ?))";
            try (PreparedStatement psFines = conn.prepareStatement(deleteFinesQuery)) {
                psFines.setInt(1, idBook);
                psFines.executeUpdate();
            }

            // 2. Xóa sách trong Books
            // Các bảng BookCopies, Borrowing, BorrowingHistory, BooksDetail sẽ tự động xóa nhờ ON DELETE CASCADE
            String deleteBookQuery = "DELETE FROM Books WHERE IdBook = ?";
            try (PreparedStatement psBook = conn.prepareStatement(deleteBookQuery)) {
                psBook.setInt(1, idBook);
                int rowsAffected = psBook.executeUpdate();
                if (rowsAffected > 0) {
                    conn.commit(); // Xác nhận giao dịch nếu thành công
                    return true;
                } else {
                    conn.rollback(); // Hoàn tác nếu không có hàng nào bị xóa
                    return false;
                }
            }
        } catch (SQLException e) {
            try {
                conn.rollback(); // Hoàn tác giao dịch nếu có lỗi
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                conn.setAutoCommit(true); // Khôi phục chế độ tự động commit
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}