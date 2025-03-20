package controll;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.dao.BorrowingDAO;
import model.entity.Members;

import java.io.IOException;
import java.util.Date;

@WebServlet(name = "ReturnBookServlet", value = "/Returning ")
public class ReturnBookServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get the current logged-in member
        Members currentMember = (Members) request.getSession().getAttribute("user");

        if (currentMember == null) {
            response.sendRedirect("/Auth/SignIn-SignUp.jsp");
            return;
        }

        int borrowId = Integer.parseInt(request.getParameter("borrowId"));
        Date returnDate = new Date(); // Current date as return date

        // BorrowingDAO to handle return logic
        BorrowingDAO borrowingDAO = new BorrowingDAO();

        // Try to return the book
        boolean isSuccess = borrowingDAO.returnBook(borrowId, returnDate);

        if (isSuccess) {
            response.sendRedirect("Borrowing.jsp?message=Book returned successfully!");
        } else {
            response.sendRedirect("Borrowing.jsp?message=Failed to return the book. Please try again.");
        }
    }
}
