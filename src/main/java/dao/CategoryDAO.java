package dao;

import model.BookCategories;
import utils.LibraryContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO extends LibraryContext {
    public CategoryDAO() {
        super();
    }

    public List<BookCategories> getAllCategories() {
        String sql = "SELECT * FROM BookCategories";
        List<BookCategories> categoriesList = new ArrayList<>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                BookCategories category = new BookCategories(
                        rs.getInt(1),  // idCategory
                        rs.getString(2) // categoryName
                );
                categoriesList.add(category);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving categories: " + e.getMessage(), e);
        }
        return categoriesList; // Trả về danh sách thay vì null
    }
}