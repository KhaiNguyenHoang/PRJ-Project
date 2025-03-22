package dao;

import model.BookCopies;
import utils.LibraryContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BookCopiesDAO extends LibraryContext {
    private static final Logger LOGGER = Logger.getLogger(BookCopiesDAO.class.getName());

    public BookCopiesDAO() {
        super(); // Kết nối tới cơ sở dữ liệu thông qua lớp cha LibraryContext
    }

    /**
     * Kiểm tra xem có bản sao nào của sách còn trống không
     *
     * @param bookId ID của sách
     * @return true nếu có ít nhất một bản sao "Available", false nếu không
     */
    public boolean hasAvailableCopy(int bookId) {
        String query = "SELECT COUNT(*) FROM BookCopies WHERE BookID = ? AND Status = 'Available'";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, bookId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error checking available copies for BookID: {0}", bookId);
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Thêm một bản sao mới cho sách
     *
     * @param bookId     ID của sách
     * @param copyNumber Số thứ tự bản sao
     * @param status     Trạng thái của bản sao
     * @return true nếu thêm thành công, false nếu thất bại
     */
    public boolean addBookCopy(int bookId, int copyNumber, String status) {
        String insertCopyQuery = "INSERT INTO BookCopies (BookID, CopyNumber, Status) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(insertCopyQuery)) {
            ps.setInt(1, bookId);
            ps.setInt(2, copyNumber);
            ps.setString(3, status);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                // Cập nhật trạng thái sách và số lượng bản sao có sẵn
                BooksDAO booksDAO = new BooksDAO();
                booksDAO.updateBookStatusBasedOnCopies(bookId);
                return true;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error adding book copy for BookID: {0}", bookId);
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Cập nhật trạng thái của một bản sao sách
     *
     * @param copyId ID của bản sao
     * @param status Trạng thái mới
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    public boolean updateBookCopyStatus(int copyId, String status) {
        String updateStatusQuery = "UPDATE BookCopies SET Status = ? WHERE IdCopy = ?";
        try (PreparedStatement ps = conn.prepareStatement(updateStatusQuery)) {
            ps.setString(1, status);
            ps.setInt(2, copyId);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                // Cập nhật trạng thái sách và số lượng bản sao có sẵn
                BookCopies copy = getBookCopyById(copyId);
                if (copy != null) {
                    BooksDAO booksDAO = new BooksDAO();
                    booksDAO.updateBookStatusBasedOnCopies(copy.getBookId());
                }
                return true;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating status for BookCopyId: {0}", copyId);
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Đồng bộ số lượng bản sao của sách với số lượng trong Books
     *
     * @param bookId          ID của sách
     * @param copiesAvailable Số lượng bản sao mong muốn
     */
    public void syncBookCopies(int bookId, int copiesAvailable) {
        String deleteQuery = "DELETE FROM BookCopies WHERE BookID = ?";
        try {
            conn.setAutoCommit(false);

            // Xóa tất cả bản sao hiện tại
            try (PreparedStatement ps = conn.prepareStatement(deleteQuery)) {
                ps.setInt(1, bookId);
                ps.executeUpdate();
            }

            // Thêm bản sao mới
            for (int i = 1; i <= copiesAvailable; i++) {
                addBookCopy(bookId, i, "Available");
            }

            // Cập nhật trạng thái sách và số lượng bản sao
            BooksDAO booksDAO = new BooksDAO();
            booksDAO.updateBookStatusBasedOnCopies(bookId);

            conn.commit();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error syncing book copies for BookID: {0}", bookId);
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException rollbackEx) {
                LOGGER.log(Level.SEVERE, "Rollback failed", rollbackEx);
            }
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Failed to reset auto-commit", e);
            }
        }
    }

    /**
     * Lấy thông tin bản sao theo ID
     *
     * @param copyId ID của bản sao
     * @return Đối tượng BookCopies hoặc null nếu không tìm thấy
     */
    public BookCopies getBookCopyById(int copyId) {
        String query = "SELECT * FROM BookCopies WHERE IdCopy = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, copyId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToBookCopy(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving book copy by IdCopy: {0}", copyId);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Lấy bản sao sách có sẵn đầu tiên theo BookID
     *
     * @param bookId ID của sách
     * @return Đối tượng BookCopies hoặc null nếu không có bản sao nào trống
     */
    public BookCopies getFirstAvailableBookCopy(int bookId) {
        String query = "SELECT * FROM BookCopies WHERE BookID = ? AND Status = 'Available' ORDER BY IdCopy ASC";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, bookId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToBookCopy(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving first available copy for BookID: {0}", bookId);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Lấy BookID từ BookCopyId
     *
     * @param bookCopyId ID của bản sao
     * @return BookID tương ứng hoặc -1 nếu không tìm thấy
     */
    public int getBookIdByCopyId(int bookCopyId) {
        String query = "SELECT BookID FROM BookCopies WHERE IdCopy = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, bookCopyId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("BookID");
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving BookID for BookCopyId: {0}", bookCopyId);
            e.printStackTrace();
        }
        return -1; // Trả về -1 nếu không tìm thấy
    }

    /**
     * Chuyển đổi ResultSet thành đối tượng BookCopies
     *
     * @param rs ResultSet từ truy vấn
     * @return Đối tượng BookCopies
     * @throws SQLException Nếu có lỗi khi đọc dữ liệu
     */
    private BookCopies mapResultSetToBookCopy(ResultSet rs) throws SQLException {
        BookCopies copy = new BookCopies();
        copy.setIdCopy(rs.getInt("IdCopy"));
        copy.setBookId(rs.getInt("BookID"));
        copy.setCopyNumber(rs.getInt("CopyNumber"));
        copy.setStatus(rs.getString("Status"));
        return copy;
    }
}