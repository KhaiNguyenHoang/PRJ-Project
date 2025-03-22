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
     */
    public boolean addBorrowingHistory(BorrowingHistory borrowing) {
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
            LOGGER.log(Level.SEVERE, "Error adding borrowing history for MemberID: {0}, BookCopyId: {1}",
                    new Object[]{borrowing.getMemberId(), borrowing.getBookCopyId()});
            return false;
        }
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
     */
    public boolean updateReturnHistory(BorrowingHistory borrowing) {
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
            LOGGER.log(Level.SEVERE, "Error updating borrowing history for MemberID: {0}, BookCopyId: {1}",
                    new Object[]{borrowing.getMemberId(), borrowing.getBookCopyId()});
            return false;
        }
    }

    /**
     * Retrieves the borrowing history for a specific member.
     */
    public List<BorrowingHistory> getBorrowingHistoryByMemberId(int memberId) throws SQLException {
        List<BorrowingHistory> historyList = new ArrayList<>();
        String sql = "SELECT IdHistory, MemberID, BookCopyId, BorrowDate, ReturnDate " +
                "FROM BorrowingHistory " +
                "WHERE MemberID = ? " +
                "ORDER BY BorrowDate DESC";

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
                    historyList.add(history);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving borrowing history for member: {0}", memberId);
            throw e;
        }
        return historyList;
    }

    /**
     * Retrieves a BorrowingHistory record by its IdHistory.
     */
    public BorrowingHistory getBorrowingHistoryById(int idHistory) throws SQLException {
        String sql = "SELECT IdHistory, MemberID, BookCopyId, BorrowDate, ReturnDate " +
                "FROM BorrowingHistory " +
                "WHERE IdHistory = ?";

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
                    return history;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving borrowing history by IdHistory: {0}", idHistory);
            throw e;
        }
        return null;
    }

    /**
     * Retrieves all borrowing history records.
     */
    public List<BorrowingHistory> getAllBorrowingHistory() throws SQLException {
        List<BorrowingHistory> historyList = new ArrayList<>();
        String sql = "SELECT IdHistory, MemberID, BookCopyId, BorrowDate, ReturnDate " +
                "FROM BorrowingHistory " +
                "ORDER BY BorrowDate DESC";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    BorrowingHistory history = new BorrowingHistory();
                    history.setIdHistory(rs.getInt("IdHistory"));
                    history.setMemberId(rs.getInt("MemberID"));
                    history.setBookCopyId(rs.getInt("BookCopyId"));
                    history.setBorrowDate(rs.getTimestamp("BorrowDate"));
                    history.setReturnDate(rs.getTimestamp("ReturnDate"));
                    historyList.add(history);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving all borrowing history", e);
            throw e;
        }
        return historyList;
    }
}