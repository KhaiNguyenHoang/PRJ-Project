package model.dao;

import model.entity.FinePayments;
import model.utils.LibraryContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class FinePaymentsDAO extends LibraryContext {

    public FinePaymentsDAO() {
        super(); // Kết nối tới cơ sở dữ liệu thông qua lớp cha LibraryContext
    }

    // Thêm thanh toán phạt vào bảng FinePayments
    public boolean addFinePayment(int fineId, double amountPaid, String paymentMethod) {
        String insertPaymentQuery = "INSERT INTO FinePayments (FineID, AmountPaid, PaymentDate, PaymentMethod) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(insertPaymentQuery)) {
            ps.setInt(1, fineId);
            ps.setDouble(2, amountPaid);
            ps.setTimestamp(3, new Timestamp(new java.util.Date().getTime())); // Lấy thời gian hiện tại
            ps.setString(4, paymentMethod);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Lấy tất cả thanh toán phạt theo FineID
    public List<FinePayments> getFinePaymentsByFineId(int fineId) {
        List<FinePayments> finePaymentsList = new ArrayList<>();
        String query = "SELECT * " + "FROM FinePayments WHERE FineID = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, fineId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    finePaymentsList.add(mapResultSetToFinePayments(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return finePaymentsList;
    }

    // Lấy tất cả thanh toán phạt
    public List<FinePayments> getAllFinePayments() {
        List<FinePayments> finePaymentsList = new ArrayList<>();
        String query = "SELECT * " + "FROM FinePayments";
        try (PreparedStatement ps = conn.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                finePaymentsList.add(mapResultSetToFinePayments(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return finePaymentsList;
    }

    // Xóa thanh toán phạt theo IdPayment
    public boolean deleteFinePayment(int paymentId) {
        String deleteQuery = "DELETE FROM FinePayments WHERE IdPayment = ?";
        try (PreparedStatement ps = conn.prepareStatement(deleteQuery)) {
            ps.setInt(1, paymentId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Map kết quả từ ResultSet thành đối tượng FinePayments
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
