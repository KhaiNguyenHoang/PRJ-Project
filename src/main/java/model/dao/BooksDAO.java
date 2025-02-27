package model.dao;

import model.entity.Books;
import model.utils.LibraryContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BooksDAO extends LibraryContext {

    public BooksDAO() {
        super();
    }

    // Xóa sách và tất cả bản sao liên quan
    public boolean deleteBook(int bookId) {
        // Bắt đầu giao dịch để đảm bảo tính toàn vẹn dữ liệu
        try {
            conn.setAutoCommit(false);  // Tắt auto-commit để thực hiện giao dịch

            // Xóa các bản sao liên quan đến sách trong bảng BookCopies
            String deleteCopiesQuery = "DELETE FROM BookCopies WHERE BookID = ?";
            try (PreparedStatement preparedStatement = conn.prepareStatement(deleteCopiesQuery)) {
                preparedStatement.setInt(1, bookId);
                preparedStatement.executeUpdate();
            }

            // Xóa sách trong bảng Books
            String deleteBookQuery = "DELETE FROM Books WHERE IdBook = ?";
            try (PreparedStatement preparedStatement = conn.prepareStatement(deleteBookQuery)) {
                preparedStatement.setInt(1, bookId);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    conn.commit();  // Commit giao dịch nếu thành công
                    return true;
                } else {
                    conn.rollback();  // Rollback nếu không có dòng nào bị xóa
                    return false;
                }
            }
        } catch (SQLException e) {
            try {
                conn.rollback();  // Rollback giao dịch nếu có lỗi
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                conn.setAutoCommit(true);  // Bật lại auto-commit sau khi giao dịch hoàn tất
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    // Cập nhật thông tin sách và các bản sao nếu cần
    public boolean updateBook(Books book) {
        // Bắt đầu giao dịch
        try {
            conn.setAutoCommit(false);  // Tắt auto-commit để thực hiện giao dịch

            // Cập nhật sách trong bảng Books
            String query = "UPDATE books SET title = ?, author = ?, isbn = ?, publisher = ?, yearPublished = ?, categoryId = ?, copiesAvailable = ?, isDigital = ?, status = ?, filePath = ?, updatedAt = GETDATE() WHERE idBook = ?";
            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setString(1, book.getTitle());
                preparedStatement.setString(2, book.getAuthor());
                preparedStatement.setString(3, book.getIsbn());
                preparedStatement.setString(4, book.getPublisher());
                preparedStatement.setInt(5, book.getYearPublished());
                preparedStatement.setInt(6, book.getCategoryId());
                preparedStatement.setInt(7, book.getCopiesAvailable());
                preparedStatement.setBoolean(8, book.isDigital());
                preparedStatement.setString(9, book.getStatus());
                preparedStatement.setString(10, book.getFilePath());
                preparedStatement.setInt(11, book.getIdBook());
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected == 0) {
                    conn.rollback();
                    return false;
                }
            }

            // Sau khi cập nhật sách, có thể cần phải cập nhật thông tin bản sao
            // Thực hiện logic cập nhật nếu có thay đổi cần thiết trong BookCopies
            BookCopiesDAO bookCopiesDAO = new BookCopiesDAO();
            bookCopiesDAO.updateCopiesStatus(book.getIdBook(), book.getStatus());

            conn.commit();  // Commit giao dịch nếu thành công
            return true;
        } catch (SQLException e) {
            try {
                conn.rollback();  // Rollback nếu có lỗi
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                conn.setAutoCommit(true);  // Bật lại auto-commit sau khi giao dịch hoàn tất
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    // Lấy sách theo ID
    public Books getBookById(int id) {
        Books book = null;
        String query = "SELECT * " + "FROM books WHERE idBook = ?";
        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    book = mapResultSetToBook(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return book;
    }

    // Cập nhật trạng thái của bản sao liên quan khi cập nhật sách
    public boolean updateCopiesStatus(int bookId, String status) {
        String query = "UPDATE BookCopies SET Status = ? WHERE BookID = ?";
        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, status);
            preparedStatement.setInt(2, bookId);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Phương thức ánh xạ kết quả từ ResultSet vào đối tượng Books
    private Books mapResultSetToBook(ResultSet resultSet) throws SQLException {
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
        return book;
    }
}
