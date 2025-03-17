package model.dao;

import model.entity.Fines;
import model.utils.LibraryContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FinesDAO extends LibraryContext {

    public FinesDAO() {
        super(); // Kết nối tới cơ sở dữ liệu thông qua lớp cha LibraryContext
    }

    // Thêm phạt vào bảng Fines
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
                if (rs.next()) {
                    int fineId = rs.getInt(1); // Lấy IdFine của phạt mới
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Cập nhật trạng thái phạt (khi thanh toán phạt)
    public boolean updateFineStatus(int fineId, String status, Date paidDate) {
        String updateFineQuery = "UPDATE Fines SET Status = ?, PaidDate = ? WHERE IdFine = ?";
        try (PreparedStatement ps = conn.prepareStatement(updateFineQuery)) {
            ps.setString(1, status);
            ps.setTimestamp(2, new Timestamp(paidDate.getTime()));
            ps.setInt(3, fineId);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Lấy tất cả phạt của một thành viên
    public List<Fines> getFinesByMemberId(int memberId) {
        List<Fines> finesList = new ArrayList<>();
        String query = "SELECT * " + "FROM Fines WHERE MemberID = ?";
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

    // Map kết quả từ ResultSet thành đối tượng Fines
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
