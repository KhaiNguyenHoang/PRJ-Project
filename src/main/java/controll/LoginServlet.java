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

@WebServlet(name = "LoginServlet", value = "/LoginServlet")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("emailLogin");
        String password = request.getParameter("passwordLogin");

        // Kiểm tra nếu email hoặc password là null hoặc rỗng
        if (email == null || password == null || email.isEmpty() || password.isEmpty()) {
            request.setAttribute("error", "missing_credentials");
            try {
                request.getRequestDispatcher("Auth/SignIn-SignUp.jsp").forward(request, response);
            } catch (ServletException | IOException e) {
                e.printStackTrace();
            }
            return;
        }

        // Khởi tạo AccountDAO để gọi phương thức login
        AccountDAO accountDAO = new AccountDAO();
        Account account = accountDAO.login(email, password);

        if (account == null) {
            request.setAttribute("error", "invalid_credentials");
            try {
                request.getRequestDispatcher("Auth/SignIn-SignUp.jsp").forward(request, response);
            } catch (ServletException | IOException e) {
                e.printStackTrace();
            }
        } else {
            HttpSession session = request.getSession();
            session.setAttribute("user", account);
            request.getSession().setAttribute("account", account);
            try {
                request.getRequestDispatcher("index.jsp").forward(request, response);
            } catch (ServletException | IOException e) {
                e.printStackTrace();
            }
        }
    }
}
