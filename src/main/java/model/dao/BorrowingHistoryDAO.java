package model.dao;

import model.entity.Borrowing;
import model.entity.BorrowingHistory;
import model.utils.LibraryContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class BorrowingHistoryDAO extends LibraryContext {

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
        String sql = "INSERT INTO BorrowingHistory (MemberID, BookID, BookCopyId, BorrowDate, ReturnDate) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, borrowing.getMemberId());
            ps.setInt(2, borrowing.getBookId());
            ps.setInt(3, borrowing.getBookCopyId());
            ps.setTimestamp(4, new Timestamp(borrowing.getBorrowDate().getTime()));
            ps.setTimestamp(5, null); // ReturnDate is null when borrowing starts

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Updates the return date for a borrowing record when a book is returned.
     *
     * @param borrowing The Borrowing object with return details
     * @return true if the update is successful, false otherwise
     */
    public boolean updateReturnHistory(Borrowing borrowing) {
        String sql = "UPDATE BorrowingHistory SET ReturnDate = ? " +
                "WHERE BookCopyId = ? AND MemberID = ? AND ReturnDate IS NULL";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, new Timestamp(borrowing.getReturnDate().getTime()));
            ps.setInt(2, borrowing.getBookCopyId());
            ps.setInt(3, borrowing.getMemberId());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
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
        String sql = "SELECT bh.IdHistory, bh.MemberID, bh.BookID, bh.BookCopyId, bh.BorrowDate, bh.ReturnDate, b.title AS bookTitle " +
                "FROM BorrowingHistory bh " +
                "JOIN Books b ON bh.BookID = b.IdBook " +
                "WHERE bh.MemberID = ? " +
                "ORDER BY bh.BorrowDate DESC";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, memberId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    BorrowingHistory history = new BorrowingHistory();
                    history.setIdHistory(rs.getInt("IdHistory"));
                    history.setMemberId(rs.getInt("MemberID"));
                    history.setBookId(rs.getInt("BookID"));
                    history.setBookCopyId(rs.getInt("BookCopyId"));
                    history.setBorrowDate(rs.getTimestamp("BorrowDate"));
                    history.setReturnDate(rs.getTimestamp("ReturnDate"));// Set the transient bookTitle field
                    historyList.add(history);
                }
            }
        }
        return historyList;
    }
}