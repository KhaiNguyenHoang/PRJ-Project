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
        super();
    }

    public boolean addBorrowingHistory(Borrowing borrowing) {
        String insertHistoryQuery = "INSERT INTO BorrowingHistory (MemberID, BookID, BookCopyId, BorrowDate, ReturnDate) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(insertHistoryQuery)) {
            ps.setInt(1, borrowing.getMemberId());
            ps.setInt(2, borrowing.getBookId());
            ps.setInt(3, borrowing.getBookCopyId());
            ps.setTimestamp(4, new Timestamp(borrowing.getBorrowDate().getTime()));
            ps.setTimestamp(5, null);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateReturnHistory(Borrowing borrowing) {
        String updateHistoryQuery = "UPDATE BorrowingHistory SET ReturnDate = ? WHERE BookCopyId = ? AND MemberID = ? AND ReturnDate IS NULL";
        try (PreparedStatement ps = conn.prepareStatement(updateHistoryQuery)) {
            ps.setTimestamp(1, new Timestamp(borrowing.getReturnDate().getTime()));
            ps.setInt(2, borrowing.getBookCopyId());
            ps.setInt(3, borrowing.getMemberId());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<BorrowingHistory> getBorrowingHistoryByMemberId(int memberId) {
        List<BorrowingHistory> historyList = new ArrayList<>();
        String query = "SELECT * FROM BorrowingHistory WHERE MemberID = ? ORDER BY BorrowDate DESC";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, memberId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    historyList.add(mapResultSetToBorrowingHistory(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return historyList;
    }

    private BorrowingHistory mapResultSetToBorrowingHistory(ResultSet rs) throws SQLException {
        BorrowingHistory history = new BorrowingHistory();
        history.setIdHistory(rs.getInt("IdHistory"));
        history.setMemberId(rs.getInt("MemberID"));
        history.setBookId(rs.getInt("BookID"));
        history.setBookCopyId(rs.getInt("BookCopyId"));
        history.setBorrowDate(rs.getTimestamp("BorrowDate")); // Assuming BorrowingHistory uses java.util.Date
        history.setReturnDate(rs.getTimestamp("ReturnDate"));
        return history;
    }
}