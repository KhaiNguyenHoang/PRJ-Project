package controller.borrowController;

import dao.BorrowingDAO;
import dao.BorrowingHistoryDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Borrowing;
import model.BorrowingHistory;
import model.Members;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "ReturningServlet", value = "/Returning")
public class ReturningServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String JSP_PATH = "HomeHTML/HomeMemberHTML/Borrowing.jsp";
    private static final Logger LOGGER = Logger.getLogger(ReturningServlet.class.getName());

    // Centralized session check
    private Members getCurrentMember(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Members currentMember = (Members) request.getSession().getAttribute("members");
        if (currentMember == null) {
            LOGGER.log(Level.WARNING, "No member found in session, redirecting to login");
            response.sendRedirect("/Auth/SignIn-SignUp.jsp");
        } else if (!"active".equalsIgnoreCase(currentMember.getStatus())) {
            LOGGER.log(Level.WARNING, "Member {0} is not active, redirecting to login", currentMember.getIdMember());
            request.getSession().invalidate();
            response.sendRedirect("/Auth/SignIn-SignUp.jsp");
        }
        return currentMember;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Members currentMember = getCurrentMember(request, response);
        if (currentMember == null) return;

        try {
            // Extract borrowId from the form
            String borrowIdStr = request.getParameter("borrowId");
            if (borrowIdStr == null || borrowIdStr.trim().isEmpty()) {
                request.setAttribute("errorMessage", "Invalid borrowing record ID.");
                refreshBorrowingHistory(request, currentMember);
                request.getRequestDispatcher(JSP_PATH).forward(request, response);
                return;
            }

            int borrowId = Integer.parseInt(borrowIdStr);
            Timestamp returnDate = new Timestamp(System.currentTimeMillis()); // Current timestamp as return date

            // Process the return
            BorrowingDAO borrowingDAO = new BorrowingDAO();
            Borrowing borrowing = borrowingDAO.getBorrowingById(borrowId);

            if (borrowing == null) {
                request.setAttribute("errorMessage", "Borrowing record not found.");
                LOGGER.log(Level.WARNING, "Borrowing record not found for borrowId: {0}", borrowId);
            } else if (borrowing.getMemberId() != currentMember.getIdMember()) {
                request.setAttribute("errorMessage", "This borrowing record does not belong to you.");
                LOGGER.log(Level.WARNING, "Member {0} attempted to return a book not borrowed by them: borrowId {1}",
                        new Object[]{currentMember.getIdMember(), borrowId});
            } else if (borrowing.getReturnDate() != null) {
                request.setAttribute("errorMessage", "This book has already been returned.");
                LOGGER.log(Level.WARNING, "Book already returned for borrowId: {0}", borrowId);
            } else if (!"Borrowed".equalsIgnoreCase(borrowing.getStatus())) {
                request.setAttribute("errorMessage", "This book is not currently borrowed.");
                LOGGER.log(Level.WARNING, "Book not in Borrowed state for borrowId: {0}", borrowId);
            } else {
                boolean isSuccess = borrowingDAO.returnBook(borrowId, returnDate);
                if (isSuccess) {
                    request.setAttribute("message", "Book returned successfully!");
                    LOGGER.log(Level.INFO, "Book returned successfully for borrowId: {0} by member: {1}",
                            new Object[]{borrowId, currentMember.getIdMember()});
                    BorrowingHistoryDAO historyDAO = new BorrowingHistoryDAO();
                    historyDAO.deleteBorrowingHistory();
                } else {
                    request.setAttribute("errorMessage", "Failed to return the book. Please try again or contact support.");
                    LOGGER.log(Level.SEVERE, "Failed to return book for borrowId: {0}", borrowId);
                }
            }

            // Refresh borrowing history for the member
            refreshBorrowingHistory(request, currentMember);

        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid borrowing record ID format.");
            LOGGER.log(Level.WARNING, "Invalid borrowId format: {0}", request.getParameter("borrowId"));
            refreshBorrowingHistory(request, currentMember);
        } catch (Exception e) {
            request.setAttribute("errorMessage", "An unexpected error occurred while processing the return: " + e.getMessage());
            LOGGER.log(Level.SEVERE, "Unexpected error while returning book", e);
            refreshBorrowingHistory(request, currentMember);
        }

        // Forward back to Borrowing.jsp to display the result
        request.getRequestDispatcher(JSP_PATH).forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Redirect GET requests to BorrowingServlet to avoid direct access
        response.sendRedirect("/Borrowing");
    }

    // Helper method to refresh borrowing history
    private void refreshBorrowingHistory(HttpServletRequest request, Members currentMember) {
        try {
            BorrowingHistoryDAO historyDAO = new BorrowingHistoryDAO();
            List<BorrowingHistory> borrowingHistoryList = historyDAO.getBorrowingHistoryByMemberId(currentMember.getIdMember());
            request.setAttribute("borrowingHistoryList", borrowingHistoryList);
            if (borrowingHistoryList.isEmpty()) {
                request.setAttribute("infoMessage", "You have no borrowing history yet.");
            }
        } catch (SQLException e) {
            request.setAttribute("errorMessage", "Failed to load borrowing history: " + e.getMessage());
            LOGGER.log(Level.SEVERE, "Error refreshing borrowing history for member: {0}", currentMember.getIdMember());
        }
    }
}