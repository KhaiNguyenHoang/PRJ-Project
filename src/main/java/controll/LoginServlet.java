package controll;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.dao.AccountDAO;
import model.dao.MembersDAO;
import model.entity.Account;
import model.entity.Members;

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
        MembersDAO membersDAO = new MembersDAO();
        Members members = membersDAO.login(email, password);

        // Kiểm tra nếu cả account và members đều không hợp lệ
        if (account == null && members == null) {
            request.setAttribute("error", "invalid_credentials");
            try {
                request.getRequestDispatcher("Auth/SignIn-SignUp.jsp").forward(request, response);
            } catch (ServletException | IOException e) {
                e.printStackTrace();
            }
        } else {
            // Nếu có tài khoản hợp lệ, tạo session và chuyển hướng
            HttpSession session = request.getSession();
            if (account != null) {
                session.setAttribute("user", account);
                request.getSession().setAttribute("account", account);
                try {
                    response.sendRedirect("HomePage");  // Chuyển hướng về trang chủ
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                if (members.getStatus().equalsIgnoreCase("Active")) {
                    session.setAttribute("user", members);
                    request.getSession().setAttribute("members", members);
                    try {
                        response.sendRedirect("HomePage");  // Chuyển hướng về trang chủ
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    request.setAttribute("error", "locked_account");
                    try {
                        request.getRequestDispatcher("Auth/SignIn-SignUp.jsp").forward(request, response);
                    } catch (ServletException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
