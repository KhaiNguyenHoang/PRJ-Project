package dao;

import model.FinePayments;
import utils.LibraryContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FinePaymentsDAO extends LibraryContext {

    public FinePaymentsDAO() {
        super();
    }

    // Thêm thanh toán phạt
    public boolean addFinePayment(int fineId, double amountPaid, String paymentMethod) {
        // Kiểm tra số tiền hợp lệ
        if (amountPaid <= 0) {
            System.out.println("Số tiền thanh toán phải lớn hơn 0.");
            return false;
        }

        // Đảm bảo paymentMethod hợp lệ theo CHECK constraint
        if (!isValidPaymentMethod(paymentMethod)) {
            paymentMethod = "Other"; // Giá trị mặc định nếu không hợp lệ
        }

        String insertPaymentQuery = "INSERT INTO FinePayments (FineID, AmountPaid, PaymentDate, PaymentMethod) " +
                "VALUES (?, ?, GETDATE(), ?)";
        try (PreparedStatement ps = conn.prepareStatement(insertPaymentQuery)) {
            ps.setInt(1, fineId);
            ps.setDouble(2, amountPaid);
            ps.setString(3, paymentMethod);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                return true;
            } else {
                System.out.println("Không thể thêm bản ghi thanh toán cho FineID: " + fineId);
                return false;
            }
        } catch (SQLException e) {
            System.out.println("SQLException in addFinePayment: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Kiểm tra PaymentMethod hợp lệ
    private boolean isValidPaymentMethod(String paymentMethod) {
        return paymentMethod != null &&
                (paymentMethod.equals("Cash") ||
                        paymentMethod.equals("Credit Card") ||
                        paymentMethod.equals("Online") ||
                        paymentMethod.equals("Other"));
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
            System.out.println("SQLException in getFinePaymentsByFineId: " + e.getMessage());
            e.printStackTrace();
        }
        return finePaymentsList;
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