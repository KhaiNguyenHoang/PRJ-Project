package dao;

import model.Fines;
import utils.LibraryContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class FinesDAO extends LibraryContext {

    public FinesDAO() {
        super();
    }

    // Thêm khoản phạt mới
    public boolean addFine(int memberId, int borrowId, double amount, String paymentMethod) {
        String insertFineQuery = "INSERT INTO Fines (MemberID, BorrowID, Amount, Status, PaymentMethod, CreatedAt) " +
                "VALUES (?, ?, ?, 'Unpaid', ?, GETDATE())";
        try (PreparedStatement ps = conn.prepareStatement(insertFineQuery, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, memberId);
            ps.setInt(2, borrowId);
            ps.setDouble(3, amount);
            ps.setString(4, paymentMethod);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Cập nhật trạng thái phạt
    public boolean updateFineStatus(int fineId, String status, Date paidDate) {
        String updateFineQuery = "UPDATE Fines SET Status = ?, PaidDate = ? WHERE IdFine = ?";
        try (PreparedStatement ps = conn.prepareStatement(updateFineQuery)) {
            ps.setString(1, status);
            ps.setTimestamp(2, paidDate != null ? new Timestamp(paidDate.getTime()) : null);
            ps.setInt(3, fineId);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Lấy tất cả khoản phạt theo MemberID
    public List<Fines> getFinesByMemberId(int memberId) {
        List<Fines> finesList = new ArrayList<>();
        String query = "SELECT * FROM Fines WHERE MemberID = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, memberId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    finesList.add(mapResultSetToFines(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return finesList;
    }

    // Tạo khoản phạt nếu cần
    public void createFineIfNeeded(int borrowId) {
        String query = "SELECT MemberID, DueDate, ReturnDate FROM Borrowing WHERE IdBorrow = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, borrowId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int memberId = rs.getInt("MemberID");
                    Date dueDate = rs.getDate("DueDate");
                    Date returnDate = rs.getDate("ReturnDate");
                    Date currentDate = new Date();

                    long daysLate;
                    if (returnDate != null) {
                        daysLate = calculateDaysBetween(dueDate, returnDate);
                    } else {
                        daysLate = calculateDaysBetween(dueDate, currentDate);
                    }

                    double amount = 0;
                    if (daysLate > 30 && returnDate == null) {
                        amount = 500000; // Phí cố định nếu quá 30 ngày chưa trả
                    } else if (daysLate > 0) {
                        amount = daysLate * 50000; // 50,000 VND/ngày
                    }

                    if (amount > 0 && !isFineExists(borrowId)) {
                        addFine(memberId, borrowId, amount, "Other");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Kiểm tra xem khoản phạt đã tồn tại chưa
    private boolean isFineExists(int borrowId) {
        String query = "SELECT COUNT(*) FROM Fines WHERE BorrowID = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, borrowId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public double getTotalFinesAmount() throws SQLException {
        String query = "SELECT SUM(Amount) AS total FROM Fines WHERE Status = 'Unpaid'";
        try (PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getDouble("total");
            }
        }
        return 0.0;
    }

    // Tính số ngày giữa hai ngày
    private long calculateDaysBetween(Date startDate, Date endDate) {
        long diffInMillies = Math.max(endDate.getTime() - startDate.getTime(), 0);
        return TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

    // Map ResultSet thành Fines
    private Fines mapResultSetToFines(ResultSet rs) throws SQLException {
        Fines fines = new Fines();
        fines.setIdFine(rs.getInt("IdFine"));
        fines.setMemberId(rs.getInt("MemberID"));
        fines.setBorrowId(rs.getInt("BorrowID"));
        fines.setAmount(rs.getDouble("Amount"));
        fines.setStatus(rs.getString("Status"));
        fines.setPaidDate(rs.getTimestamp("PaidDate"));
        fines.setPaymentMethod(rs.getString("PaymentMethod"));
        fines.setCreatedAt(rs.getTimestamp("CreatedAt"));
        return fines;
    }
}