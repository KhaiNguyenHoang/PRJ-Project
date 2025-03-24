package controller.borrowController;

import dao.BookCopiesDAO;
import dao.BooksDAO;
import dao.BorrowingDAO;
import dao.BorrowingHistoryDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Books;
import model.Borrowing;
import model.BorrowingHistory;
import model.Members;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "BorrowingServlet", value = "/Borrowing")
public class BorrowingServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String JSP_PATH = "HomeHTML/HomeMemberHTML/Borrowing.jsp";
    private static final Logger LOGGER = Logger.getLogger(BorrowingServlet.class.getName());
    private static final int MAX_BORROWING_LIMIT = 5; // Giới hạn số sách mượn tối đa

    // Centralized session check
    private Members getCurrentMember(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Members currentMember = (Members) request.getSession().getAttribute("members");
        if (currentMember == null) {
            LOGGER.log(Level.WARNING, "No member found in session, redirecting to login");
            response.sendRedirect("HomePage");
            return null;
        } else if (!"Active".equalsIgnoreCase(currentMember.getStatus())) {
            LOGGER.log(Level.WARNING, "Member {0} is not active, redirecting to login", currentMember.getIdMember());
            response.sendRedirect("HomePage");
            return null;
        }
        return currentMember;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Members currentMember = getCurrentMember(request, response);
        if (currentMember == null) {
            return;
        }

        String keyword = request.getParameter("keyword");
        String bookIdParam = request.getParameter("id");

        try {
            // Always fetch borrowing history
            BorrowingHistoryDAO historyDAO = new BorrowingHistoryDAO();
            List<BorrowingHistory> borrowingHistoryList = historyDAO.getBorrowingHistoryByMemberId(currentMember.getIdMember());
            request.setAttribute("borrowingHistoryList", borrowingHistoryList);

            // Handle book selection for borrowing form
            if (bookIdParam != null && !bookIdParam.trim().isEmpty()) {
                int bookId = Integer.parseInt(bookIdParam);
                BooksDAO booksDAO = new BooksDAO();
                Books book = booksDAO.getBookById(bookId);
                if (book != null) {
                    request.setAttribute("selectedBook", book);
                } else {
                    request.setAttribute("errorMessage", "Book not found.");
                    LOGGER.log(Level.WARNING, "Book not found for ID: {0}", bookId);
                }
            }

            // Handle book search
            if (keyword != null && !keyword.trim().isEmpty()) {
                BooksDAO booksDAO = new BooksDAO();
                List<Books> booksList = booksDAO.searchBooks(keyword.trim()); // Cần triển khai searchBooks trong BooksDAO
                request.setAttribute("booksList", booksList);
                LOGGER.log(Level.INFO, "Searched books with keyword: {0}", keyword);
            }

        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid book ID format.");
            LOGGER.log(Level.WARNING, "Invalid book ID format: {0}", bookIdParam);
        } catch (SQLException e) {
            request.setAttribute("errorMessage", "Database error occurred while retrieving borrowing history.");
            LOGGER.log(Level.SEVERE, "Database error in doGet", e);
        } catch (Exception e) {
            request.setAttribute("errorMessage", "An unexpected error occurred.");
            LOGGER.log(Level.SEVERE, "Unexpected error in doGet", e);
        }

        request.getRequestDispatcher(JSP_PATH).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Members currentMember = getCurrentMember(request, response);
        if (currentMember == null) {
            return;
        }

        String bookIdStr = null;
        String dueDateStr = null;
        try {
            // Extract and validate form parameters
            bookIdStr = request.getParameter("bookId");
            dueDateStr = request.getParameter("dueDate");

            if (bookIdStr == null || dueDateStr == null || bookIdStr.trim().isEmpty() || dueDateStr.trim().isEmpty()) {
                request.setAttribute("errorMessage", "Missing required parameters.");
                LOGGER.log(Level.WARNING, "Missing parameters: bookId={0}, dueDate={1}", new Object[]{bookIdStr, dueDateStr});
            } else {
                int bookId = Integer.parseInt(bookIdStr);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                dateFormat.setLenient(false);
                Timestamp dueDate = new Timestamp(dateFormat.parse(dueDateStr).getTime());
                Timestamp borrowDate = new Timestamp(System.currentTimeMillis());

                // Validate due date
                if (dueDate.before(borrowDate)) {
                    request.setAttribute("errorMessage", "Due date cannot be before today's date.");
                    LOGGER.log(Level.WARNING, "Due date {0} is before borrow date {1}", new Object[]{dueDate, borrowDate});
                } else {
                    BorrowingDAO borrowingDAO = new BorrowingDAO();
                    List<Borrowing> activeBorrowings = borrowingDAO.getAllBorrowingsByMemberId(currentMember.getIdMember());
                    int currentBorrowCount = (int) activeBorrowings.stream().filter(b -> b.getReturnDate() == null).count();
                    if (currentBorrowCount >= MAX_BORROWING_LIMIT) {
                        request.setAttribute("errorMessage", "You have reached the maximum borrowing limit (" + MAX_BORROWING_LIMIT + " books).");
                        LOGGER.log(Level.WARNING, "Member {0} exceeded borrowing limit", currentMember.getIdMember());
                    } else {
                        BookCopiesDAO bookCopiesDAO = new BookCopiesDAO();
                        if (!bookCopiesDAO.hasAvailableCopy(bookId)) {
                            request.setAttribute("errorMessage", "No available copies of the selected book.");
                            LOGGER.log(Level.WARNING, "No available copies for BookID: {0}", bookId);
                        } else {
                            int bookCopyId = bookCopiesDAO.getFirstAvailableBookCopy(bookId).getIdCopy();
                            Borrowing borrowing = new Borrowing(currentMember.getIdMember(), bookCopyId, borrowDate, dueDate, "Borrowed");

                            boolean isSuccess = borrowingDAO.borrowBook(borrowing);
                            if (isSuccess) {
                                request.setAttribute("message", "Book borrowed successfully!");
                                LOGGER.log(Level.INFO, "Book borrowed successfully by member {0}, BookCopyId: {1}",
                                        new Object[]{currentMember.getIdMember(), bookCopyId});
                            } else {
                                request.setAttribute("errorMessage", "Failed to borrow the book. Check server logs for details.");
                                LOGGER.log(Level.SEVERE, "Failed to borrow book for BookID: {0}", bookId);
                            }
                        }
                    }
                }
            }

            // Refresh borrowing history
            refreshBorrowingHistory(request, currentMember);

        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid book ID format.");
            LOGGER.log(Level.WARNING, "Invalid book ID format: {0}", bookIdStr);
            refreshBorrowingHistory(request, currentMember);
        } catch (ParseException e) {
            request.setAttribute("errorMessage", "Invalid due date format. Please use yyyy-MM-dd.");
            LOGGER.log(Level.WARNING, "Invalid due date format: {0}", dueDateStr);
            refreshBorrowingHistory(request, currentMember);
        } catch (Exception e) {
            request.setAttribute("errorMessage", "An unexpected error occurred.");
            LOGGER.log(Level.SEVERE, "Unexpected error in doPost", e);
            refreshBorrowingHistory(request, currentMember);
        }

        request.getRequestDispatcher(JSP_PATH).forward(request, response);
    }

    // Helper method to refresh borrowing history
    private void refreshBorrowingHistory(HttpServletRequest request, Members currentMember) {
        try {
            BorrowingHistoryDAO historyDAO = new BorrowingHistoryDAO();
            List<BorrowingHistory> borrowingHistoryList = historyDAO.getBorrowingHistoryByMemberId(currentMember.getIdMember());
            request.setAttribute("borrowingHistoryList", borrowingHistoryList);
        } catch (SQLException e) {
            request.setAttribute("errorMessage", "Failed to refresh borrowing history.");
            LOGGER.log(Level.SEVERE, "Error refreshing borrowing history for member: {0}", currentMember.getIdMember());
        }
    }
}