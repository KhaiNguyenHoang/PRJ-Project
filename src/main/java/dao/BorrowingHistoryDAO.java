package dao;

import model.Borrowing;
import model.BorrowingHistory;
import utils.LibraryContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BorrowingHistoryDAO extends LibraryContext {
    private static final Logger LOGGER = Logger.getLogger(BorrowingHistoryDAO.class.getName());

    public BorrowingHistoryDAO() {
        super(); // Initializes the database connection from LibraryContext
    }

    /**
     * Adds a new borrowing history record when a book is borrowed.
     *
     * @param borrowing The Borrowing object containing borrow details
     * @return true if the insertion is successful, false otherwise
     */
    public boolean addBorrowingHistory(Borrowing borrowing) {
        String sql = "INSERT INTO BorrowingHistory (MemberID, BookCopyId, BorrowDate, ReturnDate) " +
                "VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, borrowing.getMemberId());
            ps.setInt(2, borrowing.getBookCopyId());
            ps.setTimestamp(3, borrowing.getBorrowDate());
            ps.setTimestamp(4, null); // ReturnDate is initially null when borrowing starts

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                LOGGER.log(Level.INFO, "Borrowing history added for MemberID: {0}, BookCopyId: {1}",
                        new Object[]{borrowing.getMemberId(), borrowing.getBookCopyId()});
                return true;
            } else {
                LOGGER.log(Level.WARNING, "Failed to add borrowing history for MemberID: {0}, BookCopyId: {1}",
                        new Object[]{borrowing.getMemberId(), borrowing.getBookCopyId()});
                return false;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error adding borrowing history for MemberID: {0}, BookCopyId: {1}"
            );
            return false;
        }
    }

    /**
     * Updates the return date in the borrowing history when a book is returned.
     *
     * @param borrowing The Borrowing object with return details
     * @return true if the update is successful, false otherwise
     */
    public boolean updateReturnHistory(Borrowing borrowing) {
        String sql = "UPDATE BorrowingHistory SET ReturnDate = ? " +
                "WHERE MemberID = ? AND BookCopyId = ? AND ReturnDate IS NULL";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, borrowing.getReturnDate());
            ps.setInt(2, borrowing.getMemberId());
            ps.setInt(3, borrowing.getBookCopyId());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                LOGGER.log(Level.INFO, "Borrowing history updated for MemberID: {0}, BookCopyId: {1}",
                        new Object[]{borrowing.getMemberId(), borrowing.getBookCopyId()});
                return true;
            } else {
                LOGGER.log(Level.WARNING, "No borrowing history found to update for MemberID: {0}, BookCopyId: {1}",
                        new Object[]{borrowing.getMemberId(), borrowing.getBookCopyId()});
                return false;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating borrowing history for MemberID: {0}, BookCopyId: {1}"
            );
            return false;
        }
    }

    /**
     * Retrieves the borrowing history for a specific member, including book titles.
     *
     * @param memberId The ID of the member
     * @return A list of BorrowingHistory objects
     * @throws SQLException If a database error occurs
     */
    public List<BorrowingHistory> getBorrowingHistoryByMemberId(int memberId) throws SQLException {
        List<BorrowingHistory> historyList = new ArrayList<>();
        String sql = "SELECT bh.IdHistory, bh.MemberID, bh.BookCopyId, bh.BorrowDate, bh.ReturnDate, b.Title AS bookTitle " +
                "FROM BorrowingHistory bh " +
                "JOIN BookCopies bc ON bh.BookCopyId = bc.IdCopy " +
                "JOIN Books b ON bc.BookID = b.IdBook " +
                "WHERE bh.MemberID = ? " +
                "ORDER BY bh.BorrowDate DESC";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, memberId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    BorrowingHistory history = new BorrowingHistory();
                    history.setIdHistory(rs.getInt("IdHistory"));
                    history.setMemberId(rs.getInt("MemberID"));
                    history.setBookCopyId(rs.getInt("BookCopyId"));
                    history.setBorrowDate(rs.getTimestamp("BorrowDate"));
                    history.setReturnDate(rs.getTimestamp("ReturnDate"));
                    history.setBookTitle(rs.getString("bookTitle")); // Assign bookTitle
                    historyList.add(history);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving borrowing history for member: {0}", memberId);
            throw e; // Rethrow exception for upper layers to handle
        }
        return historyList;
    }

    /**
     * Retrieves a BorrowingHistory record by its IdHistory.
     *
     * @param idHistory The ID of the borrowing history record
     * @return A BorrowingHistory object or null if not found
     * @throws SQLException If a database error occurs
     */
    public BorrowingHistory getBorrowingHistoryById(int idHistory) throws SQLException {
        String sql = "SELECT bh.IdHistory, bh.MemberID, bh.BookCopyId, bh.BorrowDate, bh.ReturnDate, b.Title AS bookTitle " +
                "FROM BorrowingHistory bh " +
                "JOIN BookCopies bc ON bh.BookCopyId = bc.IdCopy " +
                "JOIN Books b ON bc.BookID = b.IdBook " +
                "WHERE bh.IdHistory = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idHistory);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    BorrowingHistory history = new BorrowingHistory();
                    history.setIdHistory(rs.getInt("IdHistory"));
                    history.setMemberId(rs.getInt("MemberID"));
                    history.setBookCopyId(rs.getInt("BookCopyId"));
                    history.setBorrowDate(rs.getTimestamp("BorrowDate"));
                    history.setReturnDate(rs.getTimestamp("ReturnDate"));
                    history.setBookTitle(rs.getString("bookTitle"));
                    return history;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving borrowing history by IdHistory: {0}", idHistory);
            throw e;
        }
        return null;
    }
}