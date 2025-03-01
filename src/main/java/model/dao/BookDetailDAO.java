package model.dao;

import model.entity.BookDetail;
import model.utils.LibraryContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookDetailDAO extends LibraryContext {
    public BookDetailDAO() {
        super();
    }

    // Lấy thông tin chi tiết sách theo BookID
    public BookDetail getBookDetailByBookID(int bookID) {
        BookDetail bookDetail = null;
        String sql = "SELECT * " + "FROM BooksDetail WHERE BookID = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bookID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                bookDetail = new BookDetail();
                bookDetail.setIdBooksDetail(rs.getInt("IdBooksDetail"));
                bookDetail.setBookID(rs.getInt("BookID"));
                bookDetail.setDescription(rs.getString("Description"));
                bookDetail.setPdfPath(rs.getString("PdfPath"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookDetail;
    }

    // Cập nhật thông tin chi tiết sách
    public boolean updateBookDetail(BookDetail bookDetail) {
        String updateQuery = "UPDATE BooksDetail SET Description = ?, PdfPath = ? WHERE BookID = ?";
        try (PreparedStatement ps = conn.prepareStatement(updateQuery)) {
            ps.setString(1, bookDetail.getDescription());
            ps.setString(2, bookDetail.getPdfPath());
            ps.setInt(3, bookDetail.getBookID());
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addBookDetail(BookDetail bookDetail) {
        String insertQuery = "INSERT INTO BooksDetail (BookID, Description, PdfPath) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(insertQuery)) {
            ps.setInt(1, bookDetail.getBookID());
            ps.setString(2, bookDetail.getDescription());
            ps.setString(3, bookDetail.getPdfPath());
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteBookDetail(int bookID) {
        String deleteQuery = "DELETE FROM BooksDetail WHERE BookID = ?";
        try (PreparedStatement ps = conn.prepareStatement(deleteQuery)) {
            ps.setInt(1, bookID);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
