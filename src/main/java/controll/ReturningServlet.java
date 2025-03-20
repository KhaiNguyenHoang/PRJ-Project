package controll;

import dao.BorrowingDAO;
import dao.BorrowingHistoryDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.BorrowingHistory;
import model.Members;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@WebServlet(name = "ReturningServlet", value = "/Returning")
public class ReturningServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String JSP_PATH = "HomeHTML/HomeMemberHTML/Borrowing.jsp";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if user is logged in
        Members currentMember = (Members) request.getSession().getAttribute("user");
        if (currentMember == null) {
            response.sendRedirect("/Auth/SignIn-SignUp.jsp");
            return;
        }

        try {
            // Extract historyId from the form (assuming BorrowingHistory.id maps to Borrowing.idBorrow)
            String historyIdStr = request.getParameter("historyId");
            if (historyIdStr == null || historyIdStr.isEmpty()) {
                request.setAttribute("errorMessage", "Invalid borrowing record ID.");
                request.getRequestDispatcher(JSP_PATH).forward(request, response);
                return;
            }

            int idBorrow = Integer.parseInt(historyIdStr);
            Date returnDate = new Date(); // Current date as return date

            // Process the return
            BorrowingDAO borrowingDAO = new BorrowingDAO();
            boolean isSuccess = borrowingDAO.returnBook(idBorrow, returnDate);

            if (isSuccess) {
                request.setAttribute("message", "Book returned successfully!");
            } else {
                request.setAttribute("errorMessage", "Failed to return the book. It may have already been returned or an error occurred.");
            }

            // Refresh borrowing history for the member
            List<BorrowingHistory> borrowingHistoryList = new BorrowingHistoryDAO().getBorrowingHistoryByMemberId(currentMember.getIdMember());
            request.setAttribute("borrowingHistoryList", borrowingHistoryList);

        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid borrowing record ID format.");
        } catch (Exception e) {
            request.setAttribute("errorMessage", "An error occurred while processing the return.");
            e.printStackTrace(); // Log the exception for debugging
        }

        // Forward back to Borrowing.jsp to display the result
        request.getRequestDispatcher(JSP_PATH).forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Redirect GET requests to BorrowingServlet to avoid direct access
        response.sendRedirect("/Borrowing");
    }
}