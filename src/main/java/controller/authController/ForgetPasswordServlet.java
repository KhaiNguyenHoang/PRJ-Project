package controller.authController;

import dao.AccountDAO;
import dao.MembersDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "ForgotPasswordServlet", value = "/ForgotPassword")
public class ForgetPasswordServlet extends HttpServlet {
    private MembersDAO membersDAO;
    private AccountDAO accountDAO;

    @Override
    public void init() throws ServletException {
        membersDAO = new MembersDAO();
        accountDAO = new AccountDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Forward to forget password JSP page
        request.getRequestDispatcher("Auth/forget-password.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        // Validation
        if (email == null || email.trim().isEmpty() ||
                newPassword == null || newPassword.trim().isEmpty() ||
                confirmPassword == null || confirmPassword.trim().isEmpty()) {
            request.setAttribute("error", "missing_fields");
            request.getRequestDispatcher("Auth/forget-password.jsp").forward(request, response);
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            request.setAttribute("error", "password_mismatch");
            request.getRequestDispatcher("Auth/forget-password.jsp").forward(request, response);
            return;
        }

        // Try updating password for Member first
        boolean memberUpdated = membersDAO.forgetPassword(email, newPassword);

        // If member update fails, try Account
        if (!memberUpdated) {
            boolean accountUpdated = accountDAO.forgetPassword(email, newPassword);

            if (!accountUpdated) {
                request.setAttribute("error", "email_not_found");
                request.getRequestDispatcher("Auth/forget-password.jsp").forward(request, response);
                return;
            }
        }

        // Success - redirect to home page
        response.sendRedirect("HomePage");
    }
}