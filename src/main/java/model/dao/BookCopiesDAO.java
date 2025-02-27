package model.dao;

import model.utils.LibraryContext;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BookCopiesDAO extends LibraryContext {

    public BookCopiesDAO() {
        super();
    }

    // Xóa bản sao của sách khi sách bị xóa
    public boolean deleteCopiesByBookId(int bookId) {
        String query = "DELETE FROM BookCopies WHERE BookID = ?";
        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setInt(1, bookId);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Cập nhật trạng thái bản sao khi trạng thái sách thay đổi
    public boolean updateCopyStatus(int idCopy, String status) {
        String query = "UPDATE BookCopies SET Status = ? WHERE IdCopy = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, status);
            statement.setInt(2, idCopy);
            statement.executeUpdate();

            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // Cập nhật trạng thái của tất cả bản sao của một sách
    public boolean updateCopiesStatus(int bookId, String status) {
        String query = "UPDATE BookCopies SET Status = ? WHERE BookID = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, status);
            statement.setInt(2, bookId);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Thêm bản sao mới vào cơ sở dữ liệu
    public int addBookCopies(int bookId, int numberOfCopies, String status) {
        String query = "INSERT INTO BookCopies (BookID, CopyNumber, Status) VALUES (?, ?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            for (int i = 1; i <= numberOfCopies; i++) {
                statement.setInt(1, bookId);
                statement.setInt(2, i);  // Số thứ tự bản sao
                statement.setString(3, status);
                statement.executeUpdate();
            }
            return numberOfCopies;  // Trả về số lượng bản sao đã thêm
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;  // Nếu có lỗi, trả về -1
        }
    }
}
