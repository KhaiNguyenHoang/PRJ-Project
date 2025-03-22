package dao;

import model.Borrowing;
import model.Fines;
import utils.LibraryContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BorrowingDAO extends LibraryContext {
    private static final Logger LOGGER = Logger.getLogger(BorrowingDAO.class.getName());

    public BorrowingDAO() {
        super();
    }

    // Main method for testing (đã sửa)
    public static void main(String[] args) {
        BorrowingDAO borrowingDAO = new BorrowingDAO();
        FinesDAO finesDAO = new FinesDAO();
        BookCopiesDAO bookCopiesDAO = new BookCopiesDAO();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        try {
            // Bước 1: Giả lập mượn sách
            System.out.println("=== STEP 1: BORROW BOOK ===");
            Timestamp borrowDate = new Timestamp(sdf.parse("01/03/2025").getTime());
            Timestamp dueDate = new Timestamp(sdf.parse("10/03/2025").getTime());
            int memberId = 11;
            int bookCopyId = bookCopiesDAO.getFirstAvailableBookCopy(1).getIdCopy();

            Borrowing borrowing = new Borrowing(memberId, bookCopyId, borrowDate, dueDate, "Borrowed");
            boolean borrowSuccess = borrowingDAO.borrowBook(borrowing);
            if (borrowSuccess) {
                System.out.println("Member " + memberId + " borrowed book copy ID " + bookCopyId + " successfully.");
                System.out.println("Borrow Date: " + sdf.format(borrowDate) + ", Due Date: " + sdf.format(dueDate));
            } else {
                System.out.println("Failed to borrow book.");
                return;
            }

            // Bước 2: Giả lập trả sách trễ
            System.out.println("\n=== STEP 2: RETURN BOOK LATE ===");
            Timestamp returnDate = new Timestamp(sdf.parse("15/03/2025").getTime());
            int borrowId = borrowing.getIdBorrow();
            boolean returnSuccess = borrowingDAO.returnBook(borrowId, returnDate);
            if (returnSuccess) {
                System.out.println("Book returned successfully on " + sdf.format(returnDate));
                System.out.println("Overdue: " + borrowingDAO.calculateDaysBetween(dueDate, returnDate) + " days.");
            } else {
                System.out.println("Failed to return book.");
                return;
            }

            // Bước 3: Kiểm tra khoản nợ
            System.out.println("\n=== STEP 3: CHECK FINES ===");
            List<Fines> finesList = finesDAO.getFinesByMemberId(memberId);
            if (finesList.isEmpty()) {
                System.out.println("No fines found for member " + memberId);
            } else {
                System.out.println("List of fines for member " + memberId + ":");
                for (Fines fine : finesList) {
                    System.out.println(" - Fine ID: " + fine.getIdFine() +
                            ", BorrowID: " + fine.getBorrowId() +
                            ", Amount: " + fine.getAmount() + " USD" +
                            ", Status: " + fine.getStatus() +
                            ", Payment Method: " + fine.getPaymentMethod() +
                            ", Created At: " + sdf.format(fine.getCreatedAt()));
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in demo", e);
            System.out.println("Error in demo: " + e.getMessage());
        }
    }

    /**
     * Thêm một giao dịch mượn sách mới và tạo bản ghi lịch sử mượn
     *
     * @param borrowing Đối tượng Borrowing chứa thông tin mượn
     * @return true nếu mượn thành công, false nếu thất bại
     */
    public boolean borrowBook(Borrowing borrowing) {
        String borrowQuery = "INSERT INTO Borrowing (MemberID, BookCopyId, BorrowDate, DueDate, Status) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(borrowQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
            conn.setAutoCommit(false);

            BookCopiesDAO bookCopiesDAO = new BookCopiesDAO();
            int bookId = bookCopiesDAO.getBookIdByCopyId(borrowing.getBookCopyId());
            if (!bookCopiesDAO.hasAvailableCopy(bookId)) {
                LOGGER.log(Level.WARNING, "No available copies for BookCopyId: {0}", borrowing.getBookCopyId());
                return false;
            }

            Borrowing existingBorrowing = getBorrowingByMemberIdAndBookCopyId(borrowing.getMemberId(), borrowing.getBookCopyId());
            if (existingBorrowing != null && existingBorrowing.getReturnDate() == null) {
                LOGGER.log(Level.WARNING, "Member {0} has already borrowed BookCopyId {1}",
                        new Object[]{borrowing.getMemberId(), borrowing.getBookCopyId()});
                return false;
            }

            if (borrowing.getDueDate().before(borrowing.getBorrowDate())) {
                LOGGER.log(Level.WARNING, "Due date {0} is before borrow date {1}",
                        new Object[]{borrowing.getDueDate(), borrowing.getBorrowDate()});
                return false;
            }

            ps.setInt(1, borrowing.getMemberId());
            ps.setInt(2, borrowing.getBookCopyId());
            ps.setTimestamp(3, borrowing.getBorrowDate());
            ps.setTimestamp(4, borrowing.getDueDate());
            ps.setString(5, borrowing.getStatus());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                conn.rollback();
                return false;
            }

            // Lấy ID vừa tạo
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    borrowing.setIdBorrow(rs.getInt(1));
                }
            }

            // Cập nhật trạng thái bản sao
            boolean copyUpdated = bookCopiesDAO.updateBookCopyStatus(borrowing.getBookCopyId(), "Borrowed");
            if (!copyUpdated) {
                conn.rollback();
                return false;
            }

            // Cập nhật trạng thái sách trong Books
            BooksDAO booksDAO = new BooksDAO();
            booksDAO.updateBookStatusBasedOnCopies(bookId);

            // Tạo bản ghi lịch sử mượn
            BorrowingHistoryDAO historyDAO = new BorrowingHistoryDAO();
            boolean historyCreated = historyDAO.addBorrowingHistory(borrowing);
            if (!historyCreated) {
                LOGGER.log(Level.SEVERE, "Failed to create borrowing history for BorrowID: {0}", borrowing.getIdBorrow());
                conn.rollback();
                return false;
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error borrowing book", e);
            try {
                conn.rollback();
            } catch (SQLException rollbackEx) {
                LOGGER.log(Level.SEVERE, "Rollback failed", rollbackEx);
            }
            return false;
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Failed to reset auto-commit", e);
            }
        }
    }

    /**
     * Trả sách và cập nhật trạng thái
     *
     * @param idBorrow   ID của giao dịch mượn
     * @param returnDate Ngày trả sách
     * @return true nếu trả thành công, false nếu thất bại
     */
    public boolean returnBook(int idBorrow, Timestamp returnDate) {
        String updateQuery = "UPDATE Borrowing SET ReturnDate = ?, Status = 'Returned' WHERE IdBorrow = ?";
        try (PreparedStatement ps = conn.prepareStatement(updateQuery)) {
            conn.setAutoCommit(false);

            Borrowing borrowing = getBorrowingById(idBorrow);
            if (borrowing == null) {
                LOGGER.log(Level.WARNING, "Borrowing not found for IdBorrow: {0}", idBorrow);
                return false;
            }

            if (returnDate.before(borrowing.getBorrowDate())) {
                LOGGER.log(Level.WARNING, "Return date {0} is before borrow date {1}",
                        new Object[]{returnDate, borrowing.getBorrowDate()});
                return false;
            }

            // Kiểm tra và tạo khoản phạt nếu cần
            FinesDAO finesDAO = new FinesDAO();
            finesDAO.createFineIfNeeded(idBorrow);

            ps.setTimestamp(1, returnDate);
            ps.setInt(2, idBorrow);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                conn.rollback();
                return false;
            }

            // Cập nhật trạng thái bản sao
            BookCopiesDAO bookCopiesDAO = new BookCopiesDAO();
            boolean copyUpdated = bookCopiesDAO.updateBookCopyStatus(borrowing.getBookCopyId(), "Available");
            if (!copyUpdated) {
                conn.rollback();
                return false;
            }

            // Cập nhật trạng thái sách
            BooksDAO booksDAO = new BooksDAO();
            int bookId = bookCopiesDAO.getBookIdByCopyId(borrowing.getBookCopyId());
            booksDAO.updateBookStatusBasedOnCopies(bookId);

            conn.commit();
            return true;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error returning book", e);
            try {
                conn.rollback();
            } catch (SQLException rollbackEx) {
                LOGGER.log(Level.SEVERE, "Rollback failed", rollbackEx);
            }
            return false;
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Failed to reset auto-commit", e);
            }
        }
    }

    /**
     * Lấy thông tin giao dịch mượn theo ID
     *
     * @param idBorrow ID của giao dịch mượn
     * @return Đối tượng Borrowing hoặc null nếu không tìm thấy
     */
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
            LOGGER.log(Level.SEVERE, "Error retrieving borrowing by ID", e);
        }
        return null;
    }

    /**
     * Lấy thông tin giao dịch mượn theo MemberID và BookCopyId
     *
     * @param memberId   ID của thành viên
     * @param bookCopyId ID của bản sao sách
     * @return Đối tượng Borrowing hoặc null nếu không tìm thấy
     */
    public Borrowing getBorrowingByMemberIdAndBookCopyId(int memberId, int bookCopyId) {
        String query = "SELECT * FROM Borrowing WHERE MemberID = ? AND BookCopyId = ? AND ReturnDate IS NULL";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, memberId);
            ps.setInt(2, bookCopyId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToBorrowing(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving borrowing by member and book copy", e);
        }
        return null;
    }

    /**
     * Lấy tất cả giao dịch mượn của một thành viên
     *
     * @param memberId ID của thành viên
     * @return Danh sách các Borrowing
     */
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
            LOGGER.log(Level.SEVERE, "Error retrieving all borrowings by member", e);
        }
        return borrowings;
    }

    /**
     * Tính số ngày giữa hai ngày
     *
     * @param startDate Ngày bắt đầu
     * @param endDate   Ngày kết thúc
     * @return Số ngày chênh lệch
     */
    private long calculateDaysBetween(Timestamp startDate, Timestamp endDate) {
        long diffInMillies = Math.max(endDate.getTime() - startDate.getTime(), 0);
        return java.util.concurrent.TimeUnit.DAYS.convert(diffInMillies, java.util.concurrent.TimeUnit.MILLISECONDS);
    }

    /**
     * Chuyển đổi ResultSet thành đối tượng Borrowing
     *
     * @param rs ResultSet từ truy vấn
     * @return Đối tượng Borrowing
     * @throws SQLException Nếu có lỗi khi đọc dữ liệu
     */
    private Borrowing mapResultSetToBorrowing(ResultSet rs) throws SQLException {
        Borrowing borrowing = new Borrowing();
        borrowing.setIdBorrow(rs.getInt("IdBorrow"));
        borrowing.setMemberId(rs.getInt("MemberID"));
        borrowing.setBookCopyId(rs.getInt("BookCopyId"));
        borrowing.setBorrowDate(rs.getTimestamp("BorrowDate"));
        borrowing.setDueDate(rs.getTimestamp("DueDate"));
        borrowing.setReturnDate(rs.getTimestamp("ReturnDate"));
        borrowing.setStatus(rs.getString("Status"));
        borrowing.setCreatedAt(rs.getTimestamp("CreatedAt"));
        return borrowing;
    }

    /**
     * Đếm tổng số sách đang được mượn
     *
     * @return Tổng số bản sao sách đang mượn
     * @throws SQLException Nếu có lỗi truy vấn
     */
    public int getTotalBorrowedBooks() throws SQLException {
        String query = "SELECT COUNT(DISTINCT BookCopyId) AS total FROM Borrowing WHERE Status = 'Borrowed'";
        try (PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        }
        return 0;
    }

    /**
     * Đếm tổng số thành viên đang mượn sách
     *
     * @return Tổng số thành viên
     * @throws SQLException Nếu có lỗi truy vấn
     */
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

    public List<Borrowing> getAllBorrowingHistory() {
        List<Borrowing> borrowingList = new ArrayList<>();
        String query = "SELECT * FROM Borrowing";
        try (PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                borrowingList.add(mapResultSetToBorrowing(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving borrowing history", e);
        }
        return borrowingList;
    }
}