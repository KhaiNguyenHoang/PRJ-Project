package model.dao;

import model.entity.BookCopies;
import model.utils.LibraryContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BookCopiesDAO extends LibraryContext {
    private static final String ID_COPY = "IdCopy";
    private static final String BOOK_ID = "BookID";
    private static final String COPY_NUMBER = "CopyNumber";
    private static final String STATUS = "Status";

    public BookCopiesDAO() {
        super();
    }

    public List<BookCopies> getAllCopies() {
        List<BookCopies> copies = new ArrayList<>();
        try {
            String query = "SELECT IdCopy, BookID, CopyNumber, Available FROM BookCopies";
            PreparedStatement statement = conn.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int idCopy = resultSet.getInt(ID_COPY);
                int bookId = resultSet.getInt(BOOK_ID);
                int copyNumber = resultSet.getInt(COPY_NUMBER);
                String status = resultSet.getString(STATUS);
                BookCopies copy = new BookCopies(idCopy, bookId, copyNumber, status);
                copies.add(copy);
            }
        } catch (SQLException ex) {
            Logger.getLogger(BookCopiesDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return copies;
    }

    public boolean addCopy(BookCopies copy) {
        try {
            String query = "INSERT INTO BookCopies (BookID, CopyNumber, Status) VALUES (?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, copy.getBookId());
            statement.setInt(2, copy.getCopyNumber());
            statement.setString(3, copy.getStatus());
            statement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(BookCopiesDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean updateCopy(BookCopies copy) {
        try {
            String query = "UPDATE BookCopies SET BookID = ?, CopyNumber = ?, Status = ? WHERE IdCopy = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, copy.getBookId());
            statement.setInt(2, copy.getCopyNumber());
            statement.setString(3, copy.getStatus());
            statement.setInt(4, copy.getIdCopy());
            statement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(BookCopiesDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean deleteCopy(int idCopy) {
        try {
            String query = "DELETE FROM BookCopies WHERE IdCopy = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, idCopy);
            statement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(BookCopiesDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public BookCopies getCopyById(int idCopy) {
        BookCopies copy = null;
        try {
            String query = "SELECT IdCopy, BookID, CopyNumber, Status FROM BookCopies WHERE IdCopy = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, idCopy);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int bookId = resultSet.getInt(BOOK_ID);
                int copyNumber = resultSet.getInt(COPY_NUMBER);
                String status = resultSet.getString(STATUS);
                copy = new BookCopies(idCopy, bookId, copyNumber, status);
            }
        } catch (SQLException ex) {
            Logger.getLogger(BookCopiesDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return copy;
    }

    public List<BookCopies> getAvailableCopies() {
        List<BookCopies> copies = new ArrayList<>();
        try {
            String query = "SELECT IdCopy, BookID, CopyNumber, Status FROM BookCopies WHERE Status = 'Available'";
            PreparedStatement statement = conn.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int idCopy = resultSet.getInt(ID_COPY);
                int bookId = resultSet.getInt(BOOK_ID);
                int copyNumber = resultSet.getInt(COPY_NUMBER);
                String status = resultSet.getString(STATUS);
                BookCopies copy = new BookCopies(idCopy, bookId, copyNumber, status);
                copies.add(copy);
            }
        } catch (SQLException ex) {
            Logger.getLogger(BookCopiesDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return copies;
    }
}
