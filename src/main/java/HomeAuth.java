import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "HomeAuth", value = "/homeAuth")
public class HomeAuth extends HttpServlet {
    private String message;

    public void init() {
        // Lấy tham số URL và tên từ init-param trong web.xml
        String initUrl = getServletConfig().getInitParameter("url");
        String initName = getServletConfig().getInitParameter("name");

        if (initUrl != null && initName != null) {
            message = "Received URL: " + initUrl + " and Name: " + initName;
        } else {
            message = "Hello World!";
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.getRequestDispatcher("/Auth/SignIn-SignUp.jsp").forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

    }

    public void destroy() {
    }
}
