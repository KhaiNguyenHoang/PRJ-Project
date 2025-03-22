package dao;

import model.Borrowing;
import model.Fines;
import utils.LibraryContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BorrowingDAO extends LibraryContext {

    public BorrowingDAO() {
        super();
    }

    public static void main(String[] args) {
        BorrowingDAO borrowingDAO = new BorrowingDAO();
        FinesDAO finesDAO = new FinesDAO();
        BookCopiesDAO bookCopiesDAO = new BookCopiesDAO();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        try {
            // Bước 1: Giả lập mượn sách
            System.out.println("=== BƯỚC 1: MƯỢN SÁCH ===");
            Date borrowDate = sdf.parse("01/03/2025");
            Date dueDate = sdf.parse("10/03/2025");
            int memberId = 11;
            int bookId = 52;
            int bookCopyId = bookCopiesDAO.getFirstAvailableBookCopy(bookId).getIdCopy();

            Borrowing borrowing = new Borrowing(0, memberId, bookId, bookCopyId, borrowDate, dueDate, "Borrowed");
            boolean borrowSuccess = borrowingDAO.borrowBook(borrowing);
            if (borrowSuccess) {
                System.out.println("Thành viên " + memberId + " đã mượn sách ID " + bookId + " thành công.");
                System.out.println("Ngày mượn: " + sdf.format(borrowDate) + ", Ngày đến hạn: " + sdf.format(dueDate));
            } else {
                System.out.println("Không thể mượn sách.");
                return;
            }

            // Bước 2: Giả lập trả sách trễ
            System.out.println("\n=== BƯỚC 2: TRẢ SÁCH TRỄ ===");
            Date returnDate = sdf.parse("15/03/2025");
            int borrowId = borrowingDAO.getBorrowingByMemberIdAndBookId(memberId, bookId).getIdBorrow();
            boolean returnSuccess = borrowingDAO.returnBook(borrowId, returnDate);
            if (returnSuccess) {
                System.out.println("Sách đã được trả thành công vào ngày " + sdf.format(returnDate));
                System.out.println("Trễ hạn: " + borrowingDAO.calculateDaysBetween(dueDate, returnDate) + " ngày.");
            } else {
                System.out.println("Không thể trả sách.");
                return;
            }

            // Bước 3: Kiểm tra khoản nợ
            System.out.println("\n=== BƯỚC 3: KIỂM TRA KHOẢN NỢ ===");
            List<Fines> finesList = finesDAO.getFinesByMemberId(memberId);
            if (finesList.isEmpty()) {
                System.out.println("Không có khoản phạt nào cho thành viên " + memberId);
            } else {
                System.out.println("Danh sách khoản phạt của thành viên " + memberId + ":");
                for (Fines fine : finesList) {
                    System.out.println(" - ID phạt: " + fine.getIdFine() +
                            ", BorrowID: " + fine.getBorrowId() +
                            ", Số tiền: " + fine.getAmount() + " USD" +
                            ", Trạng thái: " + fine.getStatus() +
                            ", Phương thức: " + fine.getPaymentMethod() +
                            ", Ngày tạo: " + sdf.format(fine.getCreatedAt()));
                }
            }

        } catch (Exception e) {
            System.out.println("Lỗi trong demo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean borrowBook(Borrowing borrowing) {
        PreparedStatement ps = null;
        BookCopiesDAO bookCopiesDAO = new BookCopiesDAO();
        BorrowingHistoryDAO borrowingHistoryDAO = new BorrowingHistoryDAO();
        BooksDAO booksDAO = new BooksDAO();

        try {
            conn.setAutoCommit(false);

            if (!bookCopiesDAO.hasAvailableCopy(borrowing.getBookId())) {
                System.out.println("No available copies for BookID: " + borrowing.getBookId());
                return false;
            }

            Borrowing existingBorrowing = getBorrowingByMemberIdAndBookId(borrowing.getMemberId(), borrowing.getBookId());
            if (existingBorrowing != null) {
                System.out.println("Member " + borrowing.getMemberId() + " has already borrowed BookID " + borrowing.getBookId() + " and not returned it.");
                return false;
            }

            if (borrowing.getDueDate().before(borrowing.getBorrowDate())) {
                System.out.println("Due date " + borrowing.getDueDate() + " is before borrow date " + borrowing.getBorrowDate());
                return false;
            }

            String borrowQuery = "INSERT INTO Borrowing (MemberID, BookID, BookCopyId, BorrowDate, DueDate, Status) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(borrowQuery);

            java.sql.Date borrowDate = new java.sql.Date(borrowing.getBorrowDate().getTime());
            java.sql.Date dueDate = new java.sql.Date(borrowing.getDueDate().getTime());

            ps.setInt(1, borrowing.getMemberId());
            ps.setInt(2, borrowing.getBookId());
            ps.setInt(3, borrowing.getBookCopyId());
            ps.setDate(4, borrowDate);
            ps.setDate(5, dueDate);
            ps.setString(6, borrowing.getStatus());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                System.out.println("Failed to insert Borrowing record for MemberID: " + borrowing.getMemberId());
                conn.rollback();
                return false;
            }

            boolean bookUpdated = bookCopiesDAO.updateBookCopyStatus(borrowing.getBookCopyId(), "Borrowed");
            if (!bookUpdated) {
                System.out.println("Failed to update BookCopy status for BookCopyId: " + borrowing.getBookCopyId());
                conn.rollback();
                return false;
            }

            booksDAO.updateBookStatusBasedOnCopies(borrowing.getBookId());

            boolean historyAdded = borrowingHistoryDAO.addBorrowingHistory(borrowing);
            if (!historyAdded) {
                System.out.println("Failed to add BorrowingHistory for MemberID: " + borrowing.getMemberId());
                conn.rollback();
                return false;
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            System.out.println("SQLException in borrowBook: " + e.getMessage());
            e.printStackTrace();
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
            return false;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (conn != null) {
                    conn.setAutoCommit(true);
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    public boolean returnBook(int idBorrow, Date returnDate) {
        PreparedStatement ps = null;
        Borrowing borrowing = null;
        BookCopiesDAO bookCopiesDAO = new BookCopiesDAO();
        BorrowingHistoryDAO borrowingHistoryDAO = new BorrowingHistoryDAO();
        FinesDAO finesDAO = new FinesDAO();

        try {
            conn.setAutoCommit(false);

            borrowing = getBorrowingById(idBorrow);
            if (borrowing == null) {
                System.out.println("Borrowing record not found for IdBorrow: " + idBorrow);
                conn.rollback();
                return false;
            }

            if (returnDate.before(borrowing.getBorrowDate())) {
                System.out.println("Return date " + returnDate + " is before borrow date " + borrowing.getBorrowDate());
                conn.rollback();
                return false;
            }

            // Tự động tạo khoản phạt nếu cần
            finesDAO.createFineIfNeeded(idBorrow);

            String updateReturnQuery = "UPDATE Borrowing SET ReturnDate = ?, Status = 'Returned' WHERE IdBorrow = ?";
            ps = conn.prepareStatement(updateReturnQuery);

            java.sql.Date sqlReturnDate = new java.sql.Date(returnDate.getTime());
            ps.setDate(1, sqlReturnDate);
            ps.setInt(2, idBorrow);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                System.out.println("Failed to update Borrowing record for IdBorrow: " + idBorrow);
                conn.rollback();
                return false;
            }

            boolean bookUpdated = bookCopiesDAO.updateBookCopyStatus(borrowing.getBookCopyId(), "Available");
            if (!bookUpdated) {
                System.out.println("Failed to update BookCopy status for BookCopyId: " + borrowing.getBookCopyId());
                conn.rollback();
                return false;
            }

            BooksDAO booksDAO = new BooksDAO();
            booksDAO.updateBookStatusBasedOnCopies(borrowing.getBookId());

            borrowing.setReturnDate(returnDate);
            boolean historyAdded = borrowingHistoryDAO.updateReturnHistory(borrowing);
            if (!historyAdded) {
                System.out.println("Failed to update BorrowingHistory for BookCopyId: " + borrowing.getBookCopyId());
                conn.rollback();
                return false;
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            System.out.println("SQLException in returnBook: " + e.getMessage());
            e.printStackTrace();
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException se) {
                se.printStackTrace();
            }
            return false;
        } finally {
            try {
                if (ps != null) ps.close();
                if (conn != null) conn.setAutoCommit(true);
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    private long calculateDaysBetween(Date startDate, Date endDate) {
        long diffInMillies = Math.max(endDate.getTime() - startDate.getTime(), 0);
        return java.util.concurrent.TimeUnit.DAYS.convert(diffInMillies, java.util.concurrent.TimeUnit.MILLISECONDS);
    }

    public Borrowing getBorrowingById(int idBorrow) {
        String query = "SELECT * FROM Borrowing WHERE IdBorrow = ?";
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

    private Borrowing mapResultSetToBorrowing(ResultSet rs) throws SQLException {
        Borrowing borrowing = new Borrowing();
        borrowing.setIdBorrow(rs.getInt("IdBorrow"));
        borrowing.setMemberId(rs.getInt("MemberID"));
        borrowing.setBookId(rs.getInt("BookID"));
        borrowing.setBookCopyId(rs.getInt("BookCopyId"));
        borrowing.setBorrowDate(rs.getDate("BorrowDate"));
        borrowing.setDueDate(rs.getDate("DueDate"));
        borrowing.setReturnDate(rs.getDate("ReturnDate"));
        borrowing.setStatus(rs.getString("Status"));
        borrowing.setCreatedAt(rs.getDate("CreatedAt"));
        return borrowing;
    }

    public Borrowing getBorrowingByMemberIdAndBookId(int memberId, int bookId) {
        String query = "SELECT * FROM Borrowing WHERE MemberID = ? AND BookID = ? AND ReturnDate IS NULL";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, memberId);
            ps.setInt(2, bookId);
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

    public List<Borrowing> getAllBorrowingsByMemberId(int memberId) {
        List<Borrowing> borrowings = new ArrayList<>();
        String query = "SELECT * FROM Borrowing WHERE MemberID = ?";
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

    public int getTotalBorrowedBooks() throws SQLException {
        String query = "SELECT COUNT(DISTINCT BookID) AS total FROM Borrowing WHERE Status = 'Borrowed'";
        try (PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        }
        return 0;
    }

    public int getTotalBorrowingMembers() throws SQLException {
        String query = "SELECT COUNT(DISTINCT MemberID) AS total FROM Borrowing WHERE Status = 'Borrowed'";
        try (PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        }
        return 0;
    }
}