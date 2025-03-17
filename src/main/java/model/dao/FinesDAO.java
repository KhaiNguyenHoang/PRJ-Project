package model.dao;

import model.entity.Borrowing;
import model.entity.Fines;
import model.utils.LibraryContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FinesDAO extends LibraryContext {

    public FinesDAO() {
        super(); // Kết nối tới cơ sở dữ liệu thông qua lớp cha LibraryContext
    }

    // Tính và thêm phạt khi trả sách muộn
    public boolean addFineIfNeeded(Borrowing borrowing, Date returnDate) {
        // Tính số ngày trễ
        long overdueDays = calculateOverdueDays(borrowing.getDueDate(), returnDate);

        if (overdueDays > 0) {
            double fineAmount = calculateFine(overdueDays);
            String fineStatus = overdueDays > 30 ? "Overdue" : "Unpaid";
            return insertFine(borrowing, fineAmount, fineStatus, overdueDays);
        }
        return true;
    }

    // Tính số ngày trễ giữa ngày trả sách và ngày hết hạn
    private long calculateOverdueDays(Date dueDate, Date returnDate) {
        return (returnDate.getTime() - dueDate.getTime()) / (1000 * 60 * 60 * 24); // Tính số ngày trễ
    }

    // Tính tiền phạt dựa trên số ngày trễ
    private double calculateFine(long overdueDays) {
        if (overdueDays > 30) {
            return 500000; // Phạt 500.000 nếu quá 30 ngày
        }
        return (double) overdueDays * 50000; // Phạt 50.000 mỗi ngày nếu quá hạn từ 1 đến 30 ngày
    }

    // Thêm phạt vào bảng Fines và ghi lại lịch sử
    private boolean insertFine(Borrowing borrowing, double fineAmount, String fineStatus, long overdueDays) {
        String insertFineQuery = "INSERT INTO Fines (MemberID, BorrowID, Amount, Status, CreatedAt) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(insertFineQuery, Statement.RETURN_GENERATED_KEYS)) {
            conn.setAutoCommit(false); // Bắt đầu transaction

            ps.setInt(1, borrowing.getMemberId());
            ps.setInt(2, borrowing.getIdBorrow());
            ps.setDouble(3, fineAmount);
            ps.setString(4, fineStatus);
            ps.setTimestamp(5, new Timestamp(new Date().getTime()));

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                conn.rollback();
                return false; // Không thể thêm phạt
            }

            // Lấy ID phạt vừa được thêm và ghi lại lịch sử phạt
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int fineId = rs.getInt(1);
                addFineHistory(fineId, fineAmount, borrowing.getMemberId(), fineStatus, new Date());
            }

            conn.commit(); // Commit transaction
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                conn.rollback(); // Rollback nếu có lỗi
            } catch (SQLException se) {
                se.printStackTrace();
            }
        } finally {
            try {
                conn.setAutoCommit(true); // Đảm bảo bật lại auto-commit
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return false;
    }

    // Thêm lịch sử phạt
    private void addFineHistory(int fineId, double amount, int memberId, String status, Date createdAt) {
        String insertHistoryQuery = "INSERT INTO FineHistory (FineID, Amount, MemberID, Status, CreatedAt) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(insertHistoryQuery)) {
            ps.setInt(1, fineId);
            ps.setDouble(2, amount);
            ps.setInt(3, memberId);
            ps.setString(4, status);
            ps.setTimestamp(5, new Timestamp(createdAt.getTime()));
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Thanh toán phạt
    public boolean payFine(int fineId, double amountPaid, String paymentMethod) {
        String updateFineQuery = "UPDATE Fines SET Status = 'Paid', PaidDate = ?, PaymentMethod = ? WHERE IdFine = ?";
        try (PreparedStatement ps = conn.prepareStatement(updateFineQuery)) {
            conn.setAutoCommit(false); // Bắt đầu transaction

            ps.setTimestamp(1, new Timestamp(new Date().getTime()));
            ps.setString(2, paymentMethod);
            ps.setInt(3, fineId);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                conn.rollback();
                return false; // Không thể cập nhật trạng thái phạt
            }

            // Thêm thông tin thanh toán vào bảng FinePayments
            addFinePayment(fineId, amountPaid, paymentMethod);

            conn.commit(); // Commit transaction
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                conn.rollback(); // Rollback nếu có lỗi
            } catch (SQLException se) {
                se.printStackTrace();
            }
        } finally {
            try {
                conn.setAutoCommit(true); // Đảm bảo bật lại auto-commit
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return false;
    }

    // Thêm thông tin thanh toán vào bảng FinePayments
    private void addFinePayment(int fineId, double amountPaid, String paymentMethod) throws SQLException {
        String insertPaymentQuery = "INSERT INTO FinePayments (FineID, AmountPaid, PaymentDate, PaymentMethod) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(insertPaymentQuery)) {
            ps.setInt(1, fineId);
            ps.setDouble(2, amountPaid);
            ps.setTimestamp(3, new Timestamp(new Date().getTime()));
            ps.setString(4, paymentMethod);
            ps.executeUpdate();
        }
    }

    // Lấy tất cả các phạt
    public List<Fines> getAllFines() {
        ArrayList<Fines> finesList = new ArrayList<>();
        String query = "SELECT * " + "FROM Fines";
        try (PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Fines fine = new Fines();
                fine.setIdFine(rs.getInt("IdFine"));
                fine.setMemberId(rs.getInt("MemberID"));
                fine.setBorrowId(rs.getInt("BorrowID"));
                fine.setAmount(rs.getDouble("Amount"));
                fine.setStatus(rs.getString("Status"));
                fine.setCreatedAt(rs.getTimestamp("CreatedAt"));
                fine.setPaidDate(rs.getTimestamp("PaidDate"));
                fine.setPaymentMethod(rs.getString("PaymentMethod"));
                finesList.add(fine);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return finesList;
    }
}
