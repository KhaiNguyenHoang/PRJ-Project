package model.dao;

import model.entity.BookCategories;
import model.utils.LibraryContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BookCategoriesDAO extends LibraryContext {
    public BookCategoriesDAO() {
        super();
    }

    public static void main(String[] args) {
        BookCategoriesDAO bookCategoriesDAO = new BookCategoriesDAO();
        List<BookCategories> categories = bookCategoriesDAO.getAllCategories();
        for (BookCategories category : categories) {
            System.out.println("ID: " + category.getIdCategory() + ", Name: " + category.getCategoryName());
        }
        BookCategories category = bookCategoriesDAO.getCategoryById(1);
        System.out.println("ID: " + category.getIdCategory() + ", Name: " + category.getCategoryName());
    }

    public List<BookCategories> getAllCategories() {
        List<BookCategories> categories = new ArrayList<>();
        try {
            String query = "SELECT IdCategory,CategoryName FROM BookCategories";
            PreparedStatement statement = conn.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int idCategory = resultSet.getInt("IdCategory");
                String categoryName = resultSet.getString("CategoryName");
                BookCategories category = new BookCategories(idCategory, categoryName);
                categories.add(category);
            }

        } catch (SQLException ex) {
            Logger.getLogger(BookCategoriesDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return categories;
    }

    public BookCategories getCategoryById(int idCategory) {
        BookCategories category = null;
        try {
            String query = "SELECT CategoryName FROM BookCategories WHERE IdCategory = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, idCategory);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String categoryName = resultSet.getString("CategoryName");
                category = new BookCategories(idCategory, categoryName);
            }
        } catch (SQLException ex) {
            Logger.getLogger(BookCategoriesDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return category;
    }

    public boolean updateCategory(BookCategories category) {
        try {
            String query = "UPDATE BookCategories SET CategoryName = ? WHERE IdCategory = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, category.getCategoryName());
            statement.setInt(2, category.getIdCategory());
            statement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(BookCategoriesDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean addCategory(BookCategories category) {
        try {
            String query = "INSERT INTO BookCategories (CategoryName) VALUES (?)";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, category.getCategoryName());
            statement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(BookCategoriesDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean deleteCategory(int idCategory) {
        try {
            String query = "DELETE FROM BookCategories WHERE IdCategory = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, idCategory);
            statement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(BookCategoriesDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
}
