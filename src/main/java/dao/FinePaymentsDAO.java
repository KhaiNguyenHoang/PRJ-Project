package dao;

import model.FinePayments;
import utils.LibraryContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FinePaymentsDAO extends LibraryContext {
    private static final Logger LOGGER = Logger.getLogger(FinePaymentsDAO.class.getName());

    public FinePaymentsDAO() {
        super();
    }

    // Thêm thanh toán phạt
    public boolean addFinePayment(int fineId, double amountPaid, String paymentMethod) {
        if (amountPaid <= 0) {
            LOGGER.log(Level.WARNING, "Invalid amount paid: {0} for fineId: {1}", new Object[]{amountPaid, fineId});
            return false;
        }
        if (!isValidPaymentMethod(paymentMethod)) {
            paymentMethod = "Other";
            LOGGER.log(Level.INFO, "Invalid payment method for fineId: {0}, defaulting to 'Other'", fineId);
        }

        String insertPaymentQuery = "INSERT INTO FinePayments (FineID, AmountPaid, PaymentDate, PaymentMethod) " +
                "VALUES (?, ?, GETDATE(), ?)";
        try (PreparedStatement ps = conn.prepareStatement(insertPaymentQuery)) {
            ps.setInt(1, fineId);
            ps.setDouble(2, amountPaid);
            ps.setString(3, paymentMethod);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                LOGGER.log(Level.INFO, "Payment recorded for fineId: {0}", fineId);
                return true;
            } else {
                LOGGER.log(Level.WARNING, "No rows inserted for fineId: {0}", fineId);
                return false;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQLException in addFinePayment for fineId: {0}", fineId);
            return false;
        }
    }

    // Lấy tất cả thanh toán theo FineID
    public List<FinePayments> getFinePaymentsByFineId(int fineId) {
        List<FinePayments> finePaymentsList = new ArrayList<>();
        String query = "SELECT * FROM FinePayments WHERE FineID = ? ORDER BY PaymentDate DESC";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, fineId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    finePaymentsList.add(mapResultSetToFinePayments(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQLException in getFinePaymentsByFineId: {0}", fineId);
        }
        return finePaymentsList;
    }

    // Lấy tất cả thanh toán
    public List<FinePayments> getAllFinePayments() {
        List<FinePayments> finePaymentsList = new ArrayList<>();
        String query = "SELECT * FROM FinePayments ORDER BY PaymentDate DESC";
        try (PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                finePaymentsList.add(mapResultSetToFinePayments(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQLException in getAllFinePayments", e);
        }
        return finePaymentsList;
    }

    // Xóa thanh toán theo FineID
    public boolean deleteFinePaymentByFineId(int fineId) {
        String deleteQuery = "DELETE FROM FinePayments WHERE FineID = ?";
        try (PreparedStatement ps = conn.prepareStatement(deleteQuery)) {
            ps.setInt(1, fineId);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                LOGGER.log(Level.INFO, "Deleted payments for fineId: {0}", fineId);
                return true;
            }
            return false;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQLException in deleteFinePaymentByFineId: {0}", fineId);
            return false;
        }
    }

    // Cập nhật thanh toán
    public boolean updateFinePayment(int paymentId, double amountPaid, String paymentMethod, Timestamp paymentDate) {
        if (amountPaid <= 0) {
            LOGGER.log(Level.WARNING, "Invalid amount paid: {0} for paymentId: {1}", new Object[]{amountPaid, paymentId});
            return false;
        }
        if (!isValidPaymentMethod(paymentMethod)) {
            paymentMethod = "Other";
        }

        String updateQuery = "UPDATE FinePayments SET AmountPaid = ?, PaymentMethod = ?, PaymentDate = ? WHERE IdPayment = ?";
        try (PreparedStatement ps = conn.prepareStatement(updateQuery)) {
            ps.setDouble(1, amountPaid);
            ps.setString(2, paymentMethod);
            ps.setTimestamp(3, paymentDate);
            ps.setInt(4, paymentId);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                LOGGER.log(Level.INFO, "Updated payment for paymentId: {0}", paymentId);
                return true;
            }
            return false;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQLException in updateFinePayment for paymentId: {0}", paymentId);
            return false;
        }
    }

    // Lấy thanh toán theo IdPayment
    public FinePayments getFinePaymentById(int paymentId) {
        String query = "SELECT * FROM FinePayments WHERE IdPayment = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, paymentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToFinePayments(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQLException in getFinePaymentById: {0}", paymentId);
        }
        return null;
    }

    // Kiểm tra PaymentMethod hợp lệ
    private boolean isValidPaymentMethod(String paymentMethod) {
        return paymentMethod != null &&
                (paymentMethod.equals("Cash") ||
                        paymentMethod.equals("Credit Card") ||
                        paymentMethod.equals("Online") ||
                        paymentMethod.equals("Other"));
    }

    // Map ResultSet thành FinePayments
    private FinePayments mapResultSetToFinePayments(ResultSet rs) throws SQLException {
        FinePayments finePayments = new FinePayments();
        finePayments.setIdPayment(rs.getInt("IdPayment"));
        finePayments.setFineId(rs.getInt("FineID"));
        finePayments.setAmountPaid(rs.getDouble("AmountPaid"));
        finePayments.setPaymentDate(rs.getTimestamp("PaymentDate"));
        finePayments.setPaymentMethod(rs.getString("PaymentMethod"));
        return finePayments;
    }
}