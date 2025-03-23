package dao;

import model.Fines;
import utils.LibraryContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FinesDAO extends LibraryContext {
    private static final Logger LOGGER = Logger.getLogger(FinesDAO.class.getName());

    public FinesDAO() {
        super();
    }

    // Thanh toán khoản phạt và ghi vào FinePayments
    public boolean payFine(int fineId, double amountPaid, String paymentMethod) {
        if (amountPaid <= 0) {
            LOGGER.log(Level.WARNING, "Invalid amount paid: {0} for fineId: {1}", new Object[]{amountPaid, fineId});
            return false;
        }
        if (!isValidPaymentMethod(paymentMethod)) {
            paymentMethod = "Other";
            LOGGER.log(Level.INFO, "Invalid payment method for fineId: {0}, defaulting to 'Other'", fineId);
        }

        FinePaymentsDAO finePaymentsDAO = new FinePaymentsDAO();

        // Bước 1: Cập nhật Fines
        String updateFineQuery = "UPDATE Fines SET Status = 'Paid', PaidDate = GETDATE(), PaymentMethod = ? WHERE IdFine = ? AND Status = 'Unpaid'";
        try (PreparedStatement ps = conn.prepareStatement(updateFineQuery)) {
            ps.setString(1, paymentMethod);
            ps.setInt(2, fineId);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                LOGGER.log(Level.WARNING, "No rows updated for fineId: {0}. Already paid or not found.", fineId);
                return false;
            }
            LOGGER.log(Level.INFO, "Updated Fines table for fineId: {0}", fineId);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQLException in payFine while updating Fines for fineId: {0}", fineId);
            return false;
        }

        // Bước 2: Ghi vào FinePayments
        boolean paymentAdded = finePaymentsDAO.addFinePayment(fineId, amountPaid, paymentMethod);
        if (!paymentAdded) {
            LOGGER.log(Level.WARNING, "Failed to record payment for fineId: {0}", fineId);
            return false;
        }

        LOGGER.log(Level.INFO, "Payment successful for fineId: {0}", fineId);
        return true;
    }

    // Thêm khoản phạt mới
    public boolean addFine(int memberId, int borrowId, double amount, String paymentMethod) {
        if (!isValidPaymentMethod(paymentMethod)) {
            paymentMethod = "Other";
        }
        String insertFineQuery = "INSERT INTO Fines (MemberID, BorrowID, Amount, Status, PaymentMethod, CreatedAt) " +
                "VALUES (?, ?, ?, 'Unpaid', ?, GETDATE())";
        try (PreparedStatement ps = conn.prepareStatement(insertFineQuery, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, memberId);
            ps.setInt(2, borrowId);
            ps.setDouble(3, amount);
            ps.setString(4, paymentMethod);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0 && ps.getGeneratedKeys().next();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQLException in addFine", e);
            return false;
        }
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
            LOGGER.log(Level.SEVERE, "SQLException in updateFineStatus for fineId: {0}", fineId);
            return false;
        }
    }

    // Tìm kiếm khoản phạt theo MemberID hoặc BorrowID
    public List<Fines> searchFinesByMemberIdOrBorrowId(String keyword) throws SQLException {
        List<Fines> finesList = new ArrayList<>();
        String sql = "SELECT * FROM Fines WHERE CAST(MemberID AS NVARCHAR) LIKE ? OR CAST(BorrowID AS NVARCHAR) LIKE ? ORDER BY CreatedAt DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            String searchPattern = "%" + keyword + "%";
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    finesList.add(mapResultSetToFines(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error searching fines with keyword: {0}", keyword);
            throw e;
        }
        return finesList;
    }

    // Lấy tất cả khoản phạt
    public List<Fines> getAllFines() throws SQLException {
        List<Fines> finesList = new ArrayList<>();
        String sql = "SELECT * FROM Fines ORDER BY CreatedAt DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                finesList.add(mapResultSetToFines(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving all fines", e);
            throw e;
        }
        return finesList;
    }

    // Lấy khoản phạt theo ID
    public Fines getFinesById(int fineId) throws SQLException {
        String sql = "SELECT * FROM Fines WHERE IdFine = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, fineId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToFines(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving fine by id: {0}", fineId);
            throw e;
        }
        return null;
    }

    // Lấy tất cả khoản phạt theo MemberID
    public List<Fines> getFinesByMemberId(int memberId) {
        List<Fines> finesList = new ArrayList<>();
        String query = "SELECT * FROM Fines WHERE MemberID = ? ORDER BY CreatedAt DESC";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, memberId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    finesList.add(mapResultSetToFines(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQLException in getFinesByMemberId: {0}", memberId);
        }
        return finesList;
    }

    // Tạo khoản phạt nếu cần (dựa trên Borrowing)
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
                    if (daysLate > 0) {
                        amount = daysLate * 50; // 50 USD/ngày trễ
                    }

                    if (amount > 0 && !isFineExists(borrowId)) {
                        addFine(memberId, borrowId, amount, "Other");
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQLException in createFineIfNeeded for borrowId: {0}", borrowId);
        }
    }

    // Kiểm tra xem khoản phạt đã tồn tại chưa
    public boolean isFineExists(int borrowId) {
        String query = "SELECT COUNT(*) FROM Fines WHERE BorrowID = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, borrowId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQLException in isFineExists for borrowId: {0}", borrowId);
        }
        return false;
    }

    // Tính tổng số tiền phạt chưa thanh toán
    public double getTotalFinesAmount() throws SQLException {
        String query = "SELECT COUNT(Amount) AS total FROM Fines WHERE Status = 'Unpaid'";
        try (PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getDouble("total");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error calculating total fines amount", e);
            throw e;
        }
        return 0.0;
    }

    // Kiểm tra PaymentMethod hợp lệ
    private boolean isValidPaymentMethod(String paymentMethod) {
        return paymentMethod != null &&
                (paymentMethod.equals("Cash") ||
                        paymentMethod.equals("Credit Card") ||
                        paymentMethod.equals("Online") ||
                        paymentMethod.equals("Other"));
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