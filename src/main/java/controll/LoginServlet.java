package controll;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.dao.AccountDAO;
import model.entity.Account;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "LoginServlet", value = "/LoginServlet")
public class LoginServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(LoginServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Lấy thông tin email và password từ form đăng nhập
        String email = request.getParameter("emailLogin");
        String password = request.getParameter("passwordLogin");

        // Kiểm tra nếu email hoặc password là null hoặc rỗng
        if (email == null || password == null || email.isEmpty() || password.isEmpty()) {
            response.sendRedirect("Auth/SignIn-SignUp.jsp?error=missing_credentials");
            return;  // Dừng lại nếu thiếu thông tin
        }

        // Khởi tạo AccountDAO để gọi phương thức login
        AccountDAO accountDAO = new AccountDAO();
        Account account = null;

        try {
            // Gọi phương thức login từ AccountDAO
            account = accountDAO.login(email, password);

            if (account == null) {
                logger.warning("Login failed for email: " + email);
                response.sendRedirect("Auth/SignIn-SignUp.jsp?error=invalid_credentials");
            } else {
                logger.info("Login successful for email: " + email);
                request.getSession().setAttribute("account", account);
                response.sendRedirect("index.html");
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Login failed due to an error.", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred during login.");
        }
    }
}
