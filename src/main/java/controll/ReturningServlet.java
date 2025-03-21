package controll;

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
import java.util.Date;
import java.util.List;

@WebServlet(name = "ReturningServlet", value = "/Returning")
public class ReturningServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String JSP_PATH = "HomeHTML/HomeMemberHTML/Borrowing.jsp";

    // Centralized session check
    private Members getCurrentMember(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Members currentMember = (Members) request.getSession().getAttribute("members");
        if (currentMember == null) {
            response.sendRedirect("/Auth/SignIn-SignUp.jsp");
        }
        return currentMember;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Members currentMember = getCurrentMember(request, response);
        if (currentMember == null) return;

        try {
            // Extract historyId from the form
            String historyIdStr = request.getParameter("historyId");
            if (historyIdStr == null || historyIdStr.trim().isEmpty()) {
                request.setAttribute("errorMessage", "Invalid borrowing record ID.");
                refreshBorrowingHistory(request, currentMember);
                request.getRequestDispatcher(JSP_PATH).forward(request, response);
                return;
            }

            int historyId = Integer.parseInt(historyIdStr);
            Date returnDate = new Date(); // Current date as return date

            // Process the return
            BorrowingDAO borrowingDAO = new BorrowingDAO();
            BorrowingHistoryDAO historyDAO = new BorrowingHistoryDAO();

            // Find the BorrowingHistory record
            BorrowingHistory history = getBorrowingHistoryById(historyDAO, historyId, currentMember.getIdMember());
            if (history == null) {
                request.setAttribute("errorMessage", "Borrowing history record not found or does not belong to you.");
            } else if (history.getReturnDate() != null) {
                request.setAttribute("errorMessage", "This book has already been returned.");
            } else {
                // Use the new method to find the active Borrowing record
                Borrowing borrowing = borrowingDAO.getBorrowingByMemberIdAndBookId(currentMember.getIdMember(), history.getBookId());
                if (borrowing == null) {
                    request.setAttribute("errorMessage", "No active borrowing record found for this book.");
                } else {
                    boolean isSuccess = borrowingDAO.returnBook(borrowing.getIdBorrow(), returnDate);
                    if (isSuccess) {
                        request.setAttribute("message", "Book returned successfully!");
                    } else {
                        request.setAttribute("errorMessage", "Failed to return the book. Check server logs for details.");
                    }
                }
            }

            // Refresh borrowing history for the member
            refreshBorrowingHistory(request, currentMember);

        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid borrowing record ID format.");
            refreshBorrowingHistory(request, currentMember);
        } catch (SQLException e) {
            request.setAttribute("errorMessage", "Database error occurred while processing the return. Check server logs for details.");
            refreshBorrowingHistory(request, currentMember);
            e.printStackTrace();
        } catch (Exception e) {
            request.setAttribute("errorMessage", "An unexpected error occurred while processing the return. Check server logs for details.");
            refreshBorrowingHistory(request, currentMember);
            e.printStackTrace();
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
        } catch (SQLException e) {
            request.setAttribute("errorMessage", "Failed to refresh borrowing history: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Helper method to get BorrowingHistory by IdHistory and verify member
    private BorrowingHistory getBorrowingHistoryById(BorrowingHistoryDAO historyDAO, int historyId, int memberId) throws SQLException {
        List<BorrowingHistory> historyList = historyDAO.getBorrowingHistoryByMemberId(memberId);
        for (BorrowingHistory history : historyList) {
            if (history.getIdHistory() == historyId) {
                return history;
            }
        }
        return null;
    }
}