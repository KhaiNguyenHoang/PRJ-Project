package controller;

import dao.FinesDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Members;

import java.io.IOException;
import java.util.Date;

@WebServlet(name = "PayFineServlet", value = "/PayFine ")
public class PayFineServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get the current logged-in member
        Members currentMember = (Members) request.getSession().getAttribute("user");

        if (currentMember == null) {
            response.sendRedirect("/Auth/SignIn-SignUp.jsp");
            return;
        }

        int fineId = Integer.parseInt(request.getParameter("fineId"));
        Date paidDate = new Date(); // Current date as payment date

        // FinesDAO to handle fine payment logic
        FinesDAO finesDAO = new FinesDAO();

        // Try to pay the fine
        boolean isSuccess = finesDAO.updateFineStatus(fineId, "Paid", paidDate);

        if (isSuccess) {
            response.sendRedirect("Borrowing.jsp?message=Fine paid successfully!");
        } else {
            response.sendRedirect("Borrowing.jsp?message=Failed to pay the fine. Please try again.");
        }
    }
}
