package model.dao;

import model.entity.BookCopies;
import model.utils.LibraryContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookCopiesDAO extends LibraryContext {

    public BookCopiesDAO() {
        super(); // Kết nối tới cơ sở dữ liệu thông qua lớp cha LibraryContext
    }

    public static void main(String[] args) {
        BookCopiesDAO bookCopiesDAO = new BookCopiesDAO();
        bookCopiesDAO.updateBookCopyStatus(41, "Available");
    }

    // Thêm bản sao sách mới
    public boolean addBookCopy(int bookId, int copyNumber, String status) {
        String insertCopyQuery = "INSERT INTO BookCopies (BookID, CopyNumber, Status) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(insertCopyQuery)) {
            ps.setInt(1, bookId);
            ps.setInt(2, copyNumber);
            ps.setString(3, status);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                // Cập nhật lại trạng thái sách và copiesAvailable
                BooksDAO booksDAO = new BooksDAO();
                booksDAO.updateBookStatusBasedOnCopies(bookId);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Cập nhật trạng thái bản sao sách
    public boolean updateBookCopyStatus(int copyId, String status) {
        String updateStatusQuery = "UPDATE BookCopies SET Status = ? WHERE IdCopy = ?";
        try (PreparedStatement ps = conn.prepareStatement(updateStatusQuery)) {
            ps.setString(1, status);
            ps.setInt(2, copyId);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                // Cập nhật lại trạng thái sách và copiesAvailable
                BookCopies copy = getBookCopyById(copyId);
                if (copy != null) {
                    BooksDAO booksDAO = new BooksDAO();
                    // Cập nhật trạng thái sách và số lượng bản sao có sẵn sau khi cập nhật trạng thái bản sao
                    booksDAO.updateBookStatusBasedOnCopies(copy.getBookId());
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Đồng bộ số lượng bản sao
    public void syncBookCopies(int bookId, int copiesAvailable) {
        // Xoá các bản sao hiện tại và thêm mới nếu số lượng bản sao thay đổi
        String deleteQuery = "DELETE FROM BookCopies WHERE BookID = ?";
        try (PreparedStatement ps = conn.prepareStatement(deleteQuery)) {
            ps.setInt(1, bookId);
            ps.executeUpdate();

            // Thêm bản sao mới
            for (int i = 1; i <= copiesAvailable; i++) {
                addBookCopy(bookId, i, "Available");
            }

            // Cập nhật lại trạng thái sách và số lượng bản sao
            BooksDAO booksDAO = new BooksDAO();
            booksDAO.updateBookStatusBasedOnCopies(bookId);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Lấy bản sao sách theo ID
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
            e.printStackTrace();
        }
        return null;
    }

    // Map dữ liệu ResultSet thành đối tượng BookCopies
    private BookCopies mapResultSetToBookCopy(ResultSet rs) throws SQLException {
        BookCopies copy = new BookCopies();
        copy.setIdCopy(rs.getInt("IdCopy"));
        copy.setBookId(rs.getInt("BookID"));
        copy.setCopyNumber(rs.getInt("CopyNumber"));
        copy.setStatus(rs.getString("Status"));
        return copy;
    }
}
