package model.dao;

import model.entity.BookCategories;
import model.utils.LibraryContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookCategoriesDAO extends LibraryContext {

    public BookCategoriesDAO() {
        super();
    }

    // Thêm danh mục sách mới
    public boolean addBookCategory(BookCategories category) {
        String insertQuery = "INSERT INTO BookCategories (CategoryName) VALUES (?)";
        try (PreparedStatement ps = conn.prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, category.getCategoryName());
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        category.setIdCategory(rs.getInt(1)); // Gán ID tự động tạo
                    }
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Xóa danh mục sách theo ID
    public boolean deleteBookCategory(int idCategory) {
        String deleteQuery = "DELETE FROM BookCategories WHERE IdCategory = ?";
        try (PreparedStatement ps = conn.prepareStatement(deleteQuery)) {
            ps.setInt(1, idCategory);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Lỗi nếu danh mục đang được tham chiếu bởi Books
        }
    }

    // Cập nhật danh mục sách
    public boolean updateBookCategory(BookCategories category) {
        String updateQuery = "UPDATE BookCategories SET CategoryName = ? WHERE IdCategory = ?";
        try (PreparedStatement ps = conn.prepareStatement(updateQuery)) {
            ps.setString(1, category.getCategoryName());
            ps.setInt(2, category.getIdCategory());
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public BookCategories getBookCategoryByName(String categoryName) {
        String query = "SELECT * " + "FROM BookCategories WHERE CategoryName = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, categoryName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToBookCategory(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Lấy danh mục sách theo ID
    public BookCategories getBookCategoryById(int idCategory) {
        String query = "SELECT * " + " FROM BookCategories WHERE IdCategory = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, idCategory);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToBookCategory(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Lấy tất cả danh mục sách
    public List<BookCategories> getAllBookCategories() {
        List<BookCategories> categories = new ArrayList<>();
        String query = "SELECT * " + " FROM BookCategories";
        try (PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                categories.add(mapResultSetToBookCategory(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }

    // Ánh xạ ResultSet thành BookCategories
    private BookCategories mapResultSetToBookCategory(ResultSet rs) throws SQLException {
        BookCategories category = new BookCategories();
        category.setIdCategory(rs.getInt("IdCategory"));
        category.setCategoryName(rs.getString("CategoryName"));
        return category;
    }
}