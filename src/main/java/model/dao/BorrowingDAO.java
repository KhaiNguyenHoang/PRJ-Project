package model.dao;

import model.entity.Borrowing;
import model.utils.LibraryContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BorrowingDAO extends LibraryContext {

    public BorrowingDAO() {
        super();
    }

    public static void main(String[] args) {
        try {
            // Use current date as java.util.Date
            Date today = new Date();
            BookCopiesDAO bookCopiesDAO = new BookCopiesDAO();
            // Create Borrowing object with proper constructor
            Borrowing borrowing = new Borrowing(8, 38, bookCopiesDAO.getFirstAvailableBookCopy(38).getIdCopy(), today, today, "Borrowed");

            // Initialize BorrowingDAO and borrow book
            BorrowingDAO borrowingDAO = new BorrowingDAO();
            boolean isSuccess = borrowingDAO.borrowBook(borrowing);

            if (isSuccess) {
                System.out.println("Book return successfully.");
            } else {
                System.out.println("Failed to return book.");
            }
        } catch (Exception e) {
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
                return false;
            }

            // Check date validity using Date's before method
            if (borrowing.getDueDate().before(borrowing.getBorrowDate())) {
                return false;
            }

            String borrowQuery = "INSERT INTO Borrowing (MemberID, BookID, BookCopyId, BorrowDate, DueDate, Status) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(borrowQuery);

            // Convert java.util.Date to java.sql.Date
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
                conn.rollback();
                return false;
            }

            boolean bookUpdated = bookCopiesDAO.updateBookCopyStatus(borrowing.getBookCopyId(), "Borrowed");
            if (!bookUpdated) {
                conn.rollback();
                return false;
            }

            booksDAO.updateBookStatusBasedOnCopies(borrowing.getBookId());

            boolean historyAdded = borrowingHistoryDAO.addBorrowingHistory(borrowing);
            if (!historyAdded) {
                conn.rollback();
                return false;
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (conn != null) {
                    conn.rollback();
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
                    conn.setAutoCommit(true);
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return false;
    }

    public boolean returnBook(int idBorrow, Date returnDate) {
        PreparedStatement ps = null;
        Borrowing borrowing = null;
        BookCopiesDAO bookCopiesDAO = new BookCopiesDAO();
        BorrowingHistoryDAO borrowingHistoryDAO = new BorrowingHistoryDAO();

        try {
            conn.setAutoCommit(false);

            borrowing = getBorrowingById(idBorrow);
            if (borrowing == null) {
                conn.rollback();
                return false;
            }

            // Check date validity using Date's before method
            if (returnDate.before(borrowing.getBorrowDate())) {
                return false;
            }

            String updateReturnQuery = "UPDATE Borrowing SET ReturnDate = ?, Status = 'Returned' WHERE IdBorrow = ?";
            ps = conn.prepareStatement(updateReturnQuery);

            java.sql.Date sqlReturnDate = new java.sql.Date(returnDate.getTime());
            ps.setDate(1, sqlReturnDate);
            ps.setInt(2, idBorrow);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                conn.rollback();
                return false;
            }

            boolean bookUpdated = bookCopiesDAO.updateBookCopyStatus(borrowing.getBookCopyId(), "Available");
            if (!bookUpdated) {
                conn.rollback();
                return false;
            }

            BooksDAO booksDAO = new BooksDAO();
            booksDAO.updateBookStatusBasedOnCopies(borrowing.getBookId());

            // Update return date in borrowing object for history
            borrowing.setReturnDate(returnDate);
            boolean historyAdded = borrowingHistoryDAO.updateReturnHistory(borrowing);
            if (!historyAdded) {
                conn.rollback();
                return false;
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (conn != null) {
                    conn.rollback();
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
                    conn.setAutoCommit(true);
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return false;
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
        borrowing.setBorrowDate(rs.getDate("BorrowDate")); // java.sql.Date is a subclass of java.util.Date
        borrowing.setDueDate(rs.getDate("DueDate"));
        borrowing.setReturnDate(rs.getDate("ReturnDate"));
        borrowing.setStatus(rs.getString("Status"));
        borrowing.setCreatedAt(rs.getDate("CreatedAt"));
        return borrowing;
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
}