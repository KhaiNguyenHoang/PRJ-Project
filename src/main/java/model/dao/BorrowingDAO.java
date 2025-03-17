package model.dao;

import model.entity.Borrowing;
import model.utils.LibraryContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BorrowingDAO extends LibraryContext {

    public BorrowingDAO() {
        super(); // Kết nối tới cơ sở dữ liệu thông qua lớp cha LibraryContext
    }

    // Mượn sách (Thêm bản ghi mượn sách mới)
    public boolean borrowBook(Borrowing borrowing) {
        PreparedStatement ps = null;
        BookCopiesDAO bookCopiesDAO = new BookCopiesDAO();
        BorrowingHistoryDAO borrowingHistoryDAO = new BorrowingHistoryDAO();
        BooksDAO booksDAO = new BooksDAO();

        try {
            conn.setAutoCommit(false); // Tắt chế độ tự động commit

            // Kiểm tra nếu có bản sao sách nào có sẵn không
            if (!bookCopiesDAO.hasAvailableCopy(borrowing.getBookId())) {
                return false; // Không có bản sao sách có sẵn
            }

            // Kiểm tra ngày mượn và ngày trả hợp lệ
            if (borrowing.getDueDate().before(borrowing.getBorrowDate())) {
                return false; // Ngày trả không thể trước ngày mượn
            }

            // Mượn sách
            String borrowQuery = "INSERT INTO Borrowing (MemberID, BookID, BookCopyId, BorrowDate, DueDate, Status) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(borrowQuery);
            ps.setInt(1, borrowing.getMemberId());
            ps.setInt(2, borrowing.getBookId());
            ps.setInt(3, borrowing.getBookCopyId());
            ps.setTimestamp(4, new Timestamp(borrowing.getBorrowDate().getTime()));
            ps.setTimestamp(5, new Timestamp(borrowing.getDueDate().getTime()));
            ps.setString(6, borrowing.getStatus());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                conn.rollback(); // Nếu không thành công thì rollback
                return false;
            }

            // Cập nhật trạng thái bản sao sách
            boolean bookUpdated = bookCopiesDAO.updateBookCopyStatus(borrowing.getBookCopyId(), "Borrowed");
            if (!bookUpdated) {
                conn.rollback(); // Rollback nếu không thể cập nhật trạng thái sách
                return false;
            }

            // Cập nhật trạng thái sách trong bảng Books nếu cần
            booksDAO.updateBookStatusBasedOnCopies(borrowing.getBookId());

            // Ghi lịch sử mượn sách
            boolean historyAdded = borrowingHistoryDAO.addBorrowingHistory(borrowing);
            if (!historyAdded) {
                conn.rollback(); // Rollback nếu không thể ghi lịch sử
                return false;
            }

            // Commit giao dịch
            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (conn != null) {
                    conn.rollback(); // Rollback nếu có lỗi
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (conn != null) {
                    conn.setAutoCommit(true); // Set lại auto-commit sau khi kết thúc giao dịch
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return false;
    }


    // Trả sách (Cập nhật trạng thái sách và ghi lại lịch sử trả sách)
    public boolean returnBook(int idBorrow, Date returnDate) {
        PreparedStatement ps = null;
        Borrowing borrowing = null;
        BookCopiesDAO bookCopiesDAO = new BookCopiesDAO();
        BorrowingHistoryDAO borrowingHistoryDAO = new BorrowingHistoryDAO();

        try {
            conn.setAutoCommit(false); // Tắt chế độ tự động commit

            // Lấy thông tin mượn sách
            borrowing = getBorrowingById(idBorrow);
            if (borrowing == null) {
                conn.rollback(); // Rollback nếu không tìm thấy mượn sách
                return false;
            }

            // Kiểm tra ngày trả không thể trước ngày mượn
            if (returnDate.before(borrowing.getBorrowDate())) {
                return false; // Ngày trả không thể trước ngày mượn
            }

            // Cập nhật trạng thái trả sách
            String updateReturnQuery = "UPDATE Borrowing SET ReturnDate = ?, Status = 'Returned' WHERE IdBorrow = ?";
            ps = conn.prepareStatement(updateReturnQuery);
            ps.setTimestamp(1, new Timestamp(returnDate.getTime()));
            ps.setInt(2, idBorrow);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                conn.rollback(); // Rollback nếu không thể cập nhật trả sách
                return false;
            }

            // Cập nhật lại trạng thái bản sao sách
            boolean bookUpdated = bookCopiesDAO.updateBookCopyStatus(borrowing.getBookCopyId(), "Available");
            if (!bookUpdated) {
                conn.rollback(); // Rollback nếu không thể cập nhật trạng thái sách
                return false;
            }

            // Cập nhật lại trạng thái sách trong bảng Books nếu cần
            BooksDAO booksDAO = new BooksDAO();
            booksDAO.updateBookStatusBasedOnCopies(borrowing.getBookId());

            // Ghi lại lịch sử trả sách (cập nhật lại)
            boolean historyAdded = borrowingHistoryDAO.updateReturnHistory(borrowing);
            if (!historyAdded) {
                conn.rollback(); // Rollback nếu không thể ghi lịch sử trả sách
                return false;
            }

            // Commit giao dịch
            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (conn != null) {
                    conn.rollback(); // Rollback nếu có lỗi
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (conn != null) {
                    conn.setAutoCommit(true); // Set lại auto-commit sau khi kết thúc giao dịch
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return false;
    }

    // Lấy thông tin mượn sách theo IdBorrow
    public Borrowing getBorrowingById(int idBorrow) {
        String query = "SELECT * " + "FROM Borrowing WHERE IdBorrow = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, idBorrow);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToBorrowing(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Map kết quả từ ResultSet thành đối tượng Borrowing
    private Borrowing mapResultSetToBorrowing(ResultSet rs) throws SQLException {
        Borrowing borrowing = new Borrowing();
        borrowing.setIdBorrow(rs.getInt("IdBorrow"));
        borrowing.setMemberId(rs.getInt("MemberID"));
        borrowing.setBookId(rs.getInt("BookID"));
        borrowing.setBookCopyId(rs.getInt("BookCopyId"));
        borrowing.setBorrowDate(rs.getTimestamp("BorrowDate"));
        borrowing.setDueDate(rs.getTimestamp("DueDate"));
        borrowing.setReturnDate(rs.getTimestamp("ReturnDate"));
        borrowing.setStatus(rs.getString("Status"));
        borrowing.setCreatedAt(rs.getTimestamp("CreatedAt"));
        return borrowing;
    }

    // Lấy tất cả các mượn sách của thành viên
    public List<Borrowing> getAllBorrowingsByMemberId(int memberId) {
        List<Borrowing> borrowings = new ArrayList<>();
        String query = "SELECT * " + "FROM Borrowing WHERE MemberID = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, memberId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    borrowings.add(mapResultSetToBorrowing(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return borrowings;
    }
}
