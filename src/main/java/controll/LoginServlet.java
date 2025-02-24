package controll;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.dao.AccountDAO;
import model.entity.Account;

import java.io.IOException;
import java.util.logging.Logger;

@WebServlet(name = "LoginServlet", value = "/LoginServlet")
public class LoginServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(LoginServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("emailLogin");
        String password = request.getParameter("passwordLogin");

        // Kiểm tra nếu email hoặc password là null hoặc rỗng
        if (email == null || password == null || email.isEmpty() || password.isEmpty()) {
            request.setAttribute("error", "missing_credentials");
            request.getRequestDispatcher("Auth/SignIn-SignUp.jsp").forward(request, response);
            return;
        }

        // Khởi tạo AccountDAO để gọi phương thức login
        AccountDAO accountDAO = new AccountDAO();
        Account account = accountDAO.login(email, password);

        if (account == null) {
            logger.warning("Login failed for email: " + email);
            request.setAttribute("error", "invalid_credentials");
            request.getRequestDispatcher("Auth/SignIn-SignUp.jsp").forward(request, response);
        } else {
            logger.info("Login successful for email: " + email);
            HttpSession session = request.getSession();
            session.setAttribute("user", account);
            request.getSession().setAttribute("account", account);
            response.sendRedirect("index.html");
        }
    }
}
